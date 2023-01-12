package it.cambi.qrgui.services.taskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.query.model.Attribute;
import it.cambi.qrgui.query.model.Constraint;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.query.model.SelectColumns;
import it.cambi.qrgui.util.Constants;
import it.cambi.qrgui.util.DateUtils;
import it.cambi.qrgui.util.WhereConditionOperator;
import it.cambi.qrgui.util.WrappingUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.cambi.qrgui.util.Constants.YYYY_MM_DD;
import static it.cambi.qrgui.util.Constants.YYYY_MM_DD_HH_MI_SS;

@Service
@RequiredArgsConstructor
public class QueryService {

    private final ObjectMapper objectMapper;
    private final WrappedResponse<QueryToJson> responseQueryToJson = new WrappedResponse<>();
    private final WrappedResponse<String> responseString = new WrappedResponse<>();

    public WrappedResponse<QueryToJson> checkQuery(
            UteQueDto query, boolean execQuery, BiFunction<String, Integer, List<Object>> function) throws IOException {

        QueryToJson json = objectMapper.readValue(query.getJson(), QueryToJson.class);

        if (null == json.getStatement())
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(List.of("Statement della query non presente"))
                    .build();

        if (json.getStatement().toLowerCase().contains("insert")
                || json.getStatement().toLowerCase().contains("drop")
                || json.getStatement().toLowerCase().contains("update")
                || json.getStatement().toLowerCase().contains("create")
                || json.getStatement().toLowerCase().contains("delete"))
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(List.of("E' consentito solo l'uso di Select"))
                    .build();

        int indexSelectAll = json.getStatement().toLowerCase().indexOf("select *");
        int indexSelect = json.getStatement().toLowerCase().indexOf("select");

        if (indexSelectAll - indexSelect == 0 && indexSelectAll != -1)
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(
                            List.of(
                                    "Non è possibile eseguire select *, è necessario indicare il nome degli attributi, meglio se con un alias per renderli più espliciti nel risultato"))
                    .build();

        if (json.getStatement().toLowerCase().contains("--")
                || json.getStatement().toLowerCase().contains("/*"))
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(
                            List.of(
                                    "Non è consentito l'uso dei commenti in quanto potrebbero alterare la formattazione della query"))
                    .build();

        if (null == query.getNam())
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(List.of("E' necessario indicare un nome per la query"))
                    .build();

        if (null == json.getQuerySelectColumns() || json.getQuerySelectColumns().size() == 0)
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(List.of("E' necessario indicare almeno una colonna di output "))
                    .build();

        /** Controllo tipo e alias delle colonne */
        int j = 1;

        for (SelectColumns column : json.getQuerySelectColumns()) {
            if (null == column.getAs() || null == column.getType()) {
                return responseQueryToJson.toBuilder()
                        .success(false)
                        .errorMessage(List.of("Tipo od alias non definito per colonna " + j))
                        .build();
            }
            j++;
        }

        /** Pulisco la stringa della query da eventuali ritorni a capo, punto e virgola */
        String cleanedStatement = WrappingUtils.cleanQueryString(json.getStatement());

        /** Controllo che ci siano tutti i parametri */
        int count = 0;
        Pattern p = Pattern.compile("&\\w+"); // the pattern to search for
        Matcher m = p.matcher(cleanedStatement);

        List<String> parameters = new ArrayList<>();

        while (m.find()) {
            parameters.add(m.group());
            count++;
        }

        if (json.getAttrs().size() != count)
            return responseQueryToJson.toBuilder()
                    .success(false)
                    .errorMessage(List.of("Non sono stati definiti tutti i parametri della query.getJson()"))
                    .build();

        for (Attribute attr : json.getAttrs()) {
            if (null
                    == parameters.stream()
                    .filter((param) -> param.equalsIgnoreCase(attr.getParameter().getName()))
                    .findFirst()
                    .orElse(null))
                return responseQueryToJson.toBuilder()
                        .success(false)
                        .errorMessage(List.of("Non sono stati definiti tutti i parametri della query"))
                        .build();
        }

        for (Attribute attr : json.getAttrs()) {
            if (attr.getParameter() == null
                    || attr.getOperator() == null
                    || attr.getAlias() == null
                    || attr.getParameter().getType() == null)
                return responseQueryToJson.toBuilder()
                        .success(false)
                        .errorMessage(
                                List.of(
                                        "Non è presente il parametro, l'operatore, l'alias o il tipo per "
                                                + attr.getAttrName()))
                        .build();
        }

        /** Controllo i constraint */
        if (null != json.getConstr())
            for (Constraint constr : json.getConstr()) {
                if (constr.getParameters() == null || constr.getConstrType() == null)
                    return responseQueryToJson.toBuilder()
                            .success(false)
                            .errorMessage(
                                    List.of(
                                            "Non è presente il/i parametri per il vincolo dell' attributo "
                                                    + constr.getAttrName()))
                            .build();

                switch (constr.getConstrType()) {
                    case IN_SIZE -> {
                        if (constr.getParameters().split(",").length != 1)
                            return responseQueryToJson.toBuilder()
                                    .success(false)
                                    .errorMessage(
                                            List.of(
                                                    "Per il vincoli di In può essere definito un solo parametro per "
                                                            + constr.getAttrName()))
                                    .build();
                        if (constr.getMaxInSize() == null)
                            return responseQueryToJson.toBuilder()
                                    .success(false)
                                    .errorMessage(
                                            List.of(
                                                    "Non è presente il valore per il vincolo In dell' attributo "
                                                            + constr.getAttrName()))
                                    .build();
                        String attributesIn = getParameterList(json, constr);
                        constr.setMessage(
                                "Numero massimo valori consentiti per "
                                        + attributesIn
                                        + " è di "
                                        + constr.getMaxInSize());
                    }
                    case TEMPORAL_INTERVAL -> {
                        if (constr.getParameters().split(",").length != 2)
                            return responseQueryToJson.toBuilder()
                                    .success(false)
                                    .errorMessage(
                                            List.of(
                                                    "Per un vincolo temporale devono essere dichiarati 2 parametri per l' attributo "
                                                            + constr.getAttrName()))
                                    .build();
                        if (constr.getMaxIntervalDays() == null
                                && constr.getMaxIntervalHours() == null
                                && constr.getMaxIntervalMin() == null
                                && constr.getMaxIntervalSec() == null)
                            return responseQueryToJson.toBuilder()
                                    .success(false)
                                    .errorMessage(
                                            List.of(
                                                    "Non è presente il valore per il vincolo Temporale dell' attributo "
                                                            + constr.getAttrName()))
                                    .build();
                        String attributesTempInt = getParameterList(json, constr);
                        constr.setMessage(
                                "Range massimo tra "
                                        + attributesTempInt
                                        + " è di giorni : "
                                        + constr.getMaxIntervalDays()
                                        + " , ore : "
                                        + constr.getMaxIntervalHours()
                                        + " , minuti : "
                                        + constr.getMaxIntervalMin()
                                        + " , secondi : "
                                        + constr.getMaxIntervalSec());
                    }
                    case NUMERIC_INTERVAL -> {
                        if (constr.getParameters().split(",").length != 2)
                            return responseQueryToJson.toBuilder()
                                    .success(false)
                                    .errorMessage(
                                            List.of(
                                                    "Per un vincolo numerico devono essere dichiarati 2 parametri per l' attributo "
                                                            + constr.getAttrName()))
                                    .build();
                        if (constr.getMaxIntervalNumber() == null)
                            return responseQueryToJson.toBuilder()
                                    .success(false)
                                    .errorMessage(
                                            List.of(
                                                    "Non è presente il valore per il vincolo numerico dell' attributo "
                                                            + constr.getAttrName()))
                                    .build();
                        String attributesTempNum = getParameterList(json, constr);
                        constr.setMessage(
                                "Range massimo tra "
                                        + attributesTempNum
                                        + " è di "
                                        + constr.getMaxIntervalNumber());
                    }
                    default -> {
                    }
                }
            }

        String finaleReplace = cleanedStatement;

        for (Attribute attr : json.getAttrs()) {
            if (null == attr.getParameter().getType())
                return responseQueryToJson.toBuilder()
                        .success(false)
                        .errorMessage(List.of("Deve essere dichiarato il tipo per " + attr.getAttrName()))
                        .build();

            switch (attr.getParameter().getType()) {
                case DATE -> finaleReplace =
                        finaleReplace.replace(
                                attr.getParameter().getName(),
                                "to_date('" + Constants.FAKE_DATE + "', 'YYYY/MM/DD HH24:MI:SS')");
                case DATE_TRUNC -> finaleReplace =
                        finaleReplace.replace(
                                attr.getParameter().getName(),
                                "to_date('" + Constants.FAKE_DATE_TRUNC + "', 'YYYY/MM/DD')");
                case NUMBER -> {
                    if (attr.getOperator().toUpperCase().equals(WhereConditionOperator.IN.getName())) {
                        finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "(9, 6)");

                        break;
                    }
                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "6");
                }
                case STRING -> {
                    if (attr.getOperator().toUpperCase().equals(WhereConditionOperator.IN.getName())) {
                        finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "('X', 'Z')");

                        break;
                    }
                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "'X'");
                }
                default -> {
                }
            }
        }

        /** Provo ad eseguire la query con valori fittizi quando la inserisco */
        if (execQuery) function.apply(finaleReplace, 1);

        return responseQueryToJson.toBuilder().entity(json).build();
    }

    private String getParameterList(QueryToJson query, Constraint constr) {
        StringBuilder attributes = new StringBuilder();

        for (String param : constr.getParameters().split(",")) {
            for (Attribute attr : query.getAttrs()) {

                if (param.equalsIgnoreCase(attr.getParameter().getName()))
                    attributes.append(attr.getAlias()).append(" - ");
            }
        }

        return attributes.substring(0, attributes.length() - 3);
    }

    /**
     * Metodo per l'esecuzione della query. Vengono controllati i valori dei parametri e dei vincoli e
     * sostituiti i parametri
     *
     * @param query
     * @return
     * @throws IOException
     */
    public WrappedResponse<String> getFinalQueryString(UteQueDto query) throws IOException {

        QueryToJson json = objectMapper.readValue(query.getJson(), QueryToJson.class);

        /** Controllo i valori */
        List<String> requireParamError = new ArrayList<>();

        for (Attribute attr : json.getAttrs()) {

            if (null == attr.getParameter().getValue() || attr.getParameter().getValue().isEmpty())
                requireParamError.add(
                        "Valore richiesto per " + attr.getAlias() + " nella query " + query.getNam());
        }

        if (requireParamError.size() > 0)
            return responseString.toBuilder().success(false).errorMessage(requireParamError).build();

        /** Controllo dei vincoli */
        for (Constraint constraint : json.getConstr()) {

            switch (constraint.getConstrType()) {
                case IN_SIZE -> {
                    for (Attribute attr : json.getAttrs()) {

                        if (attr.getParameter().getName().equalsIgnoreCase(constraint.getParameters())) {

                            String[] splittedValues = attr.getParameter().getValue().split(",");

                            if (splittedValues.length > constraint.getMaxInSize())
                                return responseString.toBuilder()
                                        .success(false)
                                        .errorMessage(List.of(constraint.getMessage()))
                                        .build();
                        }
                    }
                }
                case TEMPORAL_INTERVAL -> {
                    String[] intervalParams = constraint.getParameters().split(",");
                    Date fistParamValue = null;
                    Date secondParamValue = null;
                    String firstParam = intervalParams[0];
                    String secondParam = intervalParams[1];
                    for (Attribute attribute : json.getAttrs()) {

                        if (attribute.getParameter().getName().equalsIgnoreCase(firstParam)) {
                            try {

                                fistParamValue = new Date(Long.parseLong(attribute.getParameter().getValue()));
                            } catch (Exception e) {
                                return responseString.toBuilder()
                                        .success(false)
                                        .errorMessage(
                                                List.of("Il parametro " + attribute.getAlias() + " non è un data valida"))
                                        .build();
                            }
                        }

                        if (attribute.getParameter().getName().equalsIgnoreCase(secondParam)) {
                            try {

                                secondParamValue = new Date(Long.parseLong(attribute.getParameter().getValue()));
                            } catch (Exception e) {
                                return responseString.toBuilder()
                                        .success(false)
                                        .errorMessage(
                                                List.of("Il parametro " + attribute.getAlias() + " non è un data valida"))
                                        .build();
                            }
                        }
                    }

                    /** Ricostruisco il max inteval in millisecondi */
                    Long daysToMilliseconds =
                            constraint.getMaxIntervalDays() == null
                                    ? 0
                                    : (constraint.getMaxIntervalDays() * 24 * 60 * 60 * 1000);
                    Long hours =
                            constraint.getMaxIntervalHours() == null
                                    ? 0
                                    : (constraint.getMaxIntervalHours() * 60 * 60);
                    Long minutes =
                            constraint.getMaxIntervalMin() == null ? 0 : (constraint.getMaxIntervalMin() * 60);
                    Long seconds =
                            constraint.getMaxIntervalSec() == null ? 0 : (constraint.getMaxIntervalSec() * 1000);
                    long maxInterval = daysToMilliseconds + hours + minutes + seconds;

                    /** Verifico la differenza tra le due date */
                    if ((secondParamValue.getTime() - fistParamValue.getTime()) > maxInterval)
                        return responseString.toBuilder()
                                .success(false)
                                .errorMessage(List.of(constraint.getMessage()))
                                .build();
                }
                default -> {
                }
            }
        }

        /** Sostituzione parametri */
        String finaleReplace = WrappingUtils.cleanQueryString(json.getStatement());

        for (Attribute attr : json.getAttrs()) {
            switch (attr.getParameter().getType()) {
                case DATE -> finaleReplace =
                        finaleReplace.replace(
                                attr.getParameter().getName(),
                                "to_date('"
                                        + DateUtils.getStringFromDate(
                                        new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS),
                                        Long.parseLong(attr.getParameter().getValue()))
                                        + "', 'YYYY/MM/DD HH24:MI:SS')");
                case DATE_TRUNC -> finaleReplace =
                        finaleReplace.replace(
                                attr.getParameter().getName(),
                                "to_date('"
                                        + DateUtils.getStringFromDate(
                                        new SimpleDateFormat(YYYY_MM_DD),
                                        Long.parseLong(attr.getParameter().getValue()))
                                        + "', 'YYYY/MM/DD')");
                case NUMBER -> {
                    if (attr.getOperator()
                            .toUpperCase()
                            .equalsIgnoreCase(WhereConditionOperator.IN.getName())) {
                        StringBuilder finalString = new StringBuilder();
                        String[] split =
                                WrappingUtils.cleanQueryString(attr.getParameter().getValue()).split(",");

                        for (int i = 0; i < split.length; i++) {

                            if (!StringUtils.isNumeric(split[i]))
                                return responseString.toBuilder()
                                        .success(false)
                                        .errorMessage(
                                                List.of("Il parametro " + attr.getAlias() + " non è un numero corretto"))
                                        .build();

                            finalString.append(split[i]).append((i == split.length - 1) ? "" : ",");
                        }

                        finaleReplace =
                                finaleReplace.replace(attr.getParameter().getName(), "(" + finalString + ")");

                        break;
                    }
                    if (!StringUtils.isNumeric(attr.getParameter().getValue()))
                        return responseString.toBuilder()
                                .success(false)
                                .errorMessage(
                                        List.of("Il parametro " + attr.getAlias() + " non è un numero corretto"))
                                .build();
                    finaleReplace =
                            finaleReplace.replace(attr.getParameter().getName(), attr.getParameter().getValue());
                }
                case STRING -> {
                    if (attr.getOperator()
                            .toUpperCase()
                            .equalsIgnoreCase(WhereConditionOperator.IN.getName())) {
                        StringBuilder finalString = new StringBuilder();
                        String[] split =
                                WrappingUtils.cleanQueryString(attr.getParameter().getValue()).split(",");

                        for (int i = 0; i < split.length; i++) {

                            finalString
                                    .append("'")
                                    .append(split[i])
                                    .append("'")
                                    .append((i == split.length - 1) ? "" : ",");
                        }

                        finaleReplace =
                                finaleReplace.replace(attr.getParameter().getName(), "(" + finalString + ")");

                        break;
                    }
                    finaleReplace =
                            finaleReplace.replace(
                                    attr.getParameter().getName(), "'" + attr.getParameter().getValue() + "'");
                }
                default -> {
                }
            }
        }

        return responseString.toBuilder().entity(finaleReplace).build();
    }

    /**
     * Metodo per creare un lista di array nel caso ci sia solo una colonna nel result set perchè in
     * tal caso, non so per quale motivo, viene creata una lista di singoli oggetti invece che di
     * array come avviene per result set con result set > 1 colonna
     *
     * @param query
     * @param resultSet
     * @return
     */
    public List<Object> setResultSet(QueryToJson query, List<Object> resultSet) {

        if (query.getQuerySelectColumns().size() == 1) {

            List<Object> listToReturn = new ArrayList<>();

            resultSet.forEach(r -> listToReturn.add(new Object[]{r}));

            return listToReturn;
        }

        return resultSet;
    }
}

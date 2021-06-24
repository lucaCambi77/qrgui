package it.cambi.qrgui.services.oracle.taskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.enums.Schema;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.Attribute;
import it.cambi.qrgui.query.model.Constraint;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.util.DateUtils;
import it.cambi.qrgui.util.WhereConditionOperator;
import it.cambi.qrgui.util.WrappingUtils;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static it.cambi.qrgui.util.IConstants.YYYY_MM_DD;
import static it.cambi.qrgui.util.IConstants.YYYY_MM_DD_HH_MI_SS;

/**
 * Classe che implementa Callable e permette di eseguire un task di esecuzione di una query. N.B. Poichè è un task asincrono, nel contesto spring è
 * necessario utilizzare {@link EntityManagerFactory} per poi ricavare l'entityManager. Altrimenti non si può accedere in maniera sistematica alla
 * persistence Unit
 *
 * @author luca
 */
@Component
@Scope("prototype")
public class ExecuteQueryTask implements Callable<XWrappedResponse<Temi15UteQue, List<Object>>> {

    @PersistenceUnit(unitName = "firstTransactionManager")
    private EntityManagerFactory factory;

    private Temi15UteQue query;
    private Integer pageSize;
    private Integer page;

    public ExecuteQueryTask(Temi15UteQue query,
                            Integer pageSize,
                            Integer page) {
        this.query = query;
        this.pageSize = pageSize;
        this.page = page;
    }

    @Override
    public XWrappedResponse<Temi15UteQue, List<Object>> call() throws Exception {

        XWrappedResponse<Temi15UteQue, List<Object>> response = new XWrappedResponse<Temi15UteQue, List<Object>>();

        String finalQuery;

        WrappedResponse<String> queryStringResponse = getFinalQueryString(query, page, pageSize);

        if (!queryStringResponse.isSuccess())
            return response.setSuccess(false).setErrorMessages(queryStringResponse.getErrorMessage());

        finalQuery = getFinalQueryString(query, page, pageSize).getEntity();

        EntityManager entityManager = null;

        switch (Schema.valueOf(query.getTemi13DtbInf().getId().getSch())) {

            case TEST:

                entityManager = factory.createEntityManager();

                break;

            default:
                break;
        }

        List<Object> resultSet;

        Long count = 0L;

        QueryToJson json = new ObjectMapper().readValue(query.getJson(), QueryToJson.class);

        if (json.getQueryType() == QueryType.COUNT) {

            count = executeQueryCount(finalQuery, entityManager);
            response.setCount(count.intValue());

        } else {
            resultSet = getByNativeQuery(finalQuery, page, entityManager);
            resultSet = setOneColumnResultSet(json, resultSet);
            response.setEntity(resultSet);
        }

        response.setXentity(query);

        return response;
    }

    public Temi15UteQue getQuery() {
        return query;
    }

    public ExecuteQueryTask setQuery(Temi15UteQue query) {
        this.query = query;
        return this;

    }

    public Integer getPageSize() {
        return pageSize;
    }

    public ExecuteQueryTask setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;

    }

    public Integer getPage() {
        return page;
    }

    public ExecuteQueryTask setPage(Integer page) {
        this.page = page;
        return this;

    }

    @SuppressWarnings("unchecked")
    public List<Object> getByNativeQuery(String nativeQuery, Integer page, EntityManager entityManager) {

        return Optional.ofNullable(page).map(p -> entityManager.createNativeQuery(nativeQuery)
                .setMaxResults(getPageSize())
                .setFirstResult((p - 1) * getPageSize())
                .getResultList()).orElse(
                entityManager.createNativeQuery(nativeQuery)
                        .getResultList());
    }

    /**
     * Metodo per il conteggio dei record della query richiesta
     *
     * @param entityManager
     * @param queryAttribute
     * @param rootTable
     * @return
     * @throws ClassNotFoundException
     */
    public Long executeQueryCount(String sqlQuery, EntityManager entityManager) {
        Query nativeQuery = entityManager.createNativeQuery(
                "select count(*) from (" +
                        sqlQuery +
                        ") x");
        return ((Number) nativeQuery.getSingleResult()).longValue();
    }

    /**
     * Metodo per l'esecuzione della query. Vengono controllati i valori dei parametri e dei vincoli e sostituiti i parametri
     *
     * @param query
     * @param page
     * @param pageSize
     * @return
     * @throws IOException
     */
    @SuppressWarnings("serial")
    public WrappedResponse<String> getFinalQueryString(Temi15UteQue query, Integer page, Integer pageSize) throws IOException {
        setPageSize(pageSize == null ? getPageSize() : pageSize);

        WrappedResponse<String> wrappedResponse = new WrappedResponse<String>();

        // if (!checkQuery(query, false).isSuccess())
        // return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
        // {
        // {
        // add("La query non è stata definita nel formato corretto. Si prega di verificare");
        // }
        // });

        QueryToJson json = new ObjectMapper().readValue(query.getJson(), QueryToJson.class);

        /**
         * Controllo i valori
         */

        List<String> requireParamError = new ArrayList<>();

        for (Attribute attr : json.getAttrs()) {

            if (null == attr.getParameter().getValue() || attr.getParameter().getValue().isEmpty())
                requireParamError.add("Valore richiesto per " + attr.getAlias() + " nella query " + query.getNam());
        }

        if (requireParamError.size() > 0)
            return wrappedResponse.setSuccess(false).setErrorMessages(requireParamError);
        /**
         * Controllo dei vincoli
         *
         */

        for (Constraint constraint : json.getConstr()) {

            switch (constraint.getConstrType()) {
                case IN_SIZE:

                    for (Attribute attr : json.getAttrs()) {

                        if (attr.getParameter().getName().equalsIgnoreCase(constraint.getParameters())) {

                            String[] splittedValues = attr.getParameter().getValue().split(",");

                            if (splittedValues.length > constraint.getMaxInSize())
                                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>() {
                                    {
                                        add(constraint.getMessage());
                                    }
                                });
                        }
                    }

                    break;

                case TEMPORAL_INTERVAL:

                    String[] intervalParams = constraint.getParameters().split(",");

                    Date fistParamValue = null;
                    Date secondParamValue = null;

                    String firstParam = intervalParams[0];
                    String secondParam = intervalParams[1];

                    for (Attribute attribute : json.getAttrs()) {

                        if (attribute.getParameter().getName().equalsIgnoreCase(firstParam)) {
                            try {

                                fistParamValue = new Date(new Long(attribute.getParameter().getValue()));
                            } catch (Exception e) {
                                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>() {
                                    {
                                        add("Il parametro " + attribute.getAlias() + " non è un data valida");
                                    }
                                });
                            }
                        }

                        if (attribute.getParameter().getName().equalsIgnoreCase(secondParam)) {
                            try {

                                secondParamValue = new Date(new Long(attribute.getParameter().getValue()));
                            } catch (Exception e) {
                                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>() {
                                    {
                                        add("Il parametro " + attribute.getAlias() + " non è un data valida");
                                    }
                                });
                            }
                        }

                    }

                    /**
                     * Ricostruisco il max inteval in millisecondi
                     */
                    Long daysToMilliseconds = constraint.getMaxIntervalDays() == null ? 0
                            : (constraint.getMaxIntervalDays() * 24 * 60 * 60 * 1000);

                    Long hours = constraint.getMaxIntervalHours() == null ? 0 : (constraint.getMaxIntervalHours() * 60 * 60);
                    Long minutes = constraint.getMaxIntervalMin() == null ? 0 : (constraint.getMaxIntervalMin() * 60);
                    Long seconds = constraint.getMaxIntervalSec() == null ? 0 : (constraint.getMaxIntervalSec() * 1000);

                    Long maxInterval = daysToMilliseconds + hours + minutes + seconds;

                    /**
                     * Verifico la differenza tra le due date
                     */
                    if ((secondParamValue.getTime() - fistParamValue.getTime()) > maxInterval)
                        return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>() {
                            {
                                add(constraint.getMessage());
                            }
                        });

                    break;

                case NUMERIC_INTERVAL:

                    break;

                default:
                    break;
            }

        }

        /**
         * Sostituzione parametri
         */

        String finaleReplace = WrappingUtils.cleanQueryString(json.getStatement());

        for (Attribute attr : json.getAttrs()) {
            switch (attr.getParameter().getType()) {
                case DATE:

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "to_date('"
                                    + DateUtils.getStringFromDate(new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS),
                                    new Long(attr.getParameter().getValue()))
                                    + "', 'YYYY/MM/DD HH24:MI:SS')");
                    break;

                case DATE_TRUNC:

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "to_date('" + DateUtils.getStringFromDate(new SimpleDateFormat(YYYY_MM_DD),
                                    new Long(attr.getParameter().getValue())) + "', 'YYYY/MM/DD')");

                    break;

                case NUMBER:

                    if (attr.getOperator().toUpperCase().equalsIgnoreCase(WhereConditionOperator.IN.getName())) {
                        StringBuilder finalString = new StringBuilder();
                        String[] split = WrappingUtils.cleanQueryString(attr.getParameter().getValue()).split(",");

                        for (int i = 0; i < split.length; i++) {

                            if (!StringUtils.isNumeric(split[i]))
                                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>() {
                                    {
                                        add("Il parametro " + attr.getAlias() + " non è un numero corretto");
                                    }
                                });

                            finalString.append(split[i]).append((i == split.length - 1) ? "" : ",");
                        }

                        finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                                "(" + finalString + ")");

                        break;
                    }

                    if (!StringUtils.isNumeric(attr.getParameter().getValue()))
                        return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>() {
                            {
                                add("Il parametro " + attr.getAlias() + " non è un numero corretto");
                            }
                        });

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            attr.getParameter().getValue());
                    break;

                case STRING:

                    if (attr.getOperator().toUpperCase().equalsIgnoreCase(WhereConditionOperator.IN.getName())) {
                        StringBuilder finalString = new StringBuilder();
                        String[] split = WrappingUtils.cleanQueryString(attr.getParameter().getValue()).split(",");

                        for (int i = 0; i < split.length; i++) {

                            finalString.append("'").append(split[i]).append("'").append((i == split.length - 1) ? "" : ",");
                        }

                        finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                                "(" + finalString + ")");

                        break;
                    }

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "'" + attr.getParameter().getValue() + "'");
                    break;

                default:
                    break;
            }
        }

        return wrappedResponse.setEntity(finaleReplace);
    }

    /**
     * Metodo per creare un lista di array nel caso ci sia solo una colonna nel result set perchè in tal caso, non so per quale motivo, viene creata
     * una lista di singoli oggetti invece che di array come avviene per result set con result set > 1 colonna
     *
     * @param query
     * @param resultSet
     * @return
     */
    @SuppressWarnings("serial")
    private List<Object> setOneColumnResultSet(QueryToJson query, List<Object> resultSet) {
        if (query.getQuerySelectColumns().size() == 1) {

            List<Object> listToReturn = new ArrayList<Object>();

            for (Object object : resultSet) {

                listToReturn.add(new ArrayList<Object>() {
                    {
                        add(object);
                    }
                });
            }

            resultSet = listToReturn;
        }

        return resultSet;
    }

}

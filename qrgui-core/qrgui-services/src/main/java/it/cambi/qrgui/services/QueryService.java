package it.cambi.qrgui.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.dao.AbstractDao;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.Attribute;
import it.cambi.qrgui.query.model.Constraint;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.query.model.SelectColumns;
import it.cambi.qrgui.util.DateUtils;
import it.cambi.qrgui.util.IConstants;
import it.cambi.qrgui.util.WhereConditionOperator;
import it.cambi.qrgui.util.WrappingUtils;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.cambi.qrgui.util.IConstants.YYYY_MM_DD;
import static it.cambi.qrgui.util.IConstants.YYYY_MM_DD_HH_MI_SS;

@Service
@Setter
public class QueryService {

  /**
   * Metodo per controllare l'oggetto query in fase di associazione della stessa ad una categoria,
   * in particolare il {@link #QueryToJson} , nel quale al suo interno sono presenti tutti le varie
   * sezioni del form della query
   *
   * @param query
   * @return
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  @SuppressWarnings("serial")
  public <T extends AbstractDao> WrappedResponse<QueryToJson> checkQuery(
      Temi15UteQue query, boolean execQuery, T dao) throws JsonMappingException, IOException {

    QueryToJson json = new ObjectMapper().readValue(query.getJson(), QueryToJson.class);

    WrappedResponse<QueryToJson> wrappedResponse = WrappedResponse.<QueryToJson>baseBuilder().build();

    if (null == json.getStatement())
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add("Statement della query non presente");
                }
              });

    if (json.getStatement().toLowerCase().contains("insert")
        || json.getStatement().toLowerCase().contains("drop")
        || json.getStatement().toLowerCase().contains("update")
        || json.getStatement().toLowerCase().contains("create")
        || json.getStatement().toLowerCase().contains("delete"))
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add("E' consentito solo l'uso di Select");
                }
              });

    int indexSelectAll = json.getStatement().toLowerCase().indexOf("select *");
    int indexSelect = json.getStatement().toLowerCase().indexOf("select");

    if (indexSelectAll - indexSelect == 0 && (indexSelectAll != -1 && indexSelect != -1))
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add(
                      "Non è possibile eseguire select *, è necessario indicare il nome degli attributi, meglio se con un alias per renderli più espliciti nel risultato");
                }
              });

    if (json.getStatement().toLowerCase().contains("--")
        || json.getStatement().toLowerCase().contains("/*"))
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add(
                      "Non è consentito l'uso dei commenti in quanto potrebbero alterare la formattazione della query");
                }
              });

    if (null == query.getNam())
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add("E' necessario indicare un nome per la query");
                }
              });

    if (null == json.getQuerySelectColumns() || json.getQuerySelectColumns().size() == 0)
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add("E' necessario indicare almeno una colonna di output ");
                }
              });

    /** Controllo tipo e alias delle colonne */
    int j = 1;

    for (SelectColumns column : json.getQuerySelectColumns()) {
      if (null == column.getAs() || null == column.getType()) {
        String message = "Tipo od alias non definito per colonna " + j;
        return wrappedResponse
            .setSuccess(false)
            .setErrorMessages(
                new ArrayList<String>() {
                  {
                    add(message);
                  }
                });
      }
      j++;
    }

    /** Pulisco la stringa della query da eventuali ritorni a capo, punto e virgola */
    String cleanedStatement = WrappingUtils.cleanQueryString(json.getStatement());

    /** Controllo che ci siano tutti i parametri */
    int count = 0;
    Pattern p = Pattern.compile("&\\w+"); // the pattern to search for
    Matcher m = p.matcher(cleanedStatement);

    List<String> parameters = new ArrayList<String>();

    while (m.find()) {
      parameters.add(m.group());
      count++;
    }

    if (json.getAttrs().size() != count)
      return wrappedResponse
          .setSuccess(false)
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add("Non sono stati definiti tutti i parametri della query.getJson()");
                }
              });

    for (Attribute attr : json.getAttrs()) {
      if (null
          == parameters.stream()
              .filter((param) -> param.equalsIgnoreCase(attr.getParameter().getName()))
              .findFirst()
              .orElse(null))
        return wrappedResponse
            .setSuccess(false)
            .setErrorMessages(
                new ArrayList<String>() {
                  {
                    add("Non sono stati definiti tutti i parametri della query");
                  }
                });
    }

    for (Attribute attr : json.getAttrs()) {
      if (attr.getParameter() == null
          || attr.getOperator() == null
          || attr.getAlias() == null
          || attr.getParameter().getType() == null)
        return wrappedResponse
            .setSuccess(false)
            .setErrorMessages(
                new ArrayList<String>() {
                  {
                    add(
                        "Non è presente il parametro, l'operatore, l'alias o il tipo per "
                            + attr.getAttrName());
                  }
                });
    }

    /** Controllo i constraint */
    if (null != json.getConstr())
      for (Constraint constr : json.getConstr()) {
        if (constr.getParameters() == null || constr.getConstrType() == null)
          return wrappedResponse
              .setSuccess(false)
              .setErrorMessages(
                  new ArrayList<String>() {
                    {
                      add(
                          "Non è presente il/i parametri per il vincolo dell' attributo "
                              + constr.getAttrName());
                    }
                  });

        switch (constr.getConstrType()) {
          case IN_SIZE:
            if (constr.getParameters().split(",").length != 1)
              return wrappedResponse
                  .setSuccess(false)
                  .setErrorMessages(
                      new ArrayList<String>() {
                        {
                          add(
                              "Per il vincoli di In può essere definito un solo parametro per "
                                  + constr.getAttrName());
                        }
                      });

            if (constr.getMaxInSize() == null)
              return wrappedResponse
                  .setSuccess(false)
                  .setErrorMessages(
                      new ArrayList<String>() {
                        {
                          add(
                              "Non è presente il valore per il vincolo In dell' attributo "
                                  + constr.getAttrName());
                        }
                      });

            String attributesIn = getParameterList(json, constr);

            constr.setMessage(
                "Numero massimo valori consentiti per "
                    + attributesIn
                    + " è di "
                    + constr.getMaxInSize());

            break;

          case TEMPORAL_INTERVAL:
            if (constr.getParameters().split(",").length != 2)
              return wrappedResponse
                  .setSuccess(false)
                  .setErrorMessages(
                      new ArrayList<String>() {
                        {
                          add(
                              "Per un vincolo temporale devono essere dichiarati 2 parametri per l' attributo "
                                  + constr.getAttrName());
                        }
                      });

            if (constr.getMaxIntervalDays() == null
                && constr.getMaxIntervalHours() == null
                && constr.getMaxIntervalMin() == null
                && constr.getMaxIntervalSec() == null)
              return wrappedResponse
                  .setSuccess(false)
                  .setErrorMessages(
                      new ArrayList<String>() {
                        {
                          add(
                              "Non è presente il valore per il vincolo Temporale dell' attributo "
                                  + constr.getAttrName());
                        }
                      });

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

            break;

          case NUMERIC_INTERVAL:
            if (constr.getParameters().split(",").length != 2)
              return wrappedResponse
                  .setSuccess(false)
                  .setErrorMessages(
                      new ArrayList<String>() {
                        {
                          add(
                              "Per un vincolo numerico devono essere dichiarati 2 parametri per l' attributo "
                                  + constr.getAttrName());
                        }
                      });

            if (constr.getMaxIntervalNumber() == null)
              return wrappedResponse
                  .setSuccess(false)
                  .setErrorMessages(
                      new ArrayList<String>() {
                        {
                          add(
                              "Non è presente il valore per il vincolo numerico dell' attributo "
                                  + constr.getAttrName());
                        }
                      });

            String attributesTempNum = getParameterList(json, constr);

            constr.setMessage(
                "Range massimo tra "
                    + attributesTempNum
                    + " è di "
                    + constr.getMaxIntervalNumber());

            break;
          default:
            break;
        }
      }

    String finaleReplace = cleanedStatement;

    for (Attribute attr : json.getAttrs()) {
      if (null == attr.getParameter().getType())
        return wrappedResponse
            .setSuccess(false)
            .setErrorMessages(
                new ArrayList<String>() {
                  {
                    add("Deve essere dichiarato il tipo per " + attr.getAttrName());
                  }
                });

      switch (attr.getParameter().getType()) {
        case DATE:
          finaleReplace =
              finaleReplace.replace(
                  attr.getParameter().getName(),
                  "to_date('" + IConstants.FAKE_DATE + "', 'YYYY/MM/DD HH24:MI:SS')");
          break;

        case DATE_TRUNC:
          finaleReplace =
              finaleReplace.replace(
                  attr.getParameter().getName(),
                  "to_date('" + IConstants.FAKE_DATE_TRUNC + "', 'YYYY/MM/DD')");

          break;

        case NUMBER:
          if (attr.getOperator().toUpperCase() == WhereConditionOperator.IN.getName()) {
            finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "(9, 6)");

            break;
          }

          finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "6");
          break;

        case STRING:
          if (attr.getOperator().toUpperCase() == WhereConditionOperator.IN.getName()) {
            finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "('X', 'Z')");

            break;
          }

          finaleReplace = finaleReplace.replace(attr.getParameter().getName(), "'X'");
          break;

        default:
          break;
      }
    }

    /** Provo ad eseguire la query con valori fittizi quando la inserisco */
    if (execQuery) dao.getByNativeQuery(finaleReplace, 1);

    return wrappedResponse.setEntity(json).setResponse();
  }

  private String getParameterList(QueryToJson query, Constraint constr) {
    String attributes = "";

    for (String param : constr.getParameters().split(",")) {
      for (Attribute attr : query.getAttrs()) {

        if (param.equalsIgnoreCase(attr.getParameter().getName()))
          attributes += attr.getAlias() + " - ";
      }
    }

    return attributes.substring(0, attributes.length() - 3);
  }

  /**
   * Metodo per l'esecuzione della query. Vengono controllati i valori dei parametri e dei vincoli e
   * sostituiti i parametri
   *
   * @param query
   * @param pageSize
   * @return
   * @throws IOException
   */
  @SuppressWarnings("serial")
  public WrappedResponse<String> getFinalQueryString(Temi15UteQue query) throws IOException {

    WrappedResponse<String> wrappedResponse = WrappedResponse.<String>baseBuilder().build();

    // if (!checkQuery(query, false).isSuccess())
    // return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
    // {
    // {
    // add("La query non è stata definita nel formato corretto. Si prega di verificare");
    // }
    // });

    QueryToJson json = new ObjectMapper().readValue(query.getJson(), QueryToJson.class);

    /** Controllo i valori */
    List<String> requireParamError = new ArrayList<>();

    for (Attribute attr : json.getAttrs()) {

      if (null == attr.getParameter().getValue() || attr.getParameter().getValue().isEmpty())
        requireParamError.add(
            "Valore richiesto per " + attr.getAlias() + " nella query " + query.getNam());
    }

    if (requireParamError.size() > 0)
      return wrappedResponse.setSuccess(false).setErrorMessages(requireParamError);
    /** Controllo dei vincoli */
    for (Constraint constraint : json.getConstr()) {

      switch (constraint.getConstrType()) {
        case IN_SIZE:
          for (Attribute attr : json.getAttrs()) {

            if (attr.getParameter().getName().equalsIgnoreCase(constraint.getParameters())) {

              String[] splittedValues = attr.getParameter().getValue().split(",");

              if (splittedValues.length > constraint.getMaxInSize())
                return wrappedResponse
                    .setSuccess(false)
                    .setErrorMessages(
                        new ArrayList<String>() {
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

                fistParamValue = new Date(Long.parseLong(attribute.getParameter().getValue()));
              } catch (Exception e) {
                return wrappedResponse
                    .setSuccess(false)
                    .setErrorMessages(
                        new ArrayList<String>() {
                          {
                            add("Il parametro " + attribute.getAlias() + " non è un data valida");
                          }
                        });
              }
            }

            if (attribute.getParameter().getName().equalsIgnoreCase(secondParam)) {
              try {

                secondParamValue = new Date(Long.parseLong(attribute.getParameter().getValue()));
              } catch (Exception e) {
                return wrappedResponse
                    .setSuccess(false)
                    .setErrorMessages(
                        new ArrayList<String>() {
                          {
                            add("Il parametro " + attribute.getAlias() + " non è un data valida");
                          }
                        });
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
            return wrappedResponse
                .setSuccess(false)
                .setErrorMessages(
                    new ArrayList<String>() {
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

    /** Sostituzione parametri */
    String finaleReplace = WrappingUtils.cleanQueryString(json.getStatement());

    for (Attribute attr : json.getAttrs()) {
      switch (attr.getParameter().getType()) {
        case DATE:
          finaleReplace =
              finaleReplace.replace(
                  attr.getParameter().getName(),
                  "to_date('"
                      + DateUtils.getStringFromDate(
                          new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS),
                          new Long(attr.getParameter().getValue()))
                      + "', 'YYYY/MM/DD HH24:MI:SS')");
          break;

        case DATE_TRUNC:
          finaleReplace =
              finaleReplace.replace(
                  attr.getParameter().getName(),
                  "to_date('"
                      + DateUtils.getStringFromDate(
                          new SimpleDateFormat(YYYY_MM_DD),
                          new Long(attr.getParameter().getValue()))
                      + "', 'YYYY/MM/DD')");

          break;

        case NUMBER:
          if (attr.getOperator()
              .toUpperCase()
              .equalsIgnoreCase(WhereConditionOperator.IN.getName())) {
            StringBuilder finalString = new StringBuilder();
            String[] split =
                WrappingUtils.cleanQueryString(attr.getParameter().getValue()).split(",");

            for (int i = 0; i < split.length; i++) {

              if (!StringUtils.isNumeric(split[i]))
                return wrappedResponse
                    .setSuccess(false)
                    .setErrorMessages(
                        new ArrayList<String>() {
                          {
                            add("Il parametro " + attr.getAlias() + " non è un numero corretto");
                          }
                        });

              finalString.append(split[i]).append((i == split.length - 1) ? "" : ",");
            }

            finaleReplace =
                finaleReplace.replace(attr.getParameter().getName(), "(" + finalString + ")");

            break;
          }

          if (!StringUtils.isNumeric(attr.getParameter().getValue()))
            return wrappedResponse
                .setSuccess(false)
                .setErrorMessages(
                    new ArrayList<String>() {
                      {
                        add("Il parametro " + attr.getAlias() + " non è un numero corretto");
                      }
                    });

          finaleReplace =
              finaleReplace.replace(attr.getParameter().getName(), attr.getParameter().getValue());
          break;

        case STRING:
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
          break;

        default:
          break;
      }
    }

    return wrappedResponse.setEntity(finaleReplace);
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
  @SuppressWarnings("serial")
  public List<Object> setOneColumnResultSet(QueryToJson query, List<Object> resultSet) {
    if (query.getQuerySelectColumns().size() == 1) {

      List<Object> listToReturn = new ArrayList<Object>();

      for (Object object : resultSet) {

        listToReturn.add(
            new ArrayList<Object>() {
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

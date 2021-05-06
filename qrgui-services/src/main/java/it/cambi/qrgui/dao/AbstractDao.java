package it.cambi.qrgui.dao;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.query.model.Attribute;
import it.cambi.qrgui.query.model.Constraint;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.query.model.SelectColumns;
import it.cambi.qrgui.services.db.model.Temi15UteQue;
import it.cambi.qrgui.util.IConstants;
import it.cambi.qrgui.util.WhereConditionOperator;
import it.cambi.qrgui.util.WrappingUtils;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 */
public abstract class AbstractDao
{
    private static final Logger log = LoggerFactory.getLogger(AbstractDao.class);

    private int pageSize = 15;

    public abstract EntityManager getEntityManager();

    public CriteriaBuilder getCriteriaBuilder()
    {
        return getEntityManager().getCriteriaBuilder();
    }

    public int getPageSize()
    {
        return pageSize;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getByNativeQuery(String nativeQuery, Integer page)
    {

        if (null == page)
            return getEntityManager().createNativeQuery(nativeQuery)
                    .getResultList();

        return getEntityManager().createNativeQuery(nativeQuery)
                .setMaxResults(getPageSize())
                .setFirstResult((page - 1) * getPageSize())
                .getResultList();

    }

    public Long getSequence(String sequenceName)
    {

        log.info("Creo la sequence " + sequenceName);
        Query q = getEntityManager().createNativeQuery(sequenceName);

        Long sequence = ((BigDecimal) q.getSingleResult()).longValue();

        return sequence;
    }

    /**
     * Metodo per controllare l'oggetto query in fase di associazione della stessa ad una categoria, in particolare il {@link #QueryToJson} , nel
     * quale al suo interno sono presenti tutti le varie sezioni del form della query
     * 
     * @param query
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @SuppressWarnings("serial")
    public WrappedResponse<QueryToJson> checkQuery(Temi15UteQue query, boolean execQuery) throws JsonParseException, JsonMappingException, IOException
    {

        QueryToJson json = new ObjectMapper().readValue(query.getJson(), QueryToJson.class);

        WrappedResponse<QueryToJson> wrappedResponse = new WrappedResponse<QueryToJson>();

        if (null == json.getStatement())
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("Statement della query non presente");
                }
            });

        if (json.getStatement().toLowerCase().indexOf("insert") >= 0 || json.getStatement().toLowerCase().indexOf("drop") >= 0
                || json.getStatement().toLowerCase().indexOf("update") >= 0
                || json.getStatement().toLowerCase().indexOf("create") >= 0
                || json.getStatement().toLowerCase().indexOf("delete") >= 0)
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("E' consentito solo l'uso di Select");
                }
            });

        int indexSelectAll = json.getStatement().toLowerCase().indexOf("select *");
        int indexSelect = json.getStatement().toLowerCase().indexOf("select");

        if (indexSelectAll - indexSelect == 0 && (indexSelectAll != -1 && indexSelect != -1))
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("Non è possibile eseguire select *, è necessario indicare il nome degli attributi, meglio se con un alias per renderli più espliciti nel risultato");
                }
            });

        if (json.getStatement().toLowerCase().indexOf("--") >= 0 || json.getStatement().toLowerCase().indexOf("/*") >= 0)
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("Non è consentito l'uso dei commenti in quanto potrebbero alterare la formattazione della query");
                }
            });

        if (null == query.getNam())
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("E' necessario indicare un nome per la query");
                }
            });

        if (null == json.getQuerySelectColumns() || json.getQuerySelectColumns().size() == 0)
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("E' necessario indicare almeno una colonna di output ");
                }
            });

        /**
         * Controllo tipo e alias delle colonne
         */
        int j = 1;

        for (SelectColumns column : json.getQuerySelectColumns())
        {
            if (null == column.getAs() || null == column.getType())
            {
                String message = "Tipo od alias non definito per colonna " + j;
                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                {
                    {
                        add(message);
                    }
                });
            }
            j++;
        }

        /**
         * Pulisco la stringa della query da eventuali ritorni a capo, punto e virgola
         */
        String cleanedStatement = WrappingUtils.cleanQueryString(json.getStatement());

        /**
         * Controllo che ci siano tutti i parametri
         */
        int count = 0;
        Pattern p = Pattern.compile("&\\w+"); // the pattern to search for
        Matcher m = p.matcher(cleanedStatement);

        List<String> parameters = new ArrayList<String>();

        while (m.find())
        {
            parameters.add(m.group());
            count++;
        }

        if (json.getAttrs().size() != count)
            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
            {
                {
                    add("Non sono stati definiti tutti i parametri della query.getJson()");
                }
            });

        for (Attribute attr : json.getAttrs())
        {
            if (null == parameters.stream().filter((param) -> param.equalsIgnoreCase(attr.getParameter().getName())).findFirst().orElse(null))
                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                {
                    {
                        add("Non sono stati definiti tutti i parametri della query");
                    }
                });
        }

        for (Attribute attr : json.getAttrs())
        {
            if (attr.getParameter() == null || attr.getOperator() == null || attr.getAlias() == null || attr.getParameter().getType() == null)
                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                {
                    {
                        add("Non è presente il parametro, l'operatore, l'alias o il tipo per " + attr.getAttrName());
                    }
                });
        }

        /**
         * Controllo i constraint
         */
        if (null != json.getConstr())
            for (Constraint constr : json.getConstr())
            {
                if (constr.getParameters() == null || constr.getConstrType() == null)
                    return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                    {
                        {
                            add("Non è presente il/i parametri per il vincolo dell' attributo " + constr.getAttrName());
                        }
                    });

                switch (constr.getConstrType())
                {
                    case IN_SIZE:

                        if (constr.getParameters().split(",").length != 1)
                            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                            {
                                {
                                    add("Per il vincoli di In può essere definito un solo parametro per " + constr.getAttrName());
                                }
                            });

                        if (constr.getMaxInSize() == null)
                            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                            {
                                {
                                    add("Non è presente il valore per il vincolo In dell' attributo " + constr.getAttrName());
                                }
                            });

                        String attributesIn = getParameterList(json, constr);

                        constr.setMessage("Numero massimo valori consentiti per " + attributesIn + " è di " + constr.getMaxInSize());

                        break;

                    case TEMPORAL_INTERVAL:

                        if (constr.getParameters().split(",").length != 2)
                            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                            {
                                {
                                    add("Per un vincolo temporale devono essere dichiarati 2 parametri per l' attributo " + constr.getAttrName());
                                }
                            });

                        if (constr.getMaxIntervalDays() == null && constr.getMaxIntervalHours() == null && constr.getMaxIntervalMin() == null
                                && constr.getMaxIntervalSec() == null)
                            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                            {
                                {
                                    add("Non è presente il valore per il vincolo Temporale dell' attributo " + constr.getAttrName());
                                }
                            });

                        String attributesTempInt = getParameterList(json, constr);

                        constr.setMessage("Range massimo tra " + attributesTempInt + " è di giorni : " + constr.getMaxIntervalDays()
                                + " , ore : " + constr.getMaxIntervalHours() + " , minuti : " + constr.getMaxIntervalMin() + " , secondi : "
                                + constr.getMaxIntervalSec());

                        break;

                    case NUMERIC_INTERVAL:

                        if (constr.getParameters().split(",").length != 2)
                            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                            {
                                {
                                    add("Per un vincolo numerico devono essere dichiarati 2 parametri per l' attributo " + constr.getAttrName());
                                }
                            });

                        if (constr.getMaxIntervalNumber() == null)
                            return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                            {
                                {
                                    add("Non è presente il valore per il vincolo numerico dell' attributo " + constr.getAttrName());
                                }
                            });

                        String attributesTempNum = getParameterList(json, constr);

                        constr.setMessage("Range massimo tra " + attributesTempNum + " è di " + constr.getMaxIntervalNumber());

                        break;
                    default:
                        break;
                }
            }

        String finaleReplace = cleanedStatement;

        for (Attribute attr : json.getAttrs())
        {
            if (null == attr.getParameter().getType())
                return wrappedResponse.setSuccess(false).setErrorMessages(new ArrayList<String>()
                {
                    {
                        add("Deve essere dichiarato il tipo per " + attr.getAttrName());
                    }
                });

            switch (attr.getParameter().getType())
            {
                case DATE:

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "to_date('" + IConstants.FAKE_DATE + "', 'YYYY/MM/DD HH24:MI:SS')");
                    break;

                case DATE_TRUNC:

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "to_date('" + IConstants.FAKE_DATE_TRUNC + "', 'YYYY/MM/DD')");

                    break;

                case NUMBER:

                    if (attr.getOperator().toUpperCase() == WhereConditionOperator.IN.getName())
                    {
                        finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                                "(9, 6)");

                        break;
                    }

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "6");
                    break;

                case STRING:

                    if (attr.getOperator().toUpperCase() == WhereConditionOperator.IN.getName())
                    {
                        finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                                "('X', 'Z')");

                        break;
                    }

                    finaleReplace = finaleReplace.replace(attr.getParameter().getName(),
                            "'X'");
                    break;

                default:
                    break;
            }
        }

        /**
         * Provo ad eseguire la query con valori fittizi quando la inserisco
         */
        if (execQuery)
            getByNativeQuery(finaleReplace, 1);

        return wrappedResponse.setEntity(json).setResponse();
    }

    private String getParameterList(QueryToJson query, Constraint constr)
    {
        String attributes = "";

        for (String param : constr.getParameters().split(","))
        {
            for (Attribute attr : query.getAttrs())
            {

                if (param.equalsIgnoreCase(attr.getParameter().getName()))
                    attributes += attr.getAlias() + " - ";
            }
        }

        return attributes.substring(0, attributes.length() - 3);
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

}

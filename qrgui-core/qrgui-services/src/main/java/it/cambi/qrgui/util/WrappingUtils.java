/**
 * 
 */
package it.cambi.qrgui.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.enums.JavaTypes;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static it.cambi.qrgui.util.IConstants.YYYY_MM_DD;
import static it.cambi.qrgui.util.IConstants.YYYY_MM_DD_HH_MI_SS;

/**
 * @author luca
 *
 */
public class WrappingUtils
{

    public static String cleanQueryString(String statement)
    {
        return statement.replaceAll("\\r\\n", " ").replaceAll("\\n", " ").replaceAll("\\t", " ").replaceAll(";", " ");
    }

    /**
     * Creo il work book che contiene tutte le query eseguite
     * 
     * @param pageSize
     * @param wb
     * @param response
     * @param fileName
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @SuppressWarnings("unchecked")
    public static int setWorkBookSheet(int pageSize, Workbook wb, XWrappedResponse<Temi15UteQue, List<Object>> response, String fileName, Sheet sheet,
            int rowToStart)
            throws JsonMappingException, JsonProcessingException, IOException
    {

        /**
         * Assegno il nome del file che poi viene recuperato dal front end
         */
        response.setQueryFilePath("files/" + fileName);

        /**
         * Recupero la lista di entity dalla response
         */
        List<Object> queryList = new ObjectMapper().readValue(response.getSerializedEntity(),
                new TypeReference<List<Object>>()
                {
                });

        if (null != queryList && queryList.size() > 0)
        {

            String sheetName = cleanSheetBookName(response.getXentity().getNam());

            /**
             * #############################################
             * 
             * Prima riga della query il nome in bold
             * 
             * #############################################
             */
            Row row = sheet.createRow(rowToStart);

            CellStyle style = wb.createCellStyle();// Create style
            Font font = wb.createFont();// Create font
            font.setBold(true);// Make font bold
            style.setFont(font);// set it to bold

            row.createCell(0).setCellStyle(style);
            row.getCell(0).setCellValue(sheetName);

            rowToStart = rowToStart + 2;

            /**
             * ##############################################
             * 
             * Setto le colonne
             * 
             * #############################################
             */
            Row rowColumns = sheet.createRow(rowToStart);

            /**
             * Recupero il json in cui ci sono le informazioni della query
             */
            QueryToJson json = new ObjectMapper().readValue(response.getXentity().getJson(), QueryToJson.class);

            /**
             * Per ogni colonna creo una cella nella prima riga
             */
            for (int k = 0; k < json.getQuerySelectColumns().size(); k++)
            {
                // Create a cell and put a value in it.
                Cell cell = rowColumns.createCell(k);
                cell.setCellValue(json.getQuerySelectColumns().get(k).getAs());
            }

            rowToStart++;

            /**
             * ##############################################
             * 
             * Aggiungo il result set della query
             * 
             * #############################################
             */
            /**
             * Creo una riga per ogni t-pla. Se il tipo di dato della colonna è una data, viene formattata come tale. TODO Per adesso l'unico database
             * utilizzato è oracle che con l'attuale driver restituisce un long. Può darsi che per altri database ci siano risultati diversi
             */
            for (int k = 0; k < queryList.size(); k++)
            {
                Row rows = sheet.createRow(rowToStart);

                List<Object> object = (List<Object>) queryList.get(k);

                for (int j = 0; j < object.size(); j++)
                {

                    JavaTypes javaType = json.getQuerySelectColumns().get(j).getType();
                    // Create a cell and put a value in it.
                    Cell cell = rows.createCell(j);

                    String value = object.get(j) == null ? "" : object.get(j).toString();

                    if (javaType == JavaTypes.DATE && null != value && !value.isEmpty())
                        value = DateUtils.getStringFromDate(new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS),
                                new Long(value));

                    if (javaType == JavaTypes.DATE_TRUNC && null != value && !value.isEmpty())
                        value = DateUtils.getStringFromDate(new SimpleDateFormat(YYYY_MM_DD),
                                new Long(value));

                    cell.setCellValue(value);
                }

                rowToStart++;
            }

            response.setEntity(queryList.subList(0, queryList.size() - pageSize > 0 ? pageSize : queryList.size()));
        }

        rowToStart++;

        return rowToStart;
    }

    public static String cleanSheetBookName(String name)
    {
        return name.replaceAll("\\r\\n", " ").replaceAll("\\n", " ").replaceAll("\\t", " ").replaceAll("\\*", "");
    }
}

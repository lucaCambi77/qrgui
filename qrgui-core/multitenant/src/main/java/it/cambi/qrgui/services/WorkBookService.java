package it.cambi.qrgui.services;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import it.cambi.qrgui.enums.JavaTypes;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static it.cambi.qrgui.util.Constants.YYYY_MM_DD;
import static it.cambi.qrgui.util.Constants.YYYY_MM_DD_HH_MI_SS;

@RequiredArgsConstructor
public class WorkBookService {

    private final AmazonS3 s3;

    @Value("${excel.path}")
    private String excelPath;

    @Value("${amazon.aws.s3.host:http://127.0.0.1:4566}")
    public String host;

    @Value("${amazon.aws.s3.bucket.name:bucket}")
    public String bucketName;

    private static final String fileName = "workbook.xls";

    public void createWorkBook(
            int pageSize, List<XWrappedResponse<UteQueDto, List<Object>>> listOut) throws IOException {

        String localFilePath = excelPath + fileName;

        int rowToStart = 0;
        Workbook wb = new HSSFWorkbook();

        Sheet sheet = wb.createSheet();
        for (XWrappedResponse<UteQueDto, List<Object>> response : listOut) {
            rowToStart =
                    setWorkBookSheet(
                            pageSize, wb, response, host + "/" + bucketName + "/" + fileName, sheet, rowToStart);
        }

        FileOutputStream fileOut = new FileOutputStream(localFilePath);
        wb.write(fileOut);
        fileOut.close();
        wb.close();

        uploadToS3Bucket(bucketName, fileName, localFilePath);

        new File(localFilePath).delete();
    }

    /**
     * Creo il work book che contiene tutte le query eseguite
     */
    public int setWorkBookSheet(
            int pageSize,
            Workbook wb,
            XWrappedResponse<UteQueDto, List<Object>> response,
            String fileName,
            Sheet sheet,
            int rowToStart)
            throws IOException {

        /** Assegno il nome del file che poi viene recuperato dal front end */
        response.setQueryFilePath(fileName);

        /** Recupero la lista di entity dalla response */
        List<Object> queryList = response.getEntity();

        if (null != queryList && queryList.size() > 0) {

            String sheetName = cleanSheetBookName(response.getXentity().getNam());

            /**
             * #############################################
             *
             * <p>Prima riga della query il nome in bold
             *
             * <p>#############################################
             */
            Row row = sheet.createRow(rowToStart);

            CellStyle style = wb.createCellStyle(); // Create style
            Font font = wb.createFont(); // Create font
            font.setBold(true); // Make font bold
            style.setFont(font); // set it to bold

            row.createCell(0).setCellStyle(style);
            row.getCell(0).setCellValue(sheetName);

            rowToStart = rowToStart + 2;

            /**
             * ##############################################
             *
             * <p>Setto le colonne
             *
             * <p>#############################################
             */
            Row rowColumns = sheet.createRow(rowToStart);

            /** Recupero il json in cui ci sono le informazioni della query */
            QueryToJson json =
                    new ObjectMapper().readValue(response.getXentity().getJson(), QueryToJson.class);

            /** Per ogni colonna creo una cella nella prima riga */
            for (int k = 0; k < json.getQuerySelectColumns().size(); k++) {
                // Create a cell and put a value in it.
                Cell cell = rowColumns.createCell(k);
                cell.setCellValue(json.getQuerySelectColumns().get(k).getAs());
            }

            rowToStart++;

            /**
             * ##############################################
             *
             * <p>Aggiungo il result set della query
             *
             * <p>#############################################
             */
            /**
             * Creo una riga per ogni t-pla. Se il tipo di dato della colonna è una data, viene formattata
             * come tale. TODO Per adesso l'unico database utilizzato è oracle che con l'attuale driver
             * restituisce un long. Può darsi che per altri database ci siano risultati diversi
             */
            for (Object o : queryList) {
                Row rows = sheet.createRow(rowToStart);

                Object[] object = (Object[]) o;

                for (int j = 0; j < object.length; j++) {

                    JavaTypes javaType = json.getQuerySelectColumns().get(j).getType();
                    // Create a cell and put a value in it.
                    Cell cell = rows.createCell(j);

                    String value = object[j] == null ? "" : object[j].toString();

                    if (javaType == JavaTypes.DATE && null != value && !value.isEmpty())
                        value =
                                DateUtils.getStringFromDate(
                                        new SimpleDateFormat(YYYY_MM_DD_HH_MI_SS), Long.parseLong(value));

                    if (javaType == JavaTypes.DATE_TRUNC && null != value && !value.isEmpty())
                        value = DateUtils.getStringFromDate(new SimpleDateFormat(YYYY_MM_DD), Long.parseLong(value));

                    cell.setCellValue(value);
                }

                rowToStart++;
            }

            response.setEntity(
                    queryList.subList(0, queryList.size() - pageSize > 0 ? pageSize : queryList.size()));
        }

        rowToStart++;

        return rowToStart;
    }

    public void uploadToS3Bucket(String bucket, String awsPath, String filePath) {
        s3.putObject(bucket, awsPath, new File(filePath));
    }

    public static String cleanSheetBookName(String name) {
        return name.replaceAll("\\r\\n", " ")
                .replaceAll("\\n", " ")
                .replaceAll("\\t", " ")
                .replaceAll("\\*", "");
    }
}

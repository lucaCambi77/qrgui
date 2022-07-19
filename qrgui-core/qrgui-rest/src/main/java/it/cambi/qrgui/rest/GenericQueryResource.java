package it.cambi.qrgui.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.cambi.qrgui.enums.Schema;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.services.WorkBookService;
import it.cambi.qrgui.services.oracle.entity.FirstOracleService;
import it.cambi.qrgui.services.oracle.taskExecutor.GenericQueryTaskExecutorService;
import it.cambi.qrgui.util.Constants;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import lombok.RequiredArgsConstructor;

@RequestMapping("/query")
@Component
@RequiredArgsConstructor
public class GenericQueryResource extends BasicResource {
  private static final Logger log = LoggerFactory.getLogger(GenericQueryResource.class);

  private final GenericQueryTaskExecutorService genericTaskExecutor;

  private final FirstOracleService firstOracleService;

  private final WorkBookService workBookService;

  @Value("${excel.path}")
  private String excelPath;

  @Value("${amazon.aws.s3.host:http://127.0.0.1:4566}")
  public String host;

  @Value("${amazon.aws.s3.bucket.name:bucket}")
  public String bucketName;

  private static final String fileName = "workbook.xls";

  /**
   * Esegue le query e crea un excel nella directory /files. Per ogni query viene creato uno sheet
   * con i risultati della query se ci sono. Il parametro createFile serve per gestire la creazione
   * del file il quale viene creato solo la prima volta e nelle eventuali richieste di altre pagine
   * dell'utente evita la ricreazione del file. Quando viene creato il file viene popolato con tutto
   * il result set di ogni query, mentre alla gui viene passato solo la pagina richiesta
   *
   * @param queries
   * @param page
   * @param pageSize
   * @param createFile
   * @return
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("execute_query")
  public ResponseEntity<String> executeQuery(
      @RequestBody List<Temi15UteQue> queries,
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "pageSize", required = false, defaultValue = Constants.TEN)
          Integer pageSize,
      @DefaultValue("false") @RequestParam("createFile") Boolean createFile,
      HttpServletRequest sr)
      throws IOException, ExecutionException, InterruptedException {

    log.info("Eseguo query ...");

    String localFilePath = excelPath + fileName;

    List<XWrappedResponse<Temi15UteQue, List<Object>>> listOut =
        genericTaskExecutor.executeQuery(queries, page, pageSize);

    /** Creo un file con i result set nel percorso files/ */
    if (createFile) {

      int rowToStart = 0;
      Workbook wb = new HSSFWorkbook();

      Sheet sheet = wb.createSheet();
      for (XWrappedResponse<Temi15UteQue, List<Object>> response : listOut) {
        rowToStart =
            workBookService.setWorkBookSheet(
                pageSize, wb, response, host + "/" + bucketName + "/" + fileName, sheet, rowToStart);
      }

      FileOutputStream fileOut = new FileOutputStream(localFilePath);
      wb.write(fileOut);
      fileOut.close();
      wb.close();

      workBookService.uploadToS3Bucket(bucketName, fileName, localFilePath);
    }

    return getObjectMapperXResponseList(listOut, sr);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("checkQuery")
  @RolesAllowed({Constants.F_QRQINS, Constants.R_FEPQRA})
  public ResponseEntity<String> checkQuery(@RequestBody Temi15UteQue query, HttpServletRequest sr)
      throws IOException {

    if (null == query.getTemi13DtbInf()
        || null == query.getTemi13DtbInf().getId()
        || null == query.getTemi13DtbInf().getId().getSch())
      return WrappedResponse.<Long>baseBuilder()
          .success(false)
          .build()
          .setErrorMessages(
              new ArrayList<String>() {
                {
                  add("E' necessario indicare uno schema su cui eseguire la query");
                }
              })
          .setResponse()
          .getResponse(sr);

    switch (Schema.valueOf(query.getTemi13DtbInf().getId().getSch())) {
      case TEST:
        return firstOracleService.checkQuery(query).getResponse(sr);

      default:
        return null;
    }
  }
}

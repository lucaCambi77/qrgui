package it.cambi.qrgui.rest;

import it.cambi.qrgui.enums.Schema;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.services.WorkBookService;
import it.cambi.qrgui.services.database.FirstDbService;
import it.cambi.qrgui.services.taskExecutor.GenericQueryTaskExecutorService;
import it.cambi.qrgui.util.Constants;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequestMapping("/query")
@Component
@Slf4j
@RequiredArgsConstructor
public class GenericQueryResource extends BasicResource {

  private final GenericQueryTaskExecutorService genericTaskExecutor;

  private final FirstDbService firstOracleService;

  private final WorkBookService workBookService;

  protected final WrappedResponse<XWrappedResponse<Temi15UteQue, List<Object>>> response;
  protected final WrappedResponse<List<XWrappedResponse<Temi15UteQue, List<Object>>>> responseList;

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
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "pageSize", required = false, defaultValue = Constants.TEN)
          Integer pageSize,
      @DefaultValue("false") @RequestParam("createFile") Boolean createFile,
      HttpServletRequest sr)
      throws IOException, ExecutionException, InterruptedException {

    log.info("Eseguo query ...");

    List<XWrappedResponse<Temi15UteQue, List<Object>>> listOut =
        genericTaskExecutor.executeQuery(queries, page, pageSize);

    /** Creo un file con i result set nel percorso files/ */
    if (createFile) workBookService.createWorkBook(pageSize, listOut);

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
      return response.toBuilder()
          .success(false)
          .build()
          .setErrorMessages(
              new ArrayList<>() {
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

  private <T> ResponseEntity<String> getObjectMapperXResponseList(
      List<XWrappedResponse<Temi15UteQue, List<Object>>> wrappedResponses, HttpServletRequest sr) {

    for (XWrappedResponse<Temi15UteQue, List<Object>> wrappedResponse : wrappedResponses) {
      if (wrappedResponse.isSuccess()) continue;

      /*
       * Response with errors
       */
      return response.toBuilder().entity(wrappedResponse).build().setResponse().getResponse(sr);
    }

    return responseList.toBuilder().entity(wrappedResponses).build().setResponse().getResponse(sr);
  }
}

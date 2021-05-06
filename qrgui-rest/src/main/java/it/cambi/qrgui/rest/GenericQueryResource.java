package it.cambi.qrgui.rest;

import it.cambi.qrgui.enums.Schema;
import it.cambi.qrgui.services.db.model.Temi15UteQue;
import it.cambi.qrgui.services.oracle.entity.FirstOracleService;
import it.cambi.qrgui.services.oracle.entity.QrtcpuOracleService;
import it.cambi.qrgui.services.oracle.taskExecutor.GenericQueryTaskExecutorService;
import it.cambi.qrgui.util.IConstants;
import it.cambi.qrgui.util.WrappingUtils;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/query")
@Component
public class GenericQueryResource extends BasicResource {
    private static final Logger log = LoggerFactory.getLogger(GenericQueryResource.class);

    @Autowired
    private GenericQueryTaskExecutorService genericTaskExecutor;

    @Autowired
    private FirstOracleService qrfepuOracleService;

    @Autowired
    private QrtcpuOracleService qrtcpuOracleService;

    /**
     * Metodo per estrarre il path della root della parte web , in modo da creare l'excel nel percorso /files
     *
     * @return
     */
    private String getFilePath() {

        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        int index = path.indexOf("WEB-INF");

        if (index == -1)
            return path;

        return path.substring(0, index) + "files/";
    }

    /**
     * Esegue le query e crea un excel nella directory /files. Per ogni query viene creato uno sheet con i risultati della query se ci sono. Il
     * parametro createFile serve per gestire la creazione del file il quale viene creato solo la prima volta e nelle eventuali richieste di altre
     * pagine dell'utente evita la ricreazione del file. Quando viene creato il file viene popolato con tutto il result set di ogni query, mentre alla
     * gui viene passato solo la pagina richiesta
     *
     * @param queries
     * @param page
     * @param pageSize
     * @param createFile
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("execute_query")
    public ResponseEntity executeQuery(@NotNull List<Temi15UteQue> queries, @RequestParam("page") Integer page,
                                       @RequestParam("pageSize") @DefaultValue(IConstants.TEN) int pageSize, @DefaultValue("false") @RequestParam("createFile") Boolean createFile,
                                       HttpServletRequest sr) {

        log.info("Eseguo query ...");

        String fileName = "workbook.xls";
        String filePath = getFilePath() + fileName;

        try {

            List<XWrappedResponse<Temi15UteQue, List<Object>>> listOut = genericTaskExecutor.executeQuery(queries, page, pageSize);

            /**
             * Creo un file con i result set nel percorso files/
             */
            if (createFile) {

                int rowToStart = 0;
                Workbook wb = new HSSFWorkbook();

                Sheet sheet = wb
                        .createSheet();
                for (XWrappedResponse<Temi15UteQue, List<Object>> response : listOut) {
                    rowToStart = WrappingUtils.setWorkBookSheet(pageSize, wb, response, fileName, sheet, rowToStart);

                }

                FileOutputStream fileOut = new FileOutputStream(filePath);
                wb.write(fileOut);
                fileOut.close();

                wb.close();
            }

            return getObjectMapperXResponseList(listOut, sr);

        } catch (Exception exception) {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @SuppressWarnings("serial")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("checkQuery")
    @RolesAllowed({IConstants.F_QRQINS, IConstants.R_FEPQRA})
    public ResponseEntity<String> checkQuery(@NotNull Temi15UteQue query, HttpServletRequest sr) {

        if (null == query.getTemi13DtbInf() || null == query.getTemi13DtbInf().getId()
                || null == query.getTemi13DtbInf().getId().getSch())
            return new WrappedResponse<>().setSuccess(false).setErrorMessages(new ArrayList<String>() {
                {
                    add("E' necessario indicare uno schema su cui eseguire la query");
                }
            }).setResponse().getResponse(sr);

        try {
            switch (Schema.valueOf(query.getTemi13DtbInf().getId().getSch())) {
                case QRFEPU:

                    return qrfepuOracleService.checkQuery(query).getResponse(sr);

                case QRTCPU:

                    return qrtcpuOracleService.checkQuery(query).getResponse(sr);

                default:
                    return null;
            }
        } catch (Exception exception) {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

}

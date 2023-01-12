
package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import it.cambi.qrgui.enums.Schema;
import it.cambi.qrgui.services.WorkBookService;
import it.cambi.qrgui.services.database.FirstDbService;
import it.cambi.qrgui.services.taskExecutor.GenericQueryTaskExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQINS;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/query")
@Slf4j
@RestController
@RequiredArgsConstructor
public class GenericQueryController {

    private final GenericQueryTaskExecutorService genericTaskExecutor;

    private final FirstDbService firstOracleService;

    private final WorkBookService workBookService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("execute_query")
    public List<XWrappedResponse<UteQueDto, List<Object>>> executeQuery(
            @RequestBody List<UteQueDto> queries,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10")
            Integer pageSize,
            @DefaultValue("false") @RequestParam("createFile") Boolean createFile,
            HttpServletRequest sr) throws IOException, ExecutionException, InterruptedException {

        log.info("Eseguo query ...");

        List<XWrappedResponse<UteQueDto, List<Object>>> listOut =
                genericTaskExecutor.executeQuery(queries, page, pageSize);

        if (createFile) workBookService.createWorkBook(pageSize, listOut);

        return listOut;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("checkQuery")
    @RolesAllowed({F_QRQINS, R_FEPQRA})
    public WrappedResponse<?> checkQuery(@RequestBody UteQueDto query, HttpServletRequest sr)
            throws IOException {

        switch (Schema.valueOf(query.getTemi13DtbInf().getId().getSch())) {
            case TEST:
                return firstOracleService.checkQuery(query);

            default:
                return null;
        }
    }

}


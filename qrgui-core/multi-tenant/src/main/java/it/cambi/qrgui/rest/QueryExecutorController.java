
package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.services.WorkBookService;
import it.cambi.qrgui.taskExecutor.DbService;
import it.cambi.qrgui.taskExecutor.GenericQueryExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequestMapping("/query")
@Slf4j
@RestController
@RequiredArgsConstructor
public class QueryExecutorController {

    private final GenericQueryExecutorService genericTaskExecutor;

    private final DbService dbService;

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
            @DefaultValue("false") @RequestParam("createFile") Boolean createFile) throws IOException {

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
    public WrappedResponse<QueryToJson> checkQuery(@RequestBody UteQueDto query)
            throws IOException {

        return dbService.checkQuery(query);

    }

}



package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import it.cambi.qrgui.services.WorkBookService;
import it.cambi.qrgui.services.database.FirstDbService;
import it.cambi.qrgui.services.taskExecutor.GenericQueryTaskExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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

    private final RestTemplate restTemplate;

    @Value("${multitenant.contextPath}")
    protected String multitenantUrl;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("execute_query")
    public XWrappedResponse[] executeQuery(
            @RequestBody List<UteQueDto> queries,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10")
            Integer pageSize,
            @DefaultValue("false") @RequestParam("createFile") Boolean createFile,
            HttpServletRequest sr) throws IOException {

        return restTemplate.postForObject(
                UriComponentsBuilder.fromHttpUrl(multitenantUrl + "query/execute_query")
                        .queryParam("createFile", createFile)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize)
                        .build()
                        .toString(),
                queries == null ? List.of() : queries,
                XWrappedResponse[].class);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("checkQuery")
    @RolesAllowed({F_QRQINS, R_FEPQRA})
    public WrappedResponse<?> checkQuery(@RequestBody UteQueDto query) {

        return
                restTemplate.postForObject(
                        UriComponentsBuilder.fromHttpUrl(multitenantUrl + "query/checkQuery")
                                .build()
                                .toString(),
                        query,
                        WrappedResponse.class);
    }

}


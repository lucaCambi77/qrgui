
package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQINS;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/query")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryExecutorResource extends BasicResource {

    private final RestTemplate restTemplate;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("execute_query")
    @PreAuthorize("hasAnyAuthority('" + F_QRQINS + "', '" + R_FEPQRA + "')")
    public ResponseEntity<WrappedResponse<?>> executeQuery(
            @RequestBody List<UteQueDto> queries,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10")
            Integer pageSize,
            @DefaultValue("false") @RequestParam("createFile") Boolean createFile,
            HttpServletRequest sr) {

        log.info("Eseguo query ...");

        return getXResponse(sr, () -> restTemplate.postForObject(
                UriComponentsBuilder.fromHttpUrl(servicesUrl + "query/execute_query")
                        .queryParam("createFile", createFile)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize)
                        .build()
                        .toString(),
                queries == null ? List.of() : queries,
                XWrappedResponse[].class)
        );
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("checkQuery")
    @PreAuthorize("hasAnyAuthority('" + F_QRQINS + "', '" + R_FEPQRA + "')")
    public ResponseEntity<WrappedResponse<?>> checkQuery(@RequestBody UteQueDto query, HttpServletRequest sr) {

        return getResponse(
                sr,
                () ->
                        restTemplate.postForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "query/checkQuery")
                                        .build()
                                        .toString(),
                                query,
                                WrappedResponse.class));
    }
}


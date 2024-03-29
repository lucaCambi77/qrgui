package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/query")
@Slf4j
@RestController
@RequiredArgsConstructor
public class QueryExecutorController {
  private final RestTemplate restTemplate;

  @Value("${multitenant.contextPath}")
  private String multiTenantUrl;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("execute_query")
  public List<XWrappedResponse<UteQueDto, List<Object>>> executeQuery(
      @RequestBody List<UteQueDto> queries,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
      @DefaultValue("false") @RequestParam("createFile") Boolean createFile) {

    log.info("Eseguo query ...");

    List<XWrappedResponse<UteQueDto, List<Object>>> listOut = new ArrayList<>();

    for (UteQueDto query : queries) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("X-TenantID", query.tenant());

      ResponseEntity<List<XWrappedResponse<UteQueDto, List<Object>>>> responses =
          restTemplate.exchange(
              UriComponentsBuilder.fromHttpUrl(multiTenantUrl + "query/execute_query")
                  .queryParam("createFile", createFile)
                  .queryParam("page", page)
                  .queryParam("pageSize", pageSize)
                  .build()
                  .toString(),
              HttpMethod.POST,
              new HttpEntity<>(List.of(query), headers),
              new ParameterizedTypeReference<>() {});

      Optional.ofNullable(responses.getBody()).map(listOut::addAll);
    }

    return listOut;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("checkQuery")
  public WrappedResponse<?> checkQuery(@RequestBody UteQueDto query) {

    log.info("Eseguo query ...");

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-TenantID", query.tenant());

    return restTemplate.postForObject(
        UriComponentsBuilder.fromHttpUrl(multiTenantUrl + "query/checkQuery").build().toString(),
        new HttpEntity<>(query, headers),
        WrappedResponse.class);
  }
}

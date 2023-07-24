package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.config.MultiTenantDataSource;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dbInfo")
@RestController
@RequiredArgsConstructor
public class DbInfoController {

  private final Map<String, DataSource> dataSourceMap;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> getDatabaseInfoList() {

    Set<Object> tenants =
        dataSourceMap.entrySet().stream()
            .flatMap(
                d -> Stream.of(((MultiTenantDataSource) d.getValue()).getResolvedDataSources()))
            .flatMap(d -> d.keySet().stream())
            .collect(Collectors.toSet());

    return WrappedResponse.baseBuilder().entity(tenants).count(tenants.size()).build();
  }
}

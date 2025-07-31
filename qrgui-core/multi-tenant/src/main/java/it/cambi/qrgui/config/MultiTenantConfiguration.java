package it.cambi.qrgui.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Configuration
public class MultiTenantConfiguration {

  private final String defaultTenant = "tenant_1";

  /**
   * Loads all the data sources in the /tenants folder. A request will then be able to understand
   * which is the tenant by using the {@link TenantFilter}
   *
   * @return
   */
  @Bean
  public DataSource dataSource() {
    URL url = this.getClass().getClassLoader().getResource("tenants");

    if (url == null) {
      throw new RuntimeException("Tenants directory 'tenants' do not exists");
    }

    File[] files = Optional.of(new File(url.getPath())).map(File::listFiles).orElse(new File[] {});

    Map<Object, Object> dataSources =
        Arrays.stream(files)
            .map(
                f -> {
                  Properties tenantProperties = new Properties();
                  try {
                    tenantProperties.load((new FileInputStream(f)));
                  } catch (IOException e) {
                    throw new RuntimeException("Problem in tenant datasource:" + e);
                  }
                  return tenantProperties;
                })
            .collect(
                Collectors.toMap(
                    p -> p.getProperty("name"),
                    p ->
                        DataSourceBuilder.create()
                            .driverClassName(p.getProperty("datasource.driver-class-name"))
                            .username(p.getProperty("datasource.username"))
                            .password(p.getProperty("datasource.password"))
                            .url(p.getProperty("datasource.url"))
                            .build()));

    if (dataSources.isEmpty()) throw new RuntimeException("No tenants currently available");

    AbstractRoutingDataSource dataSource = new MultiTenantDataSource();

    dataSource.setDefaultTargetDataSource(dataSources.get(defaultTenant));
    dataSource.setTargetDataSources(dataSources);

    dataSource.afterPropertiesSet();

    return dataSource;
  }
}

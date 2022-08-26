package it.cambi.qrgui.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/**
 * @author luca
 */
@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "firstEntityManagerFactory",
    transactionManagerRef = "firstTransactionManager")
@RequiredArgsConstructor
public class FirstDbAppConf {

  private final Environment env;

  @Bean
  public PlatformTransactionManager firstTransactionManager() {
    return new JpaTransactionManager(
        Objects.requireNonNull(firstEntityManagerFactory().getObject()));
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(testDataSource());
    factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    factory.setPersistenceUnitName("firstPU");
    factory.setPackagesToScan("it.cambi.qrgui.db");

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
    jpaProperties.put("hibernate.dialect", env.getProperty("datasource.test.driver-class-name"));

    factory.setJpaProperties(jpaProperties);

    return factory;
  }

  @Bean
  public DataSource testDataSource() {
    return DataSourceBuilder.create()
        .url(env.getProperty("datasource.test.jdbcUrl"))
        .username(env.getProperty("datasource.test.username"))
        .password(env.getProperty("datasource.test.password"))
        .build();
  }
}

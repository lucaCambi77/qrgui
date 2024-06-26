package it.cambi.qrgui.security;

import it.cambi.qrgui.security.db.model.SecurityUser;
import it.cambi.qrgui.security.jpa.repository.UserRepository;
import it.cambi.qrgui.security.services.SecurityUserDetailService;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author luca
 */
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackageClasses = {SecurityUser.class, SecurityUserDetailService.class})
@EnableJpaRepositories(
    basePackageClasses = UserRepository.class,
    entityManagerFactoryRef = "securityEntityManagerFactory",
    transactionManagerRef = "securityTransactionManager")
@RequiredArgsConstructor
@PropertySource("classpath:security.properties")
public class SecurityDbAppConf {

  private final Environment env;

  @Bean
  public DataSource securityDataSource() {
    return DataSourceBuilder.create()
        .url(env.getProperty("datasource.security.jdbcUrl"))
        .username(env.getProperty("datasource.security.username"))
        .password(env.getProperty("datasource.security.password"))
        .build();
  }

  @Bean
  public PlatformTransactionManager securityTransactionManager() {
    EntityManagerFactory factory = securityEntityManagerFactory().getObject();
    return new JpaTransactionManager(factory);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(securityDataSource());
    factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    factory.setPackagesToScan(SecurityUser.class.getPackage().getName());

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
    jpaProperties.put(
        "hibernate.dialect", env.getProperty("datasource.security.driver-class-name"));

    factory.setJpaProperties(jpaProperties);

    return factory;
  }
}

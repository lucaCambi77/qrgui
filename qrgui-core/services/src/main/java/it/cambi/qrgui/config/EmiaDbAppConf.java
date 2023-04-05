package it.cambi.qrgui.config;

import it.cambi.qrgui.model.Temi14UteCat;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
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
@ComponentScan(basePackageClasses = {Temi14UteCat.class})
@EnableJpaRepositories(
        entityManagerFactoryRef = "emiaEntityManagerFactory",
        transactionManagerRef = "emiaTransactionManager")
@RequiredArgsConstructor
@PropertySource("classpath:services.properties")
public class EmiaDbAppConf {

    private final Environment env;

    @Primary
    @Bean
    public DataSource emiaDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("datasource.emia.jdbcUrl"))
                .username(env.getProperty("datasource.emia.username"))
                .password(env.getProperty("datasource.emia.password"))
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager emiaTransactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return new JpaTransactionManager(
                Objects.requireNonNull(localContainerEntityManagerFactoryBean.getObject()));
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean emiaEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(emiaDataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan(Temi14UteCat.class.getPackage().getName());
        factory.setPersistenceUnitName("emiaPU");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        factory.setJpaProperties(jpaProperties);

        return factory;
    }
}

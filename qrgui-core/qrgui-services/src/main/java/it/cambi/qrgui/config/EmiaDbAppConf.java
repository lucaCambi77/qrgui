package it.cambi.qrgui.config;

import it.cambi.qrgui.jpa.repository.QueryRepository;
import it.cambi.qrgui.services.db.model.Temi13DtbInf;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@ComponentScan(basePackageClasses = {Temi13DtbInf.class})
@EnableJpaRepositories(basePackageClasses = QueryRepository.class, entityManagerFactoryRef = "emiaEntityManagerFactory", transactionManagerRef = "emiaTransactionManager")
@RequiredArgsConstructor
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
    public PlatformTransactionManager emiaTransactionManager() {
        return new JpaTransactionManager(Objects.requireNonNull(emiaEntityManagerFactory().getObject()));
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean emiaEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(emiaDataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan(Temi13DtbInf.class.getPackage().getName());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        if (null != env.getProperty("load.test.sql") && Objects.equals(env.getProperty("load.test.sql"), "true"))
            jpaProperties.put("hibernate.hbm2ddl.import_files", "init.sql");

        factory.setJpaProperties(jpaProperties);

        return factory;
    }

}

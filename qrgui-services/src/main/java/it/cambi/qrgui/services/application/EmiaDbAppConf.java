package it.cambi.qrgui.services.application;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

import it.cambi.qrgui.jpa.repository.QueryRepository;
import it.cambi.qrgui.services.db.model.Temi13DtbInf;

/**
 * 
 * @author luca
 *
 */
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackageClasses = { Temi13DtbInf.class })
@EnableJpaRepositories(basePackageClasses = QueryRepository.class, entityManagerFactoryRef = "emiaEntityManagerFactory", transactionManagerRef = "emiaTransactionManager")
public class EmiaDbAppConf
{

    @Autowired
    private Environment env;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "datasource.emia")
    public DataSource emiaDataSource()
    {
        return DataSourceBuilder.create()
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager emiaTransactionManager()
    {
        EntityManagerFactory factory = emiaEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean emiaEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(emiaDataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan(Temi13DtbInf.class.getPackage().getName());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        if (null != env.getProperty("load.test.sql") && env.getProperty("load.test.sql").equals("true")) {}
            jpaProperties.put("hibernate.hbm2ddl.import_files", "init.sql");

        factory.setJpaProperties(jpaProperties);

        return factory;
    }

}

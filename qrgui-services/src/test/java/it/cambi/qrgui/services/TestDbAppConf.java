package it.cambi.qrgui.services;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.cambi.qrgui.services.db.model.Temi13DtbInf;

/**
 * 
 * @author luca
 *
 */
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackageClasses = { Temi13DtbInf.class })
@EnableJpaRepositories(entityManagerFactoryRef = "testEntityManagerFactory", transactionManagerRef = "testTransactionManager")
public class TestDbAppConf
{

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "datasource.test")
    public DataSource testDataSource()
    {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public PlatformTransactionManager testTransactionManager()
    {
        EntityManagerFactory factory = testEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean testEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(testDataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPersistenceUnitName("testPU");
        factory.setPersistenceXmlLocation("classpath:META-INF/test-persistence.xml");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProperties.put("hibernate.hbm2ddl.import_files", "init.sql");

        factory.setJpaProperties(jpaProperties);

        return factory;
    }

}

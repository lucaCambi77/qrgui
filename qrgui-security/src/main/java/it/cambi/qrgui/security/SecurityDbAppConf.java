package it.cambi.qrgui.security;

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

import it.cambi.qrgui.security.db.model.GuiUser;
import it.cambi.qrgui.security.jpa.repository.UserRepository;
import it.cambi.qrgui.security.services.GuiUserDetailService;

/**
 * 
 * @author luca
 *
 */
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackageClasses = { GuiUser.class, GuiUserDetailService.class })
@EnableJpaRepositories(basePackageClasses = UserRepository.class, entityManagerFactoryRef = "securityEntityManagerFactory", transactionManagerRef = "securityTransactionManager")
public class SecurityDbAppConf
{

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "datasource.security")
    public DataSource securityDataSource()
    {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public PlatformTransactionManager securityTransactionManager()
    {
        EntityManagerFactory factory = securityEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(securityDataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan(GuiUser.class.getPackage().getName());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        factory.setJpaProperties(jpaProperties);

        return factory;
    }

}

package it.cambi.qrgui.services;

import it.cambi.qrgui.config.EmiaDbAppConf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmiaDbAppConf.class})
@TestPropertySource("/test.properties")
class ServicesApplicationTest {

    @PersistenceContext(name = "emiaTransactionManager")
    private EntityManager em;

    @Test
    void contextLoads() {
        assertNotNull(em);
    }

}

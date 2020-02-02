package it.cambi.qrgui.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import it.cambi.qrgui.services.application.EmiaDbAppConf;
import it.cambi.qrgui.services.application.QrguiServicesApplication;

@SpringBootTest(classes = { QrguiServicesApplication.class, EmiaDbAppConf.class })
@TestPropertySource("/test.properties")
class ServicesApplicationTest
{

    @PersistenceContext
    private EntityManager em;

    @Test
    void contextLoads()
    {
        assertNotNull(em);
    }

}

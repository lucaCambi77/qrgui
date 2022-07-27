package it.cambi.qrgui.services

import it.cambi.qrgui.config.FirstDbAppConf
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@ContextConfiguration(classes = FirstDbAppConf.class)
@TestPropertySource("/test.properties")
class ContextLoadTest extends Specification {

    @PersistenceContext(unitName = "firstPU")
    @Qualifier(value = "firstEntityManagerFactory")
    private EntityManager em;

    def "when context is loaded then entity manager is created"() {
        expect: "the native query is created"
        em.createNativeQuery("select * from GENERIC_TABLE")
    }
}

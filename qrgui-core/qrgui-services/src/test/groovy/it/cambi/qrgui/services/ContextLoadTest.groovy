package it.cambi.qrgui.services

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.config.FirstDbAppConf
import it.cambi.qrgui.config.ServiceConfig
import it.cambi.qrgui.model.Temi13DtbInf
import it.cambi.qrgui.model.Temi13DtbInfId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@ContextConfiguration(classes = [FirstDbAppConf.class, ServiceConfig.class])
@TestPropertySource("/services.properties")
class ContextLoadTest extends Specification {

    @PersistenceContext(unitName = "firstPU")
    @Qualifier(value = "firstEntityManagerFactory")
    EntityManager em;

    @Autowired
    ObjectMapper objectMapper

    def setup() {
    }

    def "when context is loaded then entity manager is created"() {
        expect: "the native query is created"
        em.createNativeQuery("select * from TEST.GENERIC_TABLE")
    }

    def "when context is loaded then object mapper is ok"() {
        given:
        Temi13DtbInf dtbInf = new Temi13DtbInf(new Temi13DtbInfId("ORACLE", "TEST"));

        when:
        String serializedEntity = objectMapper.writeValueAsString(dtbInf)

        then:
        dtbInf == objectMapper.readValue(serializedEntity, Temi13DtbInf.class)
    }
}

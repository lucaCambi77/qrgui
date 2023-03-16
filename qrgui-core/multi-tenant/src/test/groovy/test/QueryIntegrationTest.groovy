package test

import com.amazonaws.services.s3.AmazonS3
import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.MultiTenantApplication
import it.cambi.qrgui.config.MultiTenantConfiguration
import it.cambi.qrgui.taskExecutor.GenericQueryTaskExecutorService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest(
        classes = [MultiTenantApplication.class, MultiTenantConfiguration.class],
        webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/multitenant.properties")
class QueryIntegrationTest extends Specification {

    @Autowired
    WebApplicationContext context

    @Autowired
    ObjectMapper mapper

    @SpringBean
    AmazonS3 amazonS3 = Mock()

    @SpringBean
    GenericQueryTaskExecutorService queryTaskExecutorService = Mock()

    MockMvc mvc;

    def setup() {
        mvc = webAppContextSetup(context).build()
        amazonS3 = Mock(AmazonS3)
        queryTaskExecutorService = Mock(GenericQueryTaskExecutorService)
    }

    def "should get database list and available tenants at /GET /dbInfo"() throws Exception {

        expect: "data Sources are created by a properties file with a list of available tenants"

        and:
        mvc.perform(get("/dbInfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.entity').isArray())
                .andExpect(jsonPath('$.entity', hasSize(1)))
                .andExpect(jsonPath('$.entity', containsInAnyOrder('tenant_1')))
    }
}

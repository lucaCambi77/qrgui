package test

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.MultiTenantApplication
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse
import it.cambi.qrgui.config.MultiTenantConfiguration
import it.cambi.qrgui.services.WorkBookService
import it.cambi.qrgui.taskExecutor.DbService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest(
        classes = [MultiTenantApplication.class, MultiTenantConfiguration.class],
        webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/multitenant.properties")
class QueryMvcTest extends Specification {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    WorkBookService bookService;

    @SpringBean
    DbService dbService = Mock(DbService)

    MockMvc mvc;

    def setup() {
        mvc = webAppContextSetup(context).build();
    }

    def "should invoke checkQuery method given POST /query/check"() throws Exception {

        given:
        UteQueDto json = UteQueDto.builder().insQue(new Date()).que(1).build()
        dbService.checkQuery(json) >> WrappedResponse.baseBuilder().build()

        when:
        mvc.perform(post("/query/checkQuery").content(
                mapper.writeValueAsString(json))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        then:
        1 * dbService.checkQuery(json)
    }
}

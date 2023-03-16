package test

import com.amazonaws.services.s3.AmazonS3
import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.MultiTenantApplication
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse
import it.cambi.qrgui.config.MultiTenantConfiguration
import it.cambi.qrgui.taskExecutor.DbService
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest(
        classes = [MultiTenantApplication.class, MultiTenantConfiguration.class],
        webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/multitenant.properties")
class QueryMvcTest extends Specification {

    @Autowired
    WebApplicationContext context

    @Autowired
    ObjectMapper mapper

    @SpringBean
    DbService dbService = Mock(DbService)

    @SpringBean
    AmazonS3 amazonS3 = Mock()

    @SpringBean
    GenericQueryTaskExecutorService queryTaskExecutorService = Mock(GenericQueryTaskExecutorService);

    MockMvc mvc;

    def setup() {
        mvc = webAppContextSetup(context).build()
        amazonS3 = Mock(AmazonS3)
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

    def "should get query execution list at /POST /query/execute_query"() throws Exception {

        given:
        Integer page = 10
        Integer pageSize = 10
        List<UteQueDto> dtoList = List.of(UteQueDto.builder().que(1).insQue(new Date()).build())
        queryTaskExecutorService.executeQuery(dtoList, page, pageSize) >> List.of(XWrappedResponse.builder().build())

        when:
        mvc.perform(post("/query/execute_query")
                .content(mapper.writeValueAsString(dtoList))
                .contentType(MediaType.APPLICATION_JSON)
                .param("createFile", "false")
                .param("page", Integer.valueOf(page).toString())
                .param("pageSize", Integer.valueOf(pageSize).toString()))
                .andExpect(status().isOk())

        then:
        1 * queryTaskExecutorService.executeQuery(dtoList, page, pageSize)
    }
}

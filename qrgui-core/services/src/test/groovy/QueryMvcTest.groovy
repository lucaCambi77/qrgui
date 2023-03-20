import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.ServiceApplication
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse
import it.cambi.qrgui.rest.QueryExecutorController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus

@SpringBootTest(
        classes = [ServiceApplication.class])
@TestPropertySource("/services.properties")
class QueryMvcTest extends Specification {

    @Autowired
    WebApplicationContext context

    @Autowired
    ObjectMapper mapper

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QueryExecutorController controller;

    private MockRestServiceServer mockServer;

    def setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    def "should get query execution list at /POST /query/execute_query"() throws Exception {

        given:
        UteQueDto uteQueDto = UteQueDto.builder().que(1).insQue(new Date()).build();
        XWrappedResponse<UteQueDto, List<Object>> response = new XWrappedResponse<UteQueDto, List<Object>>().toBuilder()
                .xentity(uteQueDto)
                .entity(List.of("row1", "row2")).build()
        List<XWrappedResponse<UteQueDto, List<Object>>> mockedResponse = List.of(response)
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8080/query/execute_query?createFile=false&page=10&pageSize=10")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(mockedResponse)))

        when:
        List<XWrappedResponse<UteQueDto, List<Object>>> controllerResponse = controller.executeQuery(List.of(uteQueDto), 10, 10, false)

        then:
        controllerResponse != null && !controllerResponse.isEmpty()
        controllerResponse == mockedResponse
    }
}

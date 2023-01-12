package it.cambi.qrgui.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.RestApplication;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.exception.AppControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = {RestApplication.class, TestConfiguration.class, AppControllerAdvice.class},
        webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {"datasource.test.driver-class-name=org.hibernate.dialect.H2Dialect", "datasource.test.jdbcUrl=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS TEST"})
public class CategoryIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${services.contextPath}")
    protected String servicesUrl;

    private MockMvc mvc;

    private MockRestServiceServer mockServer;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @WithUserDetails(value = "user@xxx.com")
    public void shouldGetForbiddenWhenUserAccessAdminEndpoint() throws Exception {
        mvc.perform(post("/emia/category").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        result -> assertTrue(result.getResolvedException() instanceof AccessDeniedException))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin@xxx.com")
    public void shouldGetOkWhenAdminAccessAdminEndpoint() throws Exception {
        mockServer.expect(once(),
                        requestTo(servicesUrl + "category?tipCateg=" + R_FEPQRA))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new WrappedResponse<>()))
                );

        mvc.perform(post("/emia/category").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

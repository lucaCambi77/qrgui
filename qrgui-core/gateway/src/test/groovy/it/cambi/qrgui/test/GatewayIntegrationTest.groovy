package it.cambi.qrgui.test

import com.fasterxml.jackson.databind.ObjectMapper
import it.cambi.qrgui.RestApplication
import it.cambi.qrgui.api.model.CategoryDto
import it.cambi.qrgui.api.model.QueCatAssDto
import it.cambi.qrgui.api.model.QueCatAssId
import it.cambi.qrgui.api.model.UteQueDto
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse
import it.cambi.qrgui.client.FeignClient
import it.cambi.qrgui.exception.AppControllerAdvice
import it.cambi.qrgui.security.db.model.SecurityUser
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest(classes = [RestApplication.class, TestConfiguration.class, AppControllerAdvice.class],
        webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GatewayIntegrationTest extends Specification {

    @Autowired
    private WebApplicationContext context;

    @SpringBean
    private RestTemplate restTemplate = Mock()

    @SpringBean
    private FeignClient feign = Mock()

    @Autowired
    private ObjectMapper mapper;

    @Value('${services.contextPath}')
    private String servicesUrl;

    private MockMvc mvc;

    def setup() {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithUserDetails(value = "user@xxx.com")
    def "Status is 403 when user access restricted endpoint POST category"() throws Exception {

        when:
        mvc.perform(post("/emia/category").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccessDeniedException))
                .andExpect(status().isForbidden());

        then:
        0 * restTemplate.postForObject(servicesUrl + "category?tipCateg=" + R_FEPQRA, _, WrappedResponse.class)
    }

    @WithUserDetails(value = "admin@xxx.com")
    def "Status is 200 when user has role permission POST category"() throws Exception {
        given:
        CategoryDto categoryDto = CategoryDto.builder().cat(0).insCat(new Date()).build()

        when:
        MockHttpServletResponse response = mvc.perform(post("/emia/category")
                .content(mapper.writeValueAsString(categoryDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        then:
        response.status == HttpStatus.OK.value()
        1 * restTemplate.postForObject(servicesUrl + "category?tipCateg=" + R_FEPQRA, categoryDto, WrappedResponse.class) >> WrappedResponse.baseBuilder().entity(1).build()
    }

    @WithUserDetails(value = "admin@xxx.com")
    def "Status is 200 when user has role permission GET category"() throws Exception {
        given:
        CategoryDto categoryDto = CategoryDto.builder().cat(0).insCat(new Date()).build()

        when:
        MockHttpServletResponse response = mvc.perform(get("/emia/category")
                .content(mapper.writeValueAsString(categoryDto)).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        then:
        response.status == HttpStatus.OK.value()
        1 * feign.getCategory(List.of(R_FEPQRA)) >> WrappedResponse.baseBuilder().entity(1).build()
    }

    @WithUserDetails(value = "admin@xxx.com")
    def "Status is 200 when user has role permission POST query"() throws Exception {
        given:
        Date insQue = new Date()
        QueCatAssId catAssId = QueCatAssId.builder().que(1).insQue(insQue).cat(1).insCat(new Date()).build()
        UteQueDto uteQueDto = UteQueDto.builder()
                .que(1)
                .insQue(insQue)
                .temi16QueCatAsses(Set.of(QueCatAssDto.builder()
                        .id(catAssId).build())).build()

        when:
        MockHttpServletResponse mvcResult = mvc.perform(post("/emia/query")
                .content(mapper.writeValueAsString(uteQueDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        then:
        mvcResult.status == HttpStatus.OK.value()
        1 * restTemplate.postForObject(servicesUrl + "query", uteQueDto, WrappedResponse.class) >> WrappedResponse.baseBuilder().entity(1).build()
    }

    @WithAnonymousUser()
    def "Status is 200 when anonymous user POST login"() throws Exception {
        given:
        SecurityUser guiUser = SecurityUser.builder().password("1234").username("test").build()

        when:
        MockHttpServletResponse mvcResult = mvc.perform(post("/userProperties/login")
                .content(mapper.writeValueAsString(guiUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        then:
        mvcResult.status == HttpStatus.OK.value()
    }
}

import it.cambi.qrgui.ServiceApplication
import it.cambi.qrgui.model.Temi14UteCat
import it.cambi.qrgui.services.emia.api.ITemi14Service
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

@SpringBootTest(
        classes = [ServiceApplication],
        webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/services.properties")
class RestIntegrationTest extends Specification {

    @Autowired
    private WebApplicationContext context;

    @SpringBean
    private ITemi14Service<Temi14UteCat> temi14Service = Mock();

    @Value('${multitenant.contextPath}')
    private String servicesUrl;

    private MockMvc mvc;

    def setup() {
        mvc = webAppContextSetup(context).build();
    }

    def "Status is 200 when user access restricted endpoint POST category"() throws Exception {

        when:
        mvc.perform(post("/category")
                .param("tipCateg", R_FEPQRA)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}"))
                .andExpect(status().isOk());

        then:
        1 * temi14Service.saveCategory(_, _) >> List.of()
    }

    def "Status is 200 when user access restricted endpoint GET category"() throws Exception {

        when:
        mvc.perform(get("/category")
                .param("tipCateg", R_FEPQRA)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        then:
        1 * temi14Service.findAll(_, null) >> List.of()
    }

}

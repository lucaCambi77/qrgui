package it.cambi.qrgui.test;

import it.cambi.qrgui.RestApplication;
import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.model.Temi13DtbInfId;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.SecurityService;
import it.cambi.qrgui.security.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RestApplication.class, ConfigurationTest.class, ResourceControllerTest.class}, webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"server.servlet.contextPath=/api"})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ApplicationTest {

    private @Autowired
    DbInfoJpaRepository dbInforRepository;

    private @Autowired
    UserRoleRepository userRoleRepository;

    private @Autowired
    UserService userService;

    private @Autowired
    SecurityService securityService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    public void securityServiceTest() {
        assertNotNull(dbInforRepository.getOne(new Temi13DtbInfId("ORACLE", "TEST")));

        assertNotNull(userService.findByUsername("user@gmail.com"));
        assertNotNull(userService.findByUsername("admin@gmail.com"));
        assertEquals(2,
                userRoleRepository.findAll().size());

        /**
         * Apparently it is not possible to test authentication (at least with custom userDetailService) because mocking security means user already
         * exists (see also @WithUserDetails or @WithMockUser with which you can test authorization), so we test the authentication service
         */
        assertTrue(securityService.autoLogin("user@gmail.com", "1234"));

        // This is actually 401 case
        assertThrows(AuthenticationException.class, () ->
                securityService.autoLogin("user@gmail.com", "124")
        );
    }

    @Test
    public void publicUrl() throws Exception {
        mvc.perform(get("/login").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@gmail.com")
    public void securedUrlForbidden() throws Exception {
        mvc.perform(get("/secured").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unauthorized() throws Exception {
        mvc.perform(get("/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "admin@gmail.com")
    public void securedUrlOk() throws Exception {
        mvc.perform(get("/secured").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

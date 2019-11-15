package it.cambi.qrgui.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.jpa.repository.QueryRepositoryImpl;
import it.cambi.qrgui.rest.RestApplication;
import it.cambi.qrgui.security.jpa.repository.UserRoleRepository;
import it.cambi.qrgui.security.services.SecurityService;
import it.cambi.qrgui.security.services.UserService;
import it.cambi.qrgui.services.db.model.Temi13DtbInfId;

@SpringBootTest(classes = { RestApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext()
public class ApplicationTest
{

    private @Autowired DbInfoJpaRepository dbInforRepository;

    private @Autowired QueryRepositoryImpl queryRepository;

    private @Autowired UserRoleRepository userRoleRepository;

    private @Autowired UserService userService;

    private @Autowired SecurityService securityService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @BeforeEach
    public void setup()
    {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
    }

    @Test
    @Order(1)
    public void testSetUp()
    {
        assertNotNull(dbInforRepository.getOne(new Temi13DtbInfId("ORACLE", "TEST")));

        assertNotNull(queryRepository.getCriteriaBuilder());
        assertNotNull(userService.findByUsername("fake@gmail.com"));
        assertEquals(1,
                userRoleRepository.findAll().size());

        /**
         * Apparently it is not possible to test authentication (at least with custom userDetailService) because mocking security means user already
         * exists (see also @WithUserDetails or @WithMockUser with which you can test authorization), so we test the authentication service
         */
        assertEquals(true, securityService.autoLogin("fake@gmail.com", "1234"));
        assertThrows(AuthenticationException.class, () -> {
            securityService.autoLogin("fake@gmail.com", "124");
        });

    }

    @Test
    @WithUserDetails(value = "fake@gmail.com")
    @Order(2)
    public void restHelloWorld() throws Exception
    {
        mvc.perform(get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}

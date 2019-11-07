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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import it.cambi.qrgui.application.Application;
import it.cambi.qrgui.db.model.Temi13DtbInfId;
import it.cambi.qrgui.jpa.repository.DbInfoJpaRepository;
import it.cambi.qrgui.jpa.repository.QueryRepositoryImpl;
import it.cambi.qrgui.jpa.security.repository.UserRoleRepository;
import it.cambi.qrgui.services.security.SecurityService;
import it.cambi.qrgui.services.security.UserService;

@SpringBootTest(classes = { Application.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class QrGuiTest
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

        /**
         * Apparently it is not possible to test authentication because mocking security means user already exists (see also @WithUserDetails
         * or @WithMockUser with which you can test authorization), so we test the authentication service
         */
        assertEquals(true, securityService.autoLogin("fake@gmail.com", "1234"));
        assertThrows(AuthenticationException.class, () -> {
            securityService.autoLogin("fake@gmail.com", "124");
        });

        assertEquals(1, userRoleRepository.findAll().size());

    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(2)
    public void adminShouldFailWithUserRole() throws Exception
    {
        mvc.perform(get("/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Order(3)
    public void adminShouldSucceedWithAdminRole() throws Exception
    {
        mvc.perform(get("/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(4)
    public void userShouldSucceedWithUserRole() throws Exception
    {
        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Order(5)
    public void userShouldSucceedWithAdminRole() throws Exception
    {
        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "fake@gmail.com")
    @Order(6)
    public void userShouldSucceedWithRightCredential() throws Exception
    {
        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "fake@gmail.com")
    @Order(6)
    public void userShouldNotSucceedWithWrongRole() throws Exception
    {
        mvc.perform(get("/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}

package it.cambi.qrgui.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.qrgui.security.config.SecurityConfigurationTest;
import it.cambi.qrgui.security.db.model.GuiUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringSecurityConfig.class, ResourceControllerTest.class, SecurityConfigurationTest.class})
@WebAppConfiguration
@TestPropertySource("/test.properties")
@Sql(value = "/user_test.sql")
class SecurityApplicationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void adminShouldFailWithUserRole() throws Exception {
        mvc.perform(get("/admin").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminShouldSucceedWithAdminRole() throws Exception {
        mvc.perform(get("/admin").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void userShouldSucceedWithUserRole() throws Exception {
        mvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminRoleShouldSucceedOnUserRole() throws Exception {
        mvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "fake@gmail.com")
    public void userShouldSucceedWithRightCredential() throws Exception {
        mvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "fake@gmail.com")
    public void userShouldNotSucceedWithWrongRole() throws Exception {
        mvc.perform(get("/admin")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void publicUrlTest() throws Exception {
        String content = new ObjectMapper().writeValueAsString(
                GuiUser.builder().userId(1).username("user").build());

        mvc.perform(post("/login").content(content)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}

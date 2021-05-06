/**
 * 
 */
package it.cambi.qrgui.security;

import it.cambi.qrgui.security.db.model.GuiUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * @author luca
 *
 */
@CrossOrigin(origins = { "*" })
@RestController
@Slf4j
public class ResourceControllerTest
{

    @RolesAllowed("ADMIN")
    @GetMapping("/admin")
    public String admin()
    {
        return "Hello Admin!";
    }

    @RolesAllowed({ "ADMIN", "USER" })
    @GetMapping("/user")
    public String user()
    {
        return "Hello User!";
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody GuiUser user)
    {
        log.info("Logged user -> " + user.getUsername());
        return "Login";
    }
}

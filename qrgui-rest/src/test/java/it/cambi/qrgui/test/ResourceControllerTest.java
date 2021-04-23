/**
 * 
 */
package it.cambi.qrgui.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * @author luca
 *
 */
@CrossOrigin(origins = { "*" })
@RestController
public class ResourceControllerTest
{
    @GetMapping("/login")
    public String login()
    {
        return "Login!";
    }

    @GetMapping("/secured")
    @RolesAllowed("ADMIN")
    public String secured()
    {
        return "Secured!";
    }

    @GetMapping("/all")
    @RolesAllowed({"USER", "ADMIN"})
    public String user()
    {
        return "User!";
    }
}

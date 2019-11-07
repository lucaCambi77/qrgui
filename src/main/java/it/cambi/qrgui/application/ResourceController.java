/**
 * 
 */
package it.cambi.qrgui.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.cambi.qrgui.db.security.model.GuiUser;
import it.cambi.qrgui.services.security.SecurityService;

/**
 * @author luca
 *
 */
@CrossOrigin(origins = { "*" })
@RestController
public class ResourceController
{

    private @Autowired SecurityService securityService;

    @PostMapping("/userLogin")
    public String login(@RequestBody GuiUser requestUser)
    {
        boolean login = securityService.autoLogin(requestUser.getUserName(), requestUser.getPasswordConfirm());
        return "Hello Login!" + login;
    }

    @GetMapping("/admin")
    public String admin()
    {
        return "Hello Admin!";
    }

    @GetMapping("/user")
    public String user()
    {
        return "Hello User!";
    }

    @GetMapping("/")
    public String hello()
    {
        return "Hello QrGui!";
    }
}

/**
 * 
 */
package it.cambi.qrgui.security;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luca
 *
 */
@CrossOrigin(origins = { "*" })
@RestController
public class ResourceController
{

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

/**
 * 
 */
package it.cambi.qrgui.rest;

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

    @GetMapping("/")
    public String hello()
    {
        return "Hello QrGui!";
    }
}

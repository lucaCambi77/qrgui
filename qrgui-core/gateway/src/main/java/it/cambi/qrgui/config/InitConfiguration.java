/** */
package it.cambi.qrgui.config;

import it.cambi.qrgui.security.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author luca
 *     <p>Db init and Security import
 */
@Configuration
@Import({SecurityConfig.class})
@Profile("!test")
public class InitConfiguration {
}

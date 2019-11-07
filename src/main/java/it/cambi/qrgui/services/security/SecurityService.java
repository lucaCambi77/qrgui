/**
 * 
 */
package it.cambi.qrgui.services.security;

/**
 * @author luca
 *
 */
public interface SecurityService
{
    String findLoggedInUsername();

    boolean autoLogin(String username, String password);
}

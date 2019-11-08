/**
 * 
 */
package it.cambi.qrgui.security.services;

/**
 * @author luca
 *
 */
public interface SecurityService
{
    String findLoggedInUsername();

    boolean autoLogin(String username, String password);
}

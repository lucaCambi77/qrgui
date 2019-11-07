/**
 * 
 */
package it.cambi.qrgui.services.security;

import it.cambi.qrgui.db.security.model.GuiUser;

/**
 * @author luca
 *
 */
public interface UserService
{
    GuiUser save(GuiUser user);

    GuiUser findByUsername(String username);
}

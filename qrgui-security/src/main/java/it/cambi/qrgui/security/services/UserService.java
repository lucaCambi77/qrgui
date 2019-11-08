/**
 * 
 */
package it.cambi.qrgui.security.services;

import it.cambi.qrgui.security.db.model.GuiUser;

/**
 * @author luca
 *
 */
public interface UserService
{
    GuiUser save(GuiUser user);

    GuiUser findByUsername(String username);
}

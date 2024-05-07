/** */
package it.cambi.qrgui.security.services;

import it.cambi.qrgui.security.db.model.SecurityUser;

/**
 * @author luca
 */
public interface UserService {
  SecurityUser save(SecurityUser user);
}

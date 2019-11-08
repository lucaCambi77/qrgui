/**
 * 
 */
package it.cambi.qrgui.security.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author luca
 *
 */
@Embeddable
public class UserRoleId  implements java.io.Serializable
{

    private Long roleId;
    private Long userId;

    @Column(name = "roleId")
    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @Column(name = "userId")
    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

}

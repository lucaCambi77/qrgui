/**
 * 
 */
package it.cambi.qrgui.db.security.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author luca
 *
 */
@Entity
@Table(name = "USER_ROLE", schema = "SECURITY")
public class UserRole implements java.io.Serializable
{

    private Role role;
    private GuiUser user;
    private UserRoleId id;

    public UserRole()
    {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "roleId", referencedColumnName = "roleId", nullable = false, insertable = false, updatable = false) })
    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false, insertable = false, updatable = false) })
    public GuiUser getUser()
    {
        return user;
    }

    public void setUser(GuiUser user)
    {
        this.user = user;
    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "roleId", column = @Column(name = "roleId", nullable = false, precision = 10, scale = 0)),
            @AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, precision = 10, scale = 0)) })
    public UserRoleId getId()
    {
        return id;
    }

    public void setId(UserRoleId id)
    {
        this.id = id;
    }

}

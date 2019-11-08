/**
 * 
 */
package it.cambi.qrgui.security.db.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author luca
 *
 */
@Entity
@Table(name = "USER", schema = "SECURITY")
public class GuiUser implements java.io.Serializable
{

    private long userId;

    private String userName;

    private String password;

    private boolean active;

    private String passwordConfirm;

    private Set<UserRole> userRoles = new HashSet<UserRole>(0);

    public GuiUser()
    {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long id)
    {
        this.userId = id;
    }

    @Column
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Column
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Column
    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserRole> getUserRoles()
    {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> roles)
    {
        this.userRoles = roles;
    }

    @Transient
    public String getPasswordConfirm()
    {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm)
    {
        this.passwordConfirm = passwordConfirm;
    }

}

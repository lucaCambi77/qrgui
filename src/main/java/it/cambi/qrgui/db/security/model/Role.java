/**
 * 
 */
package it.cambi.qrgui.db.security.model;

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

/**
 * @author luca
 *
 */
@Entity
@Table(name = "ROLE", schema = "SECURITY")
public class Role implements java.io.Serializable
{
    private Long roleId;

    private String name;

    private Set<UserRole> userRoles = new HashSet<UserRole>(0);

    public Role()
    {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long id)
    {
        this.roleId = id;
    }

    @Column
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    public Set<UserRole> getUserRoles()
    {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> users)
    {
        this.userRoles = users;
    }
}

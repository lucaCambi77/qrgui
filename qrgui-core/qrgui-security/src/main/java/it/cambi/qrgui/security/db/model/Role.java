/**
 * 
 */
package it.cambi.qrgui.security.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author luca
 *
 */
@Entity
@Table(name = "ROLE", schema = "SECURITY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements java.io.Serializable
{
    private Long roleId;

    private String name;

    private Set<UserRole> userRoles = new HashSet<UserRole>(0);

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

/**
 * 
 */
package it.cambi.qrgui.security.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>(0);

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

/** */
package it.cambi.qrgui.security.db.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author luca
 */
@Entity
@Table(name = "USER_ROLE", schema = "SECURITY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements java.io.Serializable, GrantedAuthority {

  private Role role;
  private SecurityUser user;
  private UserRoleId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(
        name = "roleId",
        referencedColumnName = "roleId",
        nullable = false,
        insertable = false,
        updatable = false)
  })
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(
        name = "userId",
        referencedColumnName = "userId",
        nullable = false,
        insertable = false,
        updatable = false)
  })
  public SecurityUser getUser() {
    return user;
  }

  public void setUser(SecurityUser user) {
    this.user = user;
  }

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(
        name = "roleId",
        column = @Column(name = "roleId", nullable = false, precision = 10, scale = 0)),
    @AttributeOverride(
        name = "userId",
        column = @Column(name = "userId", nullable = false, precision = 10, scale = 0))
  })
  public UserRoleId getId() {
    return id;
  }

  public void setId(UserRoleId id) {
    this.id = id;
  }

  @Override
  @Transient
  public String getAuthority() {
    return role.getName();
  }
}

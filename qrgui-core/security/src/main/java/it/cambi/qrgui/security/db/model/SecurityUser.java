/** */
package it.cambi.qrgui.security.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author luca
 */
@Entity
@Table(name = "GUI_USER", schema = "SECURITY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements java.io.Serializable, UserDetails {

  private long userId;

  private String username;

  private String password;

  private boolean active;

  @Builder.Default private Set<UserRole> userRoles = new HashSet<>(0);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long getUserId() {
    return userId;
  }

  public void setUserId(long id) {
    this.userId = id;
  }

  @Column
  public String getUsername() {
    return username;
  }

  public void setUsername(String userName) {
    this.username = userName;
  }

  @Column
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  public Set<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(Set<UserRole> roles) {
    this.userRoles = roles;
  }

  @Override
  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return userRoles;
  }

  @Override
  @Transient
  public boolean isAccountNonExpired() {
    return active;
  }

  @Override
  @Transient
  public boolean isAccountNonLocked() {
    return active;
  }

  @Override
  @Transient
  public boolean isCredentialsNonExpired() {
    return active;
  }

  @Override
  @Transient
  public boolean isEnabled() {
    return active;
  }
}

package it.cambi.qrgui.api.user;

import static it.cambi.qrgui.api.user.RolesFunctions.*;

public enum ErtaQrGuiRoles {
  FEPQRA(R_FEPQRA),
  FEPQR1(R_FEPQR1),
  FEPQR2(R_FEPQR2);

  private final String role;

  ErtaQrGuiRoles(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}

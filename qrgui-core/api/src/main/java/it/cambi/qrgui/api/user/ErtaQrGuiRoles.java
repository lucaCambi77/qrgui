package it.cambi.qrgui.api.user;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR1;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR2;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

public enum ErtaQrGuiRoles {
  FEPQRA(R_FEPQRA),
  FEPQR1(R_FEPQR1),
  FEPQR2(R_FEPQR2);

  private String role;

  ErtaQrGuiRoles(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}

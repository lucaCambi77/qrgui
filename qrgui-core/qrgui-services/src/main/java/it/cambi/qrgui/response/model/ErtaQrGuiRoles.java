package it.cambi.qrgui.response.model;

import static it.cambi.qrgui.util.Constants.R_FEPQR1;
import static it.cambi.qrgui.util.Constants.R_FEPQR2;
import static it.cambi.qrgui.util.Constants.R_FEPQRA;

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

  public static ErtaQrGuiRoles fromString(String text) {
    for (ErtaQrGuiRoles erta : ErtaQrGuiRoles.values()) {
      if (erta.getRole().equalsIgnoreCase(text)) {
        return erta;
      }
    }
    return null;
  }
}

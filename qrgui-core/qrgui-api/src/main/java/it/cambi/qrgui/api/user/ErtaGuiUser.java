/**
 * Questo è l'oggetto user che viene usato per loggare la request dell'utente. Ogni chiamata rest
 * che viene effettuata logga la request e la respone. La request contiente gli attributi di questa
 * classe, in particolare il tipo di richiesta, la url e i parametri usati, oltre al browser ed
 * all'ip. La response viene loggata di seguito alla request nella wrappedResponse. Da notare che
 * questo oggetto viene creato nel @SecurityInterceptor ad ogni chiamata creando ogni volta un
 * contesto di sicurezza con un custom Principal
 */
package it.cambi.qrgui.api.user;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luca
 */
@Builder(toBuilder = true)
@Value
public class ErtaGuiUser {

  String userName;
  String password;
  String locale;
  String url;
  String queryString;
  String guiVersion;
  String requestType;
  String browser;
  List<ErtaQrGuiRoles> ertaQrGuiRoles = new ArrayList<>();
  @Builder.Default Boolean isAdmin = false;

  String address;

  Date requestDate = new Date();

  public List<String> getErtaQrGuiRoles() {
    List<String> rolesToString = new ArrayList<>();

    ertaQrGuiRoles.forEach((r) -> rolesToString.add(r.getRole()));

    return rolesToString;
  }

  public void addToErtaQrGuiRoles(String role) {
    this.ertaQrGuiRoles.add(ErtaQrGuiRoles.fromString(role));
  }

  public void addToErtaQrGuiRoles(ErtaQrGuiRoles role) {
    this.ertaQrGuiRoles.add(role);
  }

  public boolean isAdmin() {
    if (getErtaQrGuiRoles().contains(ErtaQrGuiRoles.FEPQRA.getRole())) return true;

    return isAdmin;
  }
}

/**
 * Questo è l'oggetto user che viene usato per loggare la request dell'utente. Ogni chiamata rest
 * che viene effettuata logga la request e la respone. La request contiente gli attributi di questa
 * classe, in particolare il tipo di richiesta, la url e i parametri usati, oltre al browser ed
 * all'ip. La response viene loggata di seguito alla request nella wrappedResponse. Da notare che
 * questo oggetto viene creato nel @SecurityInterceptor ad ogni chiamata creando ogni volta un
 * contesto di sicurezza con un custom Principal
 */
package it.cambi.qrgui.api.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luca
 */
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErtaGuiUser {

  String userName;
  String password;
  String locale;
  String url;
  String queryString;
  String guiVersion;
  String requestType;
  String browser;

  @Builder.Default List<ErtaQrGuiRoles> ertaQrGuiRoles = new ArrayList<>();
  @Builder.Default Boolean isAdmin = false;

  String address;

  public boolean isAdmin() {
    return this.ertaQrGuiRoles.stream()
            .map(ErtaQrGuiRoles::getRole)
            .collect(Collectors.toSet())
            .contains(ErtaQrGuiRoles.FEPQRA.getRole())
        || this.isAdmin;
  }
}

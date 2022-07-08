package it.cambi.qrgui.session;

import it.cambi.qrgui.response.model.ErtaGuiUser;
import it.cambi.qrgui.response.model.ErtaQrGuiRoles;
import it.cambi.qrgui.rest.BasicResource;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.util.IConstants.R_FEPQR1;
import static it.cambi.qrgui.util.IConstants.R_FEPQR2;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

@Component
@RequestMapping("/userProperties")
@Slf4j
public class UserPropertiesResource extends BasicResource {

  @PostMapping
  @RolesAllowed({R_FEPQRA, R_FEPQR1, R_FEPQR2})
  public ResponseEntity<String> getUserPrincipal( ErtaGuiUser user, HttpServletRequest sr) {

    if (null != user.getUrl() && user.getUrl().contains("localhost")) {
      user.setUserName("Localhost");
      user.addToErtaQrGuiRoles(ErtaQrGuiRoles.FEPQRA);
      return WrappedResponse.<ErtaGuiUser>baseBuilder()
          .entity(user)
          .count(1)
          .build()
          .setResponse()
          .getResponse(sr);
    }

    try {
      user.setUserName(null == sr.getUserPrincipal() ? "" : sr.getUserPrincipal().getName());

      for (ErtaQrGuiRoles role : ErtaQrGuiRoles.values()) {
        if (sr.isUserInRole(role.getRole())) {
          user.addToErtaQrGuiRoles(role);
          log.info(
              "Utente " + sr.getUserPrincipal().getName() + " ha il ruolo di " + role.getRole());
        }
      }

      String[] ignorableFieldNames = {"password"};

      return WrappedResponse.<ErtaGuiUser>baseBuilder()
          .entity(user)
          .count(1)
          .build()
          .setIgnorableFields(ignorableFieldNames)
          .setResponse()
          .getResponse(sr);

    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping
  @RequestMapping("/login")
  public ResponseEntity<String> login(ErtaGuiUser user, HttpServletRequest sr) {

    if (null != user.getUrl() && user.getUrl().contains("localhost")) {
      user.setGuiVersion("Development");
      return WrappedResponse.<ErtaGuiUser>baseBuilder()
          .entity(user)
          .count(1)
          .build()
          .setResponse()
          .getResponse(sr);
    }

    try {

      for (ErtaQrGuiRoles role : ErtaQrGuiRoles.values()) {
        if (sr.isUserInRole(role.getRole()))
          log.info(
              "Utente " + sr.getUserPrincipal().getName() + " ha il ruolo di " + role.getRole());
      }

      String[] ignorableFieldNames = {"password"};

      return WrappedResponse.<ErtaGuiUser>baseBuilder()
          .entity(user)
          .count(1)
          .build()
          .setIgnorableFields(ignorableFieldNames)
          .setResponse()
          .getResponse(sr);

    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }
}

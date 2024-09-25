package it.cambi.qrgui.rest.session;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR1;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR2;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

import it.cambi.qrgui.api.user.ErtaGuiUser;
import it.cambi.qrgui.api.user.ErtaQrGuiRoles;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.rest.BasicController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userProperties")
@Slf4j
@RequiredArgsConstructor
public class UserPropertiesController extends BasicController {

  private final WrappedResponse<ErtaGuiUser> response = new WrappedResponse<>();

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyAuthority('" + R_FEPQRA + "', '" + R_FEPQR1 + "', '" + R_FEPQR2 + "')")
  public ResponseEntity<WrappedResponse<?>> getUserPrincipal(
      @RequestBody ErtaGuiUser user, HttpServletRequest sr) {

    return getResponse(
        sr,
        () ->
            response.toBuilder()
                .entity(
                    user.toBuilder()
                        .ertaQrGuiRoles(
                            Arrays.stream(ErtaQrGuiRoles.values())
                                .filter(r -> sr.isUserInRole(r.getRole()))
                                .collect(Collectors.toList()))
                        .userName(
                            null == sr.getUserPrincipal() ? "" : sr.getUserPrincipal().getName())
                        .build())
                .count(1)
                .build());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("/login")
  public ResponseEntity<WrappedResponse<?>> login(
      @RequestBody ErtaGuiUser user, HttpServletRequest sr) {

    if (null != user.getUrl() && user.getUrl().contains("localhost")) {
      user.toBuilder().guiVersion("Development");
      return getResponse(sr, () -> response.toBuilder().entity(user).count(1).build());
    }

    for (ErtaQrGuiRoles role : ErtaQrGuiRoles.values()) {
      if (sr.isUserInRole(role.getRole()))
        log.info("Utente " + sr.getUserPrincipal().getName() + " ha il ruolo di " + role.getRole());
    }

    return getResponse(sr, () -> response.toBuilder().entity(user).count(1).build());
  }
}

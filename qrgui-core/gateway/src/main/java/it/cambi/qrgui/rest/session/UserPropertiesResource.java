package it.cambi.qrgui.rest.session;

import it.cambi.qrgui.api.user.ErtaGuiUser;
import it.cambi.qrgui.api.user.ErtaQrGuiRoles;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.rest.BasicResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR1;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR2;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@Component
@RequestMapping("/userProperties")
@Slf4j
@RequiredArgsConstructor
public class UserPropertiesResource extends BasicResource {

    private final WrappedResponse<ErtaGuiUser> response = new WrappedResponse<>();

    @PostMapping
    @RolesAllowed({R_FEPQRA, R_FEPQR1, R_FEPQR2})
    public ResponseEntity<WrappedResponse<?>> getUserPrincipal(
            ErtaGuiUser user, HttpServletRequest sr) {

        return getResponse(sr, () -> response.toBuilder()
                .entity(
                        user.toBuilder()
                                .ertaQrGuiRoles(Arrays.stream(ErtaQrGuiRoles.values()).filter(r -> sr.isUserInRole(r.getRole())).collect(Collectors.toList()))
                                .userName(null == sr.getUserPrincipal() ? "" : sr.getUserPrincipal().getName())
                                .build())
                .count(1)
                .build());
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<WrappedResponse<?>> login(ErtaGuiUser user, HttpServletRequest sr) {

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

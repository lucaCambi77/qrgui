package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR1;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQR2;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/emia/dbInfo")
@RestController
@Slf4j
@RequiredArgsConstructor
public class DbInfoResource extends BasicResource {

    private final RestTemplate restTemplate;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({R_FEPQRA, R_FEPQR1, R_FEPQR2})
    public ResponseEntity<WrappedResponse<?>> getDatabaseInfoList(HttpServletRequest sr) {
        log.info("Recupero informazioni degli schema utilizzati dall'applicazione ...");

        return getResponse(
                sr, () -> restTemplate.getForObject(multiTenantUrl + "dbInfo", WrappedResponse.class));
    }
}

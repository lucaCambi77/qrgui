package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteRouDto;
import it.cambi.qrgui.api.model.UteRouId;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG01;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRE00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRINS;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRMOD;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/emia/routine")
@RestController
@Slf4j
@RequiredArgsConstructor
public class RoutineResource extends BasicResource {
    private final RestTemplate restTemplate;

    @GetMapping
    @RolesAllowed({F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> getRoutines(
            Authentication authentication, HttpServletRequest sr) {
        log.info("... cerco tutte le routines");

        return getResponse(
                sr,
                () ->
                        restTemplate.getForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "routine")
                                        .queryParam("tipCateg", authentication.getAuthorities())
                                        .build()
                                        .toString(),
                                WrappedResponse.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({F_QRRINS, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> postRoutine(
            @RequestBody UteRouDto temi17, HttpServletRequest sr) {
        log.info("... creo una nuova routine");

        return getResponse(
                sr,
                () -> restTemplate.postForObject(servicesUrl + "routine", temi17, WrappedResponse.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("delete")
    @RolesAllowed({F_QRRMOD, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> deleteRoutine(
            @RequestBody UteRouId crou, HttpServletRequest sr) {
        log.info("... cancella la routine " + crou);

        return getResponse(
                sr,
                () ->
                        restTemplate.postForObject(
                                servicesUrl + "routine/delete", crou, WrappedResponse.class));
    }
}

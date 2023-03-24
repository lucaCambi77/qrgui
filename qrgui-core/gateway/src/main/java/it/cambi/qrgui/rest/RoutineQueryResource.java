package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.RouQueId;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRINS;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/emia/routQuery")
@RestController
@Slf4j
@RequiredArgsConstructor
public class RoutineQueryResource extends BasicResource {

    private final RestTemplate restTemplate;

    @PostMapping
    @RolesAllowed({F_QRRINS, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> postQueRoutine(
            @RequestBody RouQueId temi18Pk, HttpServletRequest sr) {
        log.info("... creo una nuova routine");

        return getResponse(
                sr,
                () ->
                        restTemplate.postForObject(servicesUrl + "routQuery", temi18Pk, WrappedResponse.class));
    }

    @PostMapping
    @RequestMapping("delete")
    @RolesAllowed({F_QRRINS, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> deleteQueRoutineAssoc(
            @RequestBody RouQueId temi18Pk, HttpServletRequest sr) {
        log.info("... elimino assciazione routine della query " + temi18Pk.que());

        return getResponse(
                sr,
                () ->
                        restTemplate.postForObject(
                                servicesUrl + "routQuery/delete", temi18Pk, WrappedResponse.class));
    }
}

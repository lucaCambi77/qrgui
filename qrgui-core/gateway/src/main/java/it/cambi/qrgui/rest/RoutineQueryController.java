package it.cambi.qrgui.rest;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRINS;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

import it.cambi.qrgui.api.model.RouQueId;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/emia/routQuery")
@RestController
@Slf4j
@RequiredArgsConstructor
public class RoutineQueryController extends BasicController {

  private final RestTemplate restTemplate;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyAuthority('" + F_QRRINS + "', '" + R_FEPQRA + "')")
  public ResponseEntity<WrappedResponse<?>> postQueRoutine(
      @RequestBody RouQueId temi18Pk, HttpServletRequest sr) {
    log.info("... creo una nuova routine");

    return getResponse(
        sr,
        () ->
            restTemplate.postForObject(servicesUrl + "routQuery", temi18Pk, WrappedResponse.class));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  @PreAuthorize("hasAnyAuthority('" + F_QRRINS + "', '" + R_FEPQRA + "')")
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

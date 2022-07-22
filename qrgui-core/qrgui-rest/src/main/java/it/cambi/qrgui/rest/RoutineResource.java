package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.Constants.F_QRCG00;
import static it.cambi.qrgui.util.Constants.F_QRCG01;
import static it.cambi.qrgui.util.Constants.F_QRRE00;
import static it.cambi.qrgui.util.Constants.F_QRRINS;
import static it.cambi.qrgui.util.Constants.R_FEPQRA;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.services.emia.api.ITemi17Service;
import it.cambi.qrgui.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/routine")
@Component
@Slf4j
@RequiredArgsConstructor
public class RoutineResource extends BasicResource {
  private final ITemi17Service<Temi17UteRou> temi17Service;

  @GetMapping
  @RolesAllowed({F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA})
  public ResponseEntity<String> getRoutines(HttpServletRequest request, HttpServletRequest sr) {
    log.info("... cerco tutte le routines");
    return temi17Service.findAll(request).getResponse(sr);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @RolesAllowed({F_QRRINS, R_FEPQRA})
  public ResponseEntity<String> postRoutine(
      @RequestBody Temi17UteRou temi17, HttpServletRequest sr) {
    log.info("... creo una nuova routine");
    return temi17Service.merge(temi17).getResponse(sr);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  @RolesAllowed({Constants.F_QRRMOD, R_FEPQRA})
  public ResponseEntity<String> deleteRoutine(
      @RequestBody Temi17UteRouId crou, HttpServletRequest sr) {
    log.info("... cancella la routine " + crou);
    return temi17Service.delete(crou).getResponse(sr);
  }
}

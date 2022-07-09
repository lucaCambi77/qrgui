package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.IConstants.F_QRCG00;
import static it.cambi.qrgui.util.IConstants.F_QRCG01;
import static it.cambi.qrgui.util.IConstants.F_QRRE00;
import static it.cambi.qrgui.util.IConstants.F_QRRINS;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

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
import it.cambi.qrgui.util.IConstants;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/routine")
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi17Resource extends BasicResource {
  private final ITemi17Service<Temi17UteRou> temi17Service;

  @GetMapping
  @RolesAllowed({F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA})
  public ResponseEntity<String> getRoutines(HttpServletRequest request, HttpServletRequest sr) {

    log.info("... cerco tutte le routines");

    try {
      return temi17Service.findAll(request).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @RolesAllowed({F_QRRINS, R_FEPQRA})
  public ResponseEntity<String> postRoutine(
       @RequestBody Temi17UteRou temi17, HttpServletRequest sr) {

    log.info("... creo una nuova routine");

    try {
      return temi17Service.merge(temi17).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  @RolesAllowed({IConstants.F_QRRMOD, R_FEPQRA})
  public ResponseEntity<String> deleteRoutine(
       @RequestBody Temi17UteRouId crou, HttpServletRequest sr) {

    log.info("... cancella la routine " + crou);

    try {
      return temi17Service.delete(crou).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }
}

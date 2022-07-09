package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.IConstants.F_QRRINS;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi18Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/routQuery")
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi18Resource extends BasicResource {
  private final ITemi18Service<Temi18RouQue> temi18Service;

  @PostMapping
  @RolesAllowed({F_QRRINS, R_FEPQRA})
  public ResponseEntity<String> postQueRoutine(
       @RequestBody Temi18RouQueId temi18Pk, HttpServletRequest sr) {

    log.info("... creo una nuova routine");

    try {
      return temi18Service.merge(temi18Pk).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping
  @RequestMapping("delete")
  @RolesAllowed({F_QRRINS, R_FEPQRA})
  public ResponseEntity<String> deleteQueRoutineAssoc(
       @RequestBody Temi18RouQueId temi18Pk, HttpServletRequest sr) {

    log.info("... elimino assciazione routine della query " + temi18Pk.getQue());

    try {
      return temi18Service.deleteQueRoutineAssoc(temi18Pk).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }
}

package it.cambi.qrgui.rest;

import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.services.emia.api.ITemi13Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.util.IConstants.R_FEPQR1;
import static it.cambi.qrgui.util.IConstants.R_FEPQR2;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

@RequestMapping("/emia/dbInfo")
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi13Resource extends BasicResource {
  private final ITemi13Service<Temi13DtbInf> databaseInfoService;

  @GetMapping
  @RolesAllowed({R_FEPQRA, R_FEPQR1, R_FEPQR2})
  public ResponseEntity<String> getDatabaseInfoList(HttpServletRequest sr) {

    log.info("Recupero informazioni degli schema utilizzati dall'applicazione ...");

    try {

      return databaseInfoService.findAll().getResponse(sr);

    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }
}

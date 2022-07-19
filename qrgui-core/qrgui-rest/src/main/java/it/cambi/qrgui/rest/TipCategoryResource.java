package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.Constants.F_QRCG00;
import static it.cambi.qrgui.util.Constants.F_QRCG01;
import static it.cambi.qrgui.util.Constants.F_QRRE00;
import static it.cambi.qrgui.util.Constants.R_FEPQRA;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/anaTipCat")
@Component
@RequiredArgsConstructor
@Slf4j
public class TipCategoryResource extends BasicResource {
  private final ITemi20Service<Temi20AnaTipCat> temi20Service;

  @GetMapping
  @RolesAllowed({F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA})
  public ResponseEntity<String> getAnaTipCat(
      @RequestParam(value = "cque", required = false) Long cque,
      @RequestParam(value = "crou", required = false) Long crou,
      HttpServletRequest sr) {
    log.info("... cerco tutti i tipi categoria");
    return temi20Service.getByCategory(sr).getResponse(sr);
  }
}

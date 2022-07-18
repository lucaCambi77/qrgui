package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.IConstants.F_QRCG00;
import static it.cambi.qrgui.util.IConstants.F_QRCG01;
import static it.cambi.qrgui.util.IConstants.F_QRCMOD;
import static it.cambi.qrgui.util.IConstants.F_QRQE00;
import static it.cambi.qrgui.util.IConstants.F_QRQMOD;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/queCatAssoc")
@Component
@RequiredArgsConstructor
@Slf4j
public class QueryCategoryResource extends BasicResource {
  private final ITemi16Service<Temi16QueCatAss> temi16Service;

  @GetMapping
  @RolesAllowed({F_QRCG00, F_QRCG01, F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getQueCatAssoc(HttpServletRequest request, HttpServletRequest sr) {
    log.info("... cerco tutte le associazioni categorie - query");
    return temi16Service.findByCategory(request).getResponse(sr);
  }

  @PostMapping
  @RequestMapping("post")
  @RolesAllowed({R_FEPQRA, F_QRQMOD, F_QRCMOD})
  public ResponseEntity<String> addQueriesToCategory(
      @RequestBody List<Temi16QueCatAss> temi16, HttpServletRequest sr) {
    log.info("... aggiungo le queries alla categoria ");
    return temi16Service.addQueriesToCateg(temi16).getResponse(sr);
  }
}
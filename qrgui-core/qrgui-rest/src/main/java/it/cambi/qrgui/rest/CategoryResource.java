package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.IConstants.F_QRCG00;
import static it.cambi.qrgui.util.IConstants.F_QRCG01;
import static it.cambi.qrgui.util.IConstants.F_QRCINS;
import static it.cambi.qrgui.util.IConstants.F_QRCMOD;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.services.exception.NoCategoriesAllowedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/category")
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryResource extends BasicResource {
  private final ITemi14Service<Temi14UteCat> temi14Service;

  @GetMapping
  @RolesAllowed({F_QRCG00, F_QRCG01, R_FEPQRA})
  public ResponseEntity<String> getCategories(HttpServletRequest sr)
      throws NoCategoriesAllowedException {
    log.info("... cerco tutte le categorie");
    return temi14Service.findAll(sr, null).getResponse(sr);
  }

  @PostMapping
  @RolesAllowed({F_QRCINS, R_FEPQRA})
  public ResponseEntity<String> postCategory(
      @RequestBody Temi14UteCat temi14, HttpServletRequest sr) throws NoCategoriesAllowedException {
    log.info("... creo una nuova categoria");
    return temi14Service.saveCategory(sr, temi14).getResponse(sr);
  }

  @PostMapping
  @RequestMapping("delete")
  @RolesAllowed({F_QRCMOD, R_FEPQRA})
  public ResponseEntity<String> deleteCategory(@RequestBody Temi14UteCat id, HttpServletRequest sr)
      throws NoCategoriesAllowedException {
    log.info("... cancello categoria");
    return temi14Service.deleteCategory(sr, id).getResponse(sr);
  }
}

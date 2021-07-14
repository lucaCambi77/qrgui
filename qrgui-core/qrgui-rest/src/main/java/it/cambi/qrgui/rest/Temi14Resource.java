package it.cambi.qrgui.rest;

import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static it.cambi.qrgui.util.IConstants.*;

@RequestMapping("/emia/category")
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi14Resource extends BasicResource {
  private final ITemi14Service<Temi14UteCat> temi14Service;

  @GetMapping
  @RolesAllowed({F_QRCG00, F_QRCG01, R_FEPQRA})
  public ResponseEntity<String> getCategories(HttpServletRequest sr) {

    log.info("... cerco tutte le categorie");

    try {
      return temi14Service.findAll(sr, null).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping
  @RolesAllowed({F_QRCINS, R_FEPQRA})
  public ResponseEntity<String> postCategory(
      @NotNull @RequestBody Temi14UteCat temi14, HttpServletRequest sr) {

    log.info("... creo una nuova categoria");

    try {

      return temi14Service.saveCategory(sr, temi14).getResponse(sr);
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
  @RolesAllowed({F_QRCMOD, R_FEPQRA})
  public ResponseEntity<String> deleteCategory(
      @NotNull @RequestBody Temi14UteCat id, HttpServletRequest sr) {

    log.info("... cancello categoria");

    try {
      return temi14Service.deleteCategory(sr, id).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }
}

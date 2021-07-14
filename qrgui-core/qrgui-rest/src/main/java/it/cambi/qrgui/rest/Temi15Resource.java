package it.cambi.qrgui.rest;

import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

import static it.cambi.qrgui.util.IConstants.*;

@RequestMapping("/emia/query")
@Component
@RequiredArgsConstructor
@Slf4j
public class Temi15Resource extends BasicResource {
  private final ITemi15Service<Temi15UteQue> temi15Service;

  @GetMapping
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getById(
      @NotNull @RequestParam("cQue") Long cQue,
      @NotNull @RequestParam("dateIns") Long dateIns,
      HttpServletRequest sr) {

    log.info("... cerco la query per id");

    try {
      return temi15Service.getByPk(cQue, dateIns).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @GetMapping
  @RequestMapping("db")
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getByDb(
      @NotNull @RequestParam("schema") String schema,
      @NotNull @RequestParam("type") String type,
      HttpServletRequest sr) {

    log.info("... cerco la query per db");

    try {
      String[] ignorableFieldNames = {"tjson"};

      return WrappedResponse.<List<Temi15UteQue>>baseBuilder()
          .entity(temi15Service.getByDb(schema, type))
          .build()
          .setIgnorableFields(ignorableFieldNames)
          .setResponse()
          .getResponse(sr);

    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping
  @RequestMapping("tipCateg")
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getByTipCateg(
      @RequestParam(value = "tipCat", required = false) List<String> listAllowedCat,
      @RequestBody(required = false) List<Temi15UteQue> queries,
      HttpServletRequest sr) {

    try {
      return temi15Service.getByTipCateg(listAllowedCat, queries, sr).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @GetMapping
  @RequestMapping("associatedQuery")
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getAlreadyAssociatedQuery(
      @NotNull @RequestParam("tipCat") String tipCat,
      @NotNull @RequestParam("cat") Long ccat,
      @NotNull @RequestParam("insCat") Long insCat,
      HttpServletRequest sr) {

    try {
      return temi15Service.getAlreadyAssociatedQuery(ccat, insCat, tipCat).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping
  @RolesAllowed({F_QRQINS, R_FEPQRA})
  public ResponseEntity<String> postQuery(
      @NotNull @RequestBody Temi15UteQue que, HttpServletRequest sr) {

    log.info("... salvo una nuova query");

    try {
      return temi15Service.postQuery(que, sr.getLocale().toString()).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }

  @PostMapping
  @RolesAllowed({R_FEPQRA, F_QRQMOD, F_QRCMOD, R_FEPQRA})
  @RequestMapping("delete")
  public ResponseEntity<String> deleteQuery(
      @NotNull @RequestBody Temi15UteQueId key, HttpServletRequest sr) {

    log.info("... cancello associazione categorie - query");

    try {
      return temi15Service.deleteQuery(key).getResponse(sr);
    } catch (Exception exception) {
      return WrappedResponse.<Long>baseBuilder()
          .exception(exception)
          .build()
          .processException()
          .getResponse(sr);
    }
  }
}

package it.cambi.qrgui.rest;

import static it.cambi.qrgui.util.Constants.F_QRCMOD;
import static it.cambi.qrgui.util.Constants.F_QRQE00;
import static it.cambi.qrgui.util.Constants.F_QRQINS;
import static it.cambi.qrgui.util.Constants.F_QRQMOD;
import static it.cambi.qrgui.util.Constants.R_FEPQRA;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/emia/query")
@Component
@RequiredArgsConstructor
@Slf4j
public class QueryResource extends BasicResource {
  private final ITemi15Service<Temi15UteQue> temi15Service;

  @GetMapping
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getById(
      @RequestParam("cQue") Long cQue,
      @RequestParam("dateIns") Long dateIns,
      HttpServletRequest sr) {
    log.info("... cerco la query per id");
    return temi15Service.getByPk(cQue, dateIns).getResponse(sr);
  }

  @GetMapping
  @RequestMapping("db")
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getByDb(
      @RequestParam("schema") String schema,
      @RequestParam("type") String type,
      HttpServletRequest sr) {
    log.info("... cerco la query per db");
    String[] ignorableFieldNames = {"tjson"};

    return WrappedResponse.<List<Temi15UteQue>>baseBuilder()
        .entity(temi15Service.getByDb(schema, type))
        .build()
        .setIgnorableFields(ignorableFieldNames)
        .setResponse()
        .getResponse(sr);
  }

  @PostMapping
  @RequestMapping("tipCateg")
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getByTipCateg(
      @RequestParam(value = "tipCat", required = false) List<String> listAllowedCat,
      @RequestBody(required = false) List<Temi15UteQue> queries,
      HttpServletRequest sr) {
    return temi15Service.getByTipCateg(listAllowedCat, queries, sr).getResponse(sr);
  }

  @GetMapping
  @RequestMapping("associatedQuery")
  @RolesAllowed({F_QRQE00, R_FEPQRA})
  public ResponseEntity<String> getAlreadyAssociatedQuery(
      @RequestParam("tipCat") String tipCat,
      @RequestParam("cat") Integer ccat,
      @RequestParam("insCat") Long insCat,
      HttpServletRequest sr) {
    return temi15Service.getAlreadyAssociatedQuery(ccat, insCat, tipCat).getResponse(sr);
  }

  @PostMapping
  @RolesAllowed({F_QRQINS, R_FEPQRA})
  public ResponseEntity<String> postQuery(@RequestBody Temi15UteQue que, HttpServletRequest sr) {
    log.info("... salvo una nuova query");
    return temi15Service.postQuery(que, sr.getLocale().toString()).getResponse(sr);
  }

  @PostMapping
  @RolesAllowed({R_FEPQRA, F_QRQMOD, F_QRCMOD, R_FEPQRA})
  @RequestMapping("delete")
  public ResponseEntity<String> deleteQuery(
      @RequestBody Temi15UteQueId key, HttpServletRequest sr) {
    log.info("... cancello associazione categorie - query");
    return temi15Service.deleteQuery(key).getResponse(sr);
  }
}

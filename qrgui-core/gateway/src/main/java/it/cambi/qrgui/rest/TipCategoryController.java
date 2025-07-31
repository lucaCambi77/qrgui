package it.cambi.qrgui.rest;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG01;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRE00;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.client.FeignClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/emia/anaTipCat")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TipCategoryController extends BasicController {
  private final FeignClient feignClient;

  @GetMapping
  @PreAuthorize(
      "hasAnyAuthority('"
          + F_QRCG01
          + "','"
          + F_QRCG00
          + "','"
          + F_QRRE00
          + "', '"
          + R_FEPQRA
          + "')")
  public ResponseEntity<WrappedResponse<?>> getAnaTipCat(
      Authentication authentication, HttpServletRequest sr) {
    log.info("... cerco tutti i tipi categoria");
    return getResponse(
        sr,
        () ->
            feignClient.getTipCategory(
                authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList()));
  }
}

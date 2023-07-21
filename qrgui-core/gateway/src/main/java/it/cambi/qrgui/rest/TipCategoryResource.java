package it.cambi.qrgui.rest;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG01;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRRE00;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/emia/anaTipCat")
@Component
@Slf4j
@RequiredArgsConstructor
public class TipCategoryResource extends BasicResource {
  private final RestTemplate restTemplate;

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
            restTemplate.getForObject(
                UriComponentsBuilder.fromHttpUrl(servicesUrl + "anaTipCat")
                    .queryParam("tipCateg", authentication.getAuthorities())
                    .build()
                    .toString(),
                WrappedResponse.class));
  }
}

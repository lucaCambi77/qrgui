package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.api.user.RolesFunctions.*;

@RequestMapping("/emia/anaTipCat")
@Component
@Slf4j
@RequiredArgsConstructor
public class TipCategoryResource extends BasicResource {
  private final RestTemplate restTemplate;

  @GetMapping
  @RolesAllowed({F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA})
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

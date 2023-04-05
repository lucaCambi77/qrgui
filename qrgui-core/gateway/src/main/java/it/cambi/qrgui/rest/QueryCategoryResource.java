package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.QueCatAssDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.cambi.qrgui.api.user.RolesFunctions.*;

@RequestMapping("/emia/queCatAssoc")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryCategoryResource extends BasicResource {

    private final RestTemplate restTemplate;

    @GetMapping
    @RolesAllowed({F_QRCG00, F_QRCG01, F_QRQE00, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> getQueCatAssoc(
            Authentication authentication, HttpServletRequest sr) {
        log.info("... cerco tutte le associazioni categorie - query");

        return getResponse(
                sr,
                () ->
                        restTemplate.getForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "queCatAssoc")
                                        .queryParam("tipCateg", authentication.getAuthorities())
                                        .build()
                                        .toString(),
                                WrappedResponse.class));
    }

    @PostMapping
    @RequestMapping("post")
    @RolesAllowed({R_FEPQRA, F_QRQMOD, F_QRCMOD})
    public ResponseEntity<WrappedResponse<?>> addQueriesToCategory(
            @RequestBody List<QueCatAssDto> temi16, HttpServletRequest sr) {
        log.info("... aggiungo le queries alla categoria ");

        return getResponse(
                sr,
                () -> restTemplate.postForObject(servicesUrl + "queCatAssoc", temi16, WrappedResponse.class));
    }
}

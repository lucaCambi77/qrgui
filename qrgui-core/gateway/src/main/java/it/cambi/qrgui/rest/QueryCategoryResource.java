package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.QueCatAssDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCG01;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCMOD;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQE00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQMOD;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/emia/queCatAssoc")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryCategoryResource extends BasicResource {

    private final RestTemplate restTemplate;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + F_QRCG00 + "', '" + F_QRCG01 + "', '" + F_QRQE00 + "', '" + R_FEPQRA + "')")
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
    @PreAuthorize("hasAnyAuthority('" + F_QRQMOD + "', '" + F_QRCMOD + "', '" + R_FEPQRA + "')")
    public ResponseEntity<WrappedResponse<?>> addQueriesToCategory(
            @RequestBody List<QueCatAssDto> temi16, HttpServletRequest sr) {
        log.info("... aggiungo le queries alla categoria ");

        return getResponse(
                sr,
                () -> restTemplate.postForObject(servicesUrl + "queCatAssoc", temi16, WrappedResponse.class));
    }
}

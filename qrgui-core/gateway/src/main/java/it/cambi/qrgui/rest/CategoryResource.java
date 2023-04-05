package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.CategoryDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.api.user.RolesFunctions.*;

@RequestMapping("/emia/category")
@Slf4j
@RequiredArgsConstructor
@RestController
public class CategoryResource extends BasicResource {

    private final RestTemplate restTemplate;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({F_QRCG00, F_QRCG01, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> getCategories(
            Authentication authentication, HttpServletRequest sr) {
        log.info("... cerco tutte le categorie");

        return getResponse(
                sr,
                () ->
                        restTemplate.getForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "category")
                                        .queryParam("tipCateg", authentication.getAuthorities())
                                        .build()
                                        .toString(),
                                WrappedResponse.class));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({F_QRCINS, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> postCategory(
            @RequestBody CategoryDto temi14, HttpServletRequest sr, Authentication authentication) {
        log.info("... creo una nuova categoria");

        return getResponse(
                sr,
                () -> restTemplate.postForObject(
                        UriComponentsBuilder.fromHttpUrl(servicesUrl + "category")
                                .queryParam("tipCateg", authentication.getAuthorities())
                                .build()
                                .toString(),
                        temi14,
                        WrappedResponse.class));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("delete")
    @RolesAllowed({F_QRCMOD, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> deleteCategory(
            @RequestBody CategoryDto categoryDeleteDto, HttpServletRequest sr, Authentication authentication) {
        log.info("... cancello categoria");

        return getResponse(
                sr,
                () ->
                        restTemplate.postForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "category/delete"
                                        )
                                        .queryParam("tipCateg", authentication.getAuthorities())
                                        .build()
                                        .toString(),
                                categoryDeleteDto,
                                WrappedResponse.class));
    }
}

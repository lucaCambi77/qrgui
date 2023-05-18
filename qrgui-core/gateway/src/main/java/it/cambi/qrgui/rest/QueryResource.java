package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.model.UteQueId;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static it.cambi.qrgui.api.user.RolesFunctions.F_QRCMOD;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQE00;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQINS;
import static it.cambi.qrgui.api.user.RolesFunctions.F_QRQMOD;
import static it.cambi.qrgui.api.user.RolesFunctions.R_FEPQRA;

@RequestMapping("/emia/query")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryResource extends BasicResource {
    private final RestTemplate restTemplate;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("associatedQuery")
    @RolesAllowed({F_QRQE00, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> getAlreadyAssociatedQuery(
            @RequestParam("tipCat") String tipCat,
            @RequestParam("cat") Integer cat,
            @RequestParam("insCat") String insCat,
            HttpServletRequest sr) {

        return getResponse(
                sr,
                () ->
                        restTemplate.getForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "query/associatedQuery")
                                        .queryParam("tipCat", tipCat)
                                        .queryParam("cat", cat)
                                        .queryParam("insCat", insCat)
                                        .build()
                                        .toString(),
                                WrappedResponse.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("tipCateg")
    @RolesAllowed({F_QRQE00, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> getByTipCateg(
            @RequestParam(value = "tipCat", required = false) List<String> tipCatInput,
            @RequestBody(required = false) List<UteQueDto> queries,
            HttpServletRequest sr,
            Authentication authentication) {

        return getResponse(
                sr,
                () ->
                        restTemplate.postForObject(
                                UriComponentsBuilder.fromHttpUrl(servicesUrl + "query/tipCateg")
                                        .queryParam("tipCat", authentication.getAuthorities())
                                        .queryParam("tipCatInput", tipCatInput)
                                        .build()
                                        .toString(),
                                queries == null ? List.of() : queries,
                                WrappedResponse.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({F_QRQINS, R_FEPQRA})
    public ResponseEntity<WrappedResponse<?>> postQuery(
            @RequestBody UteQueDto que, HttpServletRequest sr) {
        log.info("... salvo una nuova query");

        return getResponse(
                sr, () -> restTemplate.postForObject(servicesUrl + "query", que, WrappedResponse.class));
    }

    @PostMapping
    @RolesAllowed({R_FEPQRA, F_QRQMOD, F_QRCMOD, R_FEPQRA})
    @RequestMapping("delete")
    public ResponseEntity<WrappedResponse<?>> deleteQuery(
            @RequestBody UteQueId key, HttpServletRequest sr) {
        log.info("... cancello associazione categorie - query");

        return getResponse(
                sr,
                () -> restTemplate.postForObject(servicesUrl + "query/delete", key, WrappedResponse.class));
    }
}

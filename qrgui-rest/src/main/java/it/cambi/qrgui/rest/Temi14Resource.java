package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cambi.qrgui.services.db.model.Temi14UteCat;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static it.cambi.qrgui.services.util.IConstants.*;

@Scope("request")
@RequestMapping("/emia/category")
@Component
public class Temi14Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi14Resource.class);

    @Autowired
    private ITemi14Service<Temi14UteCat> temi14Service;

    @GetMapping
    @RolesAllowed({ F_QRCG00, F_QRCG01, R_FEPQRA })
    public ResponseEntity<String> getCategories( HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cerco tutte le categorie");

        try
        {
            return temi14Service.findAll(sr, null).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RolesAllowed({ F_QRCINS, R_FEPQRA })
    public ResponseEntity<String> postCategory(@NotNull Temi14UteCat temi14, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... creo una nuova categoria");

        try
        {

            return temi14Service.saveCategory(sr, temi14).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RequestMapping("delete")
    @RolesAllowed({ F_QRCMOD, R_FEPQRA })
    public ResponseEntity<String> deleteCategory(@NotNull Temi14UteCat id, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cancello categoria");

        try
        {
            return temi14Service.deleteCategory(sr, id).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }
}

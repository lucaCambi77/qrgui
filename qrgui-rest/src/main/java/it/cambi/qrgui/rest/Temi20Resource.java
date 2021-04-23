package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cambi.qrgui.services.db.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import it.cambi.qrgui.services.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import static it.cambi.qrgui.services.util.IConstants.*;


@Scope("request")
@RequestMapping("/emia/anaTipCat")
@Component
public class Temi20Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi20Resource.class);

    @Autowired
    ITemi20Service<Temi20AnaTipCat> temi20Service;

    @GetMapping
    @RolesAllowed({ F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA })
    public ResponseEntity getAnaTipCat(@RequestParam("cque") Long cque, @RequestParam("crou") Long crou, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cerco tutti i tipi categoria");

        try
        {

            return temi20Service.getByCategory(sr).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

}

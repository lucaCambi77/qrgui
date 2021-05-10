package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cambi.qrgui.services.db.model.Temi16QueCatAss;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

import static it.cambi.qrgui.util.IConstants.*;

@RequestMapping("/emia/queCatAssoc")
@Component
public class Temi16Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi16Resource.class);

    @Autowired
    ITemi16Service<Temi16QueCatAss> temi16Service;

    @GetMapping
    @RolesAllowed({ F_QRCG00, F_QRCG01, F_QRQE00, R_FEPQRA })
    public ResponseEntity<String> getQueCatAssoc(HttpServletRequest request, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cerco tutte le associazioni categorie - query");

        try
        {
            return temi16Service.findByCategory(request).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RequestMapping("post")
    @RolesAllowed({ R_FEPQRA, F_QRQMOD, F_QRCMOD })
    public ResponseEntity<String> addQueriesToCateg(@NotNull @RequestBody List<Temi16QueCatAss> temi16, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... aggiungo le queries alla categoria ");

        try
        {
            return temi16Service.addQueriesToCateg(temi16).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }
}

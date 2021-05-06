package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cambi.qrgui.services.db.model.Temi17UteRou;
import it.cambi.qrgui.services.db.model.Temi17UteRouId;
import it.cambi.qrgui.services.emia.api.ITemi17Service;
import it.cambi.qrgui.util.IConstants;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static it.cambi.qrgui.util.IConstants.*;

@RequestMapping("/emia/routine")
@Component
public class Temi17Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi17Resource.class);

    @Autowired
    ITemi17Service<Temi17UteRou> temi17Service;

    @GetMapping
    @RolesAllowed({ F_QRRE00, F_QRCG00, F_QRCG01, R_FEPQRA })
    public ResponseEntity<String> getRoutines(HttpServletRequest request, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cerco tutte le routines");

        try
        {
            return temi17Service.findAll(request).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({ F_QRRINS, R_FEPQRA })
    public ResponseEntity<String> postRoutine(@NotNull Temi17UteRou temi17, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... creo una nuova routine");

        try
        {
            return temi17Service.merge(temi17).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("delete")
    @RolesAllowed({IConstants.F_QRRMOD, R_FEPQRA })
    public ResponseEntity<String> deleteRoutine(@NotNull Temi17UteRouId crou, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cancella la routine " + crou);

        try
        {
            return temi17Service.delete(crou).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }
}

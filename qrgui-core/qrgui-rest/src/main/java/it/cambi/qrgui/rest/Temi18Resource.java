package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cambi.qrgui.services.db.model.Temi18RouQue;
import it.cambi.qrgui.services.db.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi18Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import static it.cambi.qrgui.util.IConstants.F_QRRINS;
import static it.cambi.qrgui.util.IConstants.R_FEPQRA;

@RequestMapping("/emia/routQuery")
@Component
public class Temi18Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi18Resource.class);

    @Autowired
    ITemi18Service<Temi18RouQue> temi18Service;

    @PostMapping
    @RolesAllowed({ F_QRRINS, R_FEPQRA })
    public ResponseEntity<String> postQueRoutine(@NotNull Temi18RouQueId temi18Pk, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... creo una nuova routine");

        try
        {
            return temi18Service.merge(temi18Pk).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RequestMapping("delete")
    @RolesAllowed({ F_QRRINS, R_FEPQRA })
    public ResponseEntity<String> deleteQueRoutineAssoc(@NotNull Temi18RouQueId temi18Pk, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... elimino assciazione routine della query " + temi18Pk.getQue());

        try
        {
            return temi18Service.deleteQueRoutineAssoc(temi18Pk).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }
}

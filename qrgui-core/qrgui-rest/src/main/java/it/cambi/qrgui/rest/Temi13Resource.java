package it.cambi.qrgui.rest;

import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.services.emia.api.ITemi13Service;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static it.cambi.qrgui.util.IConstants.*;

@RequestMapping("/emia/dbInfo")
@Component
public class Temi13Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi13Resource.class);

    @Autowired
    ITemi13Service<Temi13DtbInf> databaseInfoService;

    @GetMapping
    @RolesAllowed({ R_FEPQRA, R_FEPQR1, R_FEPQR2 })
    public ResponseEntity<String> getDatabaseInfoList(HttpServletRequest sr) throws IOException
    {

        log.info("Recupero informazioni degli schema utilizzati dall'applicazione ...");

        try
        {

            return databaseInfoService.findAll().getResponse(sr);

        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

}

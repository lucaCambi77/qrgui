package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cambi.qrgui.services.db.model.Temi15UteQue;
import it.cambi.qrgui.services.db.model.Temi15UteQueId;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

import static it.cambi.qrgui.services.util.IConstants.*;

@Scope("request")
@RequestMapping("/emia/query")
@Component
public class Temi15Resource extends BasicResource
{
    private static final Logger log = LoggerFactory.getLogger(Temi14Resource.class);

    @Autowired
    ITemi15Service<Temi15UteQue> temi15Service;

    @GetMapping
    @RolesAllowed({ F_QRQE00, R_FEPQRA })
    public ResponseEntity<String> getById(@NotNull @RequestParam("cQue") Long cQue, @NotNull @RequestParam("dateIns") Long dateIns, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cerco la query per id");

        try
        {
            return temi15Service.getByPk(cQue, dateIns).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @GetMapping
    @RequestMapping("db")
    @RolesAllowed({ F_QRQE00, R_FEPQRA })
    public ResponseEntity<String> getByDb(@NotNull @RequestParam("schema") String schema, @NotNull @RequestParam("type") String type, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cerco la query per db");

        try
        {
            String[] ignorableFieldNames = { "tjson" };

            return new WrappedResponse<List<Temi15UteQue>>().setIgnorableFields(ignorableFieldNames).setEntity(temi15Service.getByDb(schema, type))
                    .setResponse().getResponse(sr);

        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RequestMapping("tipCateg")
    @RolesAllowed({ F_QRQE00, R_FEPQRA })
    public ResponseEntity<String> getByTipCateg(@RequestParam("tipCat") List<String> listAllowedCat, List<Temi15UteQue> queries, HttpServletRequest sr)
            throws JsonProcessingException
    {

        try
        {
            return temi15Service.getByTipCateg(listAllowedCat, queries, sr).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @GetMapping
    @RequestMapping("associatedQuery")
    @RolesAllowed({ F_QRQE00, R_FEPQRA })
    public ResponseEntity<String> getAlreadyAssociatedQuery(@NotNull @RequestParam("tipCat") String tipCat, @NotNull @RequestParam("cat") Long ccat,
                                              @NotNull @RequestParam("insCat") Long insCat, HttpServletRequest sr)
            throws JsonProcessingException
    {

        try
        {
            return temi15Service.getAlreadyAssociatedQuery(ccat, insCat, tipCat).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RolesAllowed({ F_QRQINS, R_FEPQRA })
    public ResponseEntity<String> postQuery(@NotNull Temi15UteQue que, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... salvo una nuova query");

        try
        {
            return temi15Service.postQuery(que, sr.getLocale().toString()).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RolesAllowed({ R_FEPQRA, F_QRQMOD, F_QRCMOD, R_FEPQRA })
    @RequestMapping("delete")
    public ResponseEntity<String> deleteQuery(@NotNull Temi15UteQueId key, HttpServletRequest sr)
            throws JsonProcessingException
    {

        log.info("... cancello associazione categorie - query");

        try
        {
            return temi15Service.deleteQuery(key).getResponse(sr);
        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

}

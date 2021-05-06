package it.cambi.qrgui.session;

import it.cambi.qrgui.response.model.ErtaGuiUser;
import it.cambi.qrgui.response.model.ErtaQrGuiRoles;
import it.cambi.qrgui.rest.BasicResource;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static it.cambi.qrgui.util.IConstants.*;

@Component
@RequestMapping("/userProperties")
public class UserPropertiesResource extends BasicResource
{

    private static final Logger log = LoggerFactory.getLogger(UserPropertiesResource.class);

    @PostMapping
    @RolesAllowed({ R_FEPQRA, R_FEPQR1, R_FEPQR2 })
    public ResponseEntity<String> getUserPrincipal(@NotNull ErtaGuiUser user, HttpServletRequest sr)
    {

        if (null != user.getUrl() && user.getUrl().contains("localhost"))
        {
            user.setUserName("Localhost");
            user.addToErtaQrGuiRoles(ErtaQrGuiRoles.FEPQRA);
            return new WrappedResponse<ErtaGuiUser>().setEntity(user).setCount(1).setResponse().getResponse(sr);
        }

        try
        {
            user.setUserName(null == sr.getUserPrincipal() ? "" : sr.getUserPrincipal().getName());

            for (ErtaQrGuiRoles role : ErtaQrGuiRoles.values())
            {
                if (sr.isUserInRole(role.getRole()))
                {
                    user.addToErtaQrGuiRoles(role);
                    log.info("Utente " + sr.getUserPrincipal().getName() + " ha il ruolo di " + role.getRole());
                }
            }

            String[] ignorableFieldNames = { "password" };

            return new WrappedResponse<ErtaGuiUser>().setIgnorableFields(ignorableFieldNames).setEntity(user).setCount(1).setResponse()
                    .getResponse(sr);

        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<String> login(ErtaGuiUser user, HttpServletRequest sr) throws IOException
    {

        // log.debug("E' stata invocata la login con utente " + ((user == null) ? "N.A." : user.getUserName()));

        if (null != user.getUrl() && user.getUrl().contains("localhost"))
        {
            user.setGuiVersion("Development");
            return new WrappedResponse<ErtaGuiUser>().setEntity(user).setCount(1).setResponse().getResponse(sr);
        }

        try
        {

            /*
             * if (user.getUrl().contains(LOCALHOST)) { log.debug( "Utilizzo l'utente fittizio " + DEVELOPER); user.setUserName(DEVELOPER); } else
             */
            {
                // log.debug("Effettuo il login con username " + user.getUserName() + " e password " + user.getPassword());

                /* Invalido la sessione se per qualche motivo è rimasta attiva */
                // HttpSession session = request.getSession(false);
                // if (null != session)
                // session.invalidate();

                /**
                 * La login non serve più perchè l'utente arriverà sulla Gui già autenticato, come da passaggio all'autenticazione su Tam che tramite
                 * l'header iv-user si autentica su weblogic e mantiene il principal durante la sessione. Nel caso di sessione scaduta o logout
                 * l'utente sarà indirizzata alla pagina di login del Tam
                 */
                // request.login(user.getUserName(), user.getPassword());

                for (ErtaQrGuiRoles role : ErtaQrGuiRoles.values())
                {
                    if (sr.isUserInRole(role.getRole()))
                        log.info("Utente " + sr.getUserPrincipal().getName() + " ha il ruolo di " + role.getRole());
                }

            }

            String[] ignorableFieldNames = { "password" };

            return new WrappedResponse<ErtaGuiUser>().setIgnorableFields(ignorableFieldNames).setEntity(user).setCount(1).setResponse()
                    .getResponse(sr);

        }
        catch (Exception exception)
        {
            return new WrappedResponse<Long>(exception).processException().getResponse(sr);
        }

    }
}

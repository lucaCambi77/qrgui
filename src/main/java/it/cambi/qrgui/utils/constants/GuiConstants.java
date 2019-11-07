/**
 * 
 */
package it.cambi.qrgui.utils.constants;

/**
 * @author luca
 *
 */
public class GuiConstants
{

    public final String POM_PROPERTIES_PATH = "project.properties";
    public final String IS_AUTH_REQUIRED = "is.auth.required";

    // Servlet
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    // Gmt error
    public static final String ERROR_GMT_NOT_SET = "ERROR.GMT.NOT.SET";

    public final String POM_VERSION = "version";

    public final String LOCALHOST = "localhost";
    public final String SESSION = "session";
    public final String LOGIN = "login";

    public final String DEVELOPER = "Developer";
    public final String ERTAQRGUISUSER = "ertaQrGuiUser";

    /**
     * property che viene utilizzata per indicare il persistence xml da usare nei test perchè altrimenti va in conflitto con quello dei services
     */
    public final String TEST_PERSISTENCE_LOCATION = "test.persitence.xml.location";
}

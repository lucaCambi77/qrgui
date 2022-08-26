/**
 * Questo è l'oggetto user che viene usato per loggare la request dell'utente. Ogni chiamata rest
 * che viene effettuata logga la request e la respone. La request contiente gli attributi di questa
 * classe, in particolare il tipo di richiesta, la url e i parametri usati, oltre al browser ed
 * all'ip. La response viene loggata di seguito alla request nella wrappedResponse. Da notare che
 * questo oggetto viene creato nel @SecurityInterceptor ad ogni chiamata creando ogni volta un
 * contesto di sicurezza con un custom Principal
 */
package it.cambi.qrgui.response.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.istack.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luca
 */
public class ErtaGuiUser implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private String userName;
  private String password;
  private String locale;
  private String url;
  private String queryString;
  private String guiVersion;
  private String requestType;
  private String browser;
  private List<ErtaQrGuiRoles> ertaQrGuiRoles = new ArrayList<>();
  private boolean isAdmin = false;

  private String address;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E MMM d HH:mm:ss.SSS z y")
  private Date requestDate = new Date();

  /**
   * @param request
   * @param user
   */
  public ErtaGuiUser(@NotNull HttpServletRequest request) {
    this.requestType = request.getMethod();
    this.locale = request.getLocale().toString();
    this.userName =
        request.getUserPrincipal() == null ? "N.A." : request.getUserPrincipal().getName();
    this.browser = request.getHeader("user-agent");

    String remoteHost =
        request.getRemoteHost() == null ? "Host sconosciuto" : request.getRemoteHost();
    String remoteAddr =
        request.getRemoteAddr() == null ? "Address sconosciuto" : request.getRemoteAddr();
    int remotePort = request.getRemotePort();

    String url = remoteHost + " (" + remoteAddr + ":" + remotePort + ")";

    String ipAddress = request.getHeader("x-forwarded-for");
    setAddress(ipAddress != null ? request.getRemoteAddr() : url);

    setUrl(request.getRequestURL().toString());
    setQueryString(request.getQueryString());
  }

  @Override
  public String toString() {

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.setSerializationInclusion(Include.NON_NULL);

    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "Errore serializzazione dati utente";
    }
  }

  public ErtaGuiUser() {}

  public ErtaGuiUser(String userName) {
    this.userName = userName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getGuiVersion() {
    return guiVersion;
  }

  public void setGuiVersion(String guiVersion) {
    this.guiVersion = guiVersion;
  }

  public String getQueryString() {
    return queryString;
  }

  public String getAddress() {
    return address;
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public List<String> getErtaQrGuiRoles() {
    List<String> rolesToString = new ArrayList<>();

    ertaQrGuiRoles.forEach((r) -> rolesToString.add(r.getRole()));

    return rolesToString;
  }

  public void setErtaQrGuiRoles(List<ErtaQrGuiRoles> ertaQrGuiRoles) {
    this.ertaQrGuiRoles = ertaQrGuiRoles;
  }

  public void addToErtaQrGuiRoles(String role) {
    this.ertaQrGuiRoles.add(ErtaQrGuiRoles.fromString(role));
  }

  public void addToErtaQrGuiRoles(ErtaQrGuiRoles role) {
    this.ertaQrGuiRoles.add(role);
  }

  public boolean isAdmin() {
    if (getErtaQrGuiRoles().contains(ErtaQrGuiRoles.FEPQRA.getRole())) return true;

    return isAdmin;
  }

  public void setAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }
}

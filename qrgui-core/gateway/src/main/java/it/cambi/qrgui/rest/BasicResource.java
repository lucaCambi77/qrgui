package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.user.ErtaGuiUser;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.api.wrappedResponse.XWrappedResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@Slf4j
public abstract class BasicResource {

  @Value("${services.contextPath}")
  protected String servicesUrl;

  @Value("${multitenant.contextPath}")
  protected String multiTenantUrl;

  protected ResponseEntity<WrappedResponse<?>> getResponse(
      HttpServletRequest sr, Supplier<WrappedResponse<?>> supplier) {

    logRequestInfo(sr);

    WrappedResponse<?> wrappedResponse = supplier.get();

    return wrappedResponse.isSuccess()
        ? ResponseEntity.ok(wrappedResponse)
        : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(wrappedResponse);
  }

  protected ResponseEntity<WrappedResponse<?>> getXResponse(
      HttpServletRequest sr,
      Supplier<XWrappedResponse<UteQueDto, List<Object>>[]> wrappedResponses) {

    logRequestInfo(sr);

    XWrappedResponse<UteQueDto, List<Object>>[] responses = wrappedResponses.get();

    List<XWrappedResponse<UteQueDto, List<Object>>> errors =
        Arrays.stream(responses).filter(r -> !r.isSuccess()).toList();

    return !errors.isEmpty()
        ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(WrappedResponse.baseBuilder().entity(errors).build())
        : ResponseEntity.ok(WrappedResponse.baseBuilder().entity(responses).build());
  }

  public void logRequestInfo(HttpServletRequest sr) {

    String builder =
        new StringBuilder()
            .append("Request  ----> ")
            .append(getRequestUser(sr))
            .append("\n")
            .append("Response ----> ")
            .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()))
            .append(", ")
            .append("success --> ")
            // .append(isSuccess())
            .append(", ")
            .append("count --> ")
            // .append(getCount())
            .append("\n")
            .toString();

    log.info(builder);
  }

  private ErtaGuiUser getRequestUser(HttpServletRequest request) {

    String remoteHost =
        request.getRemoteHost() == null ? "Host sconosciuto" : request.getRemoteHost();
    String remoteAddr =
        request.getRemoteAddr() == null ? "Address sconosciuto" : request.getRemoteAddr();
    int remotePort = request.getRemotePort();

    String url = remoteHost + " (" + remoteAddr + ":" + remotePort + ")";
    String ipAddress = request.getHeader("x-forwarded-for");

    return ErtaGuiUser.builder()
        .requestType(request.getMethod())
        .locale(request.getLocale().toString())
        .userName(
            request.getUserPrincipal() == null ? "N.A." : request.getUserPrincipal().getName())
        .browser(request.getHeader("user-agent"))
        .url(url)
        .address(ipAddress != null ? request.getRemoteAddr() : url)
        .url(request.getRequestURL().toString())
        .queryString(request.getQueryString())
        .build();
  }
}

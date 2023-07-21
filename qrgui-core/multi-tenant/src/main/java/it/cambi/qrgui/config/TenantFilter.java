package it.cambi.qrgui.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
class TenantFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String tenant = ((HttpServletRequest) request).getHeader("X-TenantID");
    TenantContext.setCurrentTenant(tenant);

    try {
      chain.doFilter(request, response);
    } finally {
      TenantContext.setCurrentTenant("");
    }
  }
}

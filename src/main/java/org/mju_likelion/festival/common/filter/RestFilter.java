package org.mju_likelion.festival.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.mju_likelion.festival.common.exception.CustomException;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;

public abstract class RestFilter implements Filter {

  @Override
  public void doFilter(
      final ServletRequest request,
      final ServletResponse response,
      final FilterChain chain) throws ServletException, IOException {

    try {
      if (isPassableRequest(request)) {
        chain.doFilter(request, response);
        return;
      }
      process(request);
      chain.doFilter(request, response);
    } catch (CustomException customException) {
      ErrorResponseHandler.writeErrorResponse(response, customException);
      response.getWriter().flush();
    }
  }

  protected abstract void process(final ServletRequest request);

  private boolean isPassableRequest(final ServletRequest request) {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return isOptionsRequest(httpRequest) || !isRequestNotPermitted(httpRequest);
  }

  private boolean isOptionsRequest(final HttpServletRequest request) {
    return CorsUtils.isPreFlightRequest(request);
  }

  private boolean isRequestNotPermitted(final HttpServletRequest request) {
    return getNotAllowedRequestMatchers().stream()
        .anyMatch(matcher -> matcher.matches(request));
  }

  protected List<RequestMatcher> getNotAllowedRequestMatchers() {
    return new ArrayList<>();
  }
}

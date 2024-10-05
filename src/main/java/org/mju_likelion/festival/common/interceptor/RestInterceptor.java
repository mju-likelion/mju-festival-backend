package org.mju_likelion.festival.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class RestInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler) {

    if (isPassableRequest(request)) {
      return true;
    }

    return doInternal(request);
  }

  protected abstract boolean doInternal(HttpServletRequest request);

  private boolean isPassableRequest(final HttpServletRequest request) {
    return CorsUtils.isPreFlightRequest(request) || isPermittedApi(request);
  }

  abstract protected boolean isPermittedApi(final HttpServletRequest request);
}

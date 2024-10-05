package org.mju_likelion.festival.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestIdInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler) {

    String requestId = UUID.randomUUID().toString();
    request.setAttribute("requestId", requestId);
    return true;
  }

  @Override
  public void afterCompletion(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler,
      final Exception ex) {

    request.removeAttribute("requestId");
  }
}

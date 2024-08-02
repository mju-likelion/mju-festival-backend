package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.JWT_NOT_FOUND_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.authentication.AuthorizationExtractor;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

  private final AuthenticationContext authenticationContext;
  private final JwtUtil userJwtUtil;

  protected AbstractAuthenticationInterceptor(AuthenticationContext authenticationContext,
      JwtUtil userJwtUtil) {
    this.authenticationContext = authenticationContext;
    this.userJwtUtil = userJwtUtil;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String accessToken = AuthorizationExtractor.extract(request)
        .orElseThrow(() -> new UnauthorizedException(JWT_NOT_FOUND_ERROR));
    Payload payload = userJwtUtil.getPayload(accessToken);

    if (!isAuthorized(payload)) {
      throw new ForbiddenException(getErrorType());
    }

    authenticationContext.setPrincipal(payload.getId());
    return true;
  }

  protected abstract boolean isAuthorized(Payload payload);

  protected abstract ErrorType getErrorType();
}


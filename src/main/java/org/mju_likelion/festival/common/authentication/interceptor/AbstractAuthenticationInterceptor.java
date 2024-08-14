package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.JWT_NOT_FOUND_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.authentication.AuthorizationExtractor;
import org.mju_likelion.festival.common.config.RequestMatcher;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

  private final AuthenticationContext authenticationContext;
  private final JwtUtil userJwtUtil;

  protected AbstractAuthenticationInterceptor(final AuthenticationContext authenticationContext,
      final JwtUtil userJwtUtil) {
    this.authenticationContext = authenticationContext;
    this.userJwtUtil = userJwtUtil;
  }

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler) {

    if (isPassableRequest(request)) {
      return true;
    }

    String accessToken = AuthorizationExtractor.extract(request)
        .orElseThrow(() -> new UnauthorizedException(JWT_NOT_FOUND_ERROR));
    Payload payload = userJwtUtil.getPayload(accessToken);

    if (!isAuthorized(payload)) {
      throw new ForbiddenException(getErrorType());
    }

    authenticationContext.setPrincipal(payload.getId());
    return true;
  }

  private boolean isPassableRequest(final HttpServletRequest request) {
    return isOptionsRequest(request) || isRequestPermitted(request);
  }

  private boolean isOptionsRequest(final HttpServletRequest request) {
    return Objects.equals(request.getMethod(), "OPTIONS");
  }

  /**
   * HTTP 메서드에 따른 요청 허용 여부를 판단한다.
   *
   * @param request 요청
   * @return 허용 여부
   */
  private boolean isRequestPermitted(final HttpServletRequest request) {
    return getAllowedRequestMatchers().stream()
        .anyMatch(matcher -> matcher.matches(request));
  }

  /**
   * 허용 요청 매처 목록을 반환한다. 오버라이딩하지 않는다면 빈 목록을 반환한다.
   *
   * @return 요청 매처 목록
   */
  protected List<RequestMatcher> getAllowedRequestMatchers() {
    return new ArrayList<>();
  }

  protected abstract boolean isAuthorized(final Payload payload);

  protected abstract ErrorType getErrorType();
}


package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.JWT_NOT_FOUND_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.authentication.AuthorizationExtractor;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.mju_likelion.festival.common.interceptor.RestInterceptor;
import org.mju_likelion.festival.common.util.request_matcher.AllowedRequestMatcherProvider;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;

public abstract class AbstractAuthenticationInterceptor extends RestInterceptor implements
    AllowedRequestMatcherProvider {

  private final AuthenticationContext authenticationContext;
  private final JwtUtil userJwtUtil;

  protected AbstractAuthenticationInterceptor(final AuthenticationContext authenticationContext,
      final JwtUtil userJwtUtil) {
    this.authenticationContext = authenticationContext;
    this.userJwtUtil = userJwtUtil;
  }

  @Override
  protected boolean doInternal(final HttpServletRequest request) {
    String accessToken = AuthorizationExtractor.extract(request)
        .orElseThrow(() -> new UnauthorizedException(JWT_NOT_FOUND_ERROR));
    Payload payload = userJwtUtil.getPayload(accessToken);

    request.setAttribute("userId", payload.getId().toString());

    if (!isAuthorized(payload)) {
      throw new ForbiddenException(getErrorType());
    }

    authenticationContext.setPrincipal(payload.getId());
    return true;
  }

  @Override
  protected boolean isPermittedApi(final HttpServletRequest request) {
    return getAllowedRequestMatchers().stream()
        .anyMatch(matcher -> matcher.matches(request));
  }

  @Override
  public List<RequestMatcher> getAllowedRequestMatchers() {
    return new ArrayList<>();
  }

  protected abstract boolean isAuthorized(final Payload payload);

  protected abstract ErrorType getErrorType();
}


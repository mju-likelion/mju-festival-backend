package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_MANAGER_ONLY_ERROR;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.config.RequestMatcher;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class BoothAdminAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

  public BoothAdminAuthenticationInterceptor(
      final AuthenticationContext authenticationContext,
      final JwtUtil userJwtUtil) {

    super(authenticationContext, userJwtUtil);
  }

  @Override
  protected List<RequestMatcher> getAllowedRequestMatchers() {
    List<RequestMatcher> allowedRequestMatchers = new LinkedList<>();
    allowedRequestMatchers.add(new RequestMatcher(HttpMethod.GET, "/booths/{id}"));

    return allowedRequestMatchers;
  }

  @Override
  protected boolean isAuthorized(Payload payload) {
    return Objects.equals(payload.getRole(), AuthenticationRole.BOOTH_MANAGER);
  }

  @Override
  protected ErrorType getErrorType() {
    return BOOTH_MANAGER_ONLY_ERROR;
  }
}

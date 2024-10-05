package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_ONLY_ERROR;

import java.util.Objects;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

  public AdminAuthenticationInterceptor(
      final AuthenticationContext authenticationContext,
      final JwtUtil userJwtUtil) {

    super(authenticationContext, userJwtUtil);
  }

  @Override
  protected boolean isAuthorized(final Payload payload) {
    return !Objects.equals(payload.getRole(), AuthenticationRole.USER);
  }

  @Override
  protected ErrorType getErrorType() {
    return ADMIN_ONLY_ERROR;
  }
}


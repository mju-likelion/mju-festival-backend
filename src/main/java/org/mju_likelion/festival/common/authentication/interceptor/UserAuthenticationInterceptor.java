package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.USER_ONLY_ERROR;

import java.util.Objects;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

  public UserAuthenticationInterceptor(AuthenticationContext authenticationContext,
      JwtUtil userJwtUtil) {
    super(authenticationContext, userJwtUtil);
  }

  @Override
  protected boolean isAuthorized(Payload payload) {
    return Objects.equals(payload.getRole(), AuthenticationRole.USER);
  }

  @Override
  protected ErrorType getErrorType() {
    return USER_ONLY_ERROR;
  }
}

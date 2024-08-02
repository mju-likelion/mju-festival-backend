package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_MANAGER_ONLY_ERROR;

import java.util.Objects;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.common.authentication.AuthenticationContext;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class BoothAdminAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

  public BoothAdminAuthenticationInterceptor(AuthenticationContext authenticationContext,
      JwtUtil userJwtUtil) {
    super(authenticationContext, userJwtUtil);
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

package org.mju_likelion.festival.common.authentication;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ALREADY_AUTHENTICATED_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.NOT_AUTHENTICATED_ERROR;

import java.util.UUID;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthenticationContext {

  private UUID principal;

  public UUID getPrincipal() {
    if (principal == null) {
      throw new UnauthorizedException(NOT_AUTHENTICATED_ERROR);
    }
    return principal;
  }

  public void setPrincipal(final UUID principal) {
    if (this.principal != null) {
      throw new UnauthorizedException(ALREADY_AUTHENTICATED_ERROR);
    }
    this.principal = principal;
  }
}

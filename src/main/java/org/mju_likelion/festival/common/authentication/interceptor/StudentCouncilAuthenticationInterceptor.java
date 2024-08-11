package org.mju_likelion.festival.common.authentication.interceptor;

import static org.mju_likelion.festival.common.exception.type.ErrorType.STUDENT_COUNCIL_ONLY_ERROR;

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
public class StudentCouncilAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

  public StudentCouncilAuthenticationInterceptor(AuthenticationContext authenticationContext,
      JwtUtil userJwtUtil) {
    super(authenticationContext, userJwtUtil);
  }

  @Override
  protected List<RequestMatcher> getAllowedRequestMatchers() {
    List<RequestMatcher> allowedRequestMatchers = new LinkedList<>();
    allowedRequestMatchers.add(new RequestMatcher(HttpMethod.GET, "/announcements"));
    allowedRequestMatchers.add(new RequestMatcher(HttpMethod.GET, "/announcements/{id}"));

    return allowedRequestMatchers;
  }

  @Override
  protected boolean isAuthorized(Payload payload) {
    return Objects.equals(payload.getRole(), AuthenticationRole.STUDENT_COUNCIL);
  }

  @Override
  protected ErrorType getErrorType() {
    return STUDENT_COUNCIL_ONLY_ERROR;
  }
}

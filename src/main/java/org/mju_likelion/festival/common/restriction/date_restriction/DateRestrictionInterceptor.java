package org.mju_likelion.festival.common.restriction.date_restriction;

import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_LOGIN;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.DATE_RESTRICTED_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import org.mju_likelion.festival.common.exception.ServiceUnavailableException;
import org.mju_likelion.festival.common.interceptor.RestInterceptor;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;
import org.mju_likelion.festival.common.util.request_matcher.RequestRestrictionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class DateRestrictionInterceptor extends RestInterceptor implements
    RequestRestrictionProvider {

  private final LocalDate startDate;
  private final LocalDate endDate;

  public DateRestrictionInterceptor(
      @Value("${application.service.start.date}") String startDateString,
      @Value("${application.service.end.date}") String endDateString) {
    this.startDate = LocalDate.parse(startDateString);
    this.endDate = LocalDate.parse(endDateString);
  }

  @Override
  protected boolean doInternal(HttpServletRequest request) {
    LocalDate currentDate = LocalDate.now();

    if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
      throw new ServiceUnavailableException(DATE_RESTRICTED_ERROR);
    }

    return true;
  }

  @Override
  protected boolean isPermittedApi(HttpServletRequest request) {
    return getNotAllowedRequestMatchers().stream().noneMatch(matcher -> matcher.matches(request));
  }

  @Override
  public List<RequestMatcher> getNotAllowedRequestMatchers() {
    return List.of(
        new RequestMatcher(HttpMethod.POST, USER_LOGIN),
        new RequestMatcher(HttpMethod.GET, ISSUE_BOOTH_QR),
        new RequestMatcher(HttpMethod.POST, VISIT_BOOTH)
    );
  }
}

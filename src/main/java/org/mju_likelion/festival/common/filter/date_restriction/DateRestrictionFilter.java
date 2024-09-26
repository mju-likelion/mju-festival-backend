package org.mju_likelion.festival.common.filter.date_restriction;

import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_LOGIN;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.DATE_RESTRICTED_ERROR;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import java.time.LocalDate;
import java.util.List;
import org.mju_likelion.festival.common.exception.ServiceUnavailableException;
import org.mju_likelion.festival.common.filter.RestFilter;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;
import org.springframework.http.HttpMethod;

public class DateRestrictionFilter extends RestFilter {

  private LocalDate startDate;
  private LocalDate endDate;

  @Override
  public void init(FilterConfig filterConfig) {
    String startDateString = filterConfig.getInitParameter("startDateString");
    String endDateString = filterConfig.getInitParameter("endDateString");

    startDate = LocalDate.parse(startDateString);
    endDate = LocalDate.parse(endDateString);
  }

  @Override
  protected void process(final ServletRequest request) {
    LocalDate currentDate = LocalDate.now();

    if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
      throw new ServiceUnavailableException(DATE_RESTRICTED_ERROR);
    }
  }

  @Override
  protected List<RequestMatcher> getNotAllowedRequestMatchers() {
    return List.of(
        new RequestMatcher(HttpMethod.POST, USER_LOGIN),
        new RequestMatcher(HttpMethod.GET, ISSUE_BOOTH_QR),
        new RequestMatcher(HttpMethod.POST, VISIT_BOOTH)
    );
  }
}

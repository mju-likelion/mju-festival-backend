package org.mju_likelion.festival.common.filter.date_restriction;

import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_LOGIN;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.DATE_RESTRICTED_ERROR;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.mju_likelion.festival.common.exception.ServiceUnavailableException;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;
import org.springframework.http.HttpMethod;

public class DateRestrictionFilter implements Filter {

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
  public void doFilter(
      final ServletRequest request,
      final ServletResponse response,
      final FilterChain chain) throws IOException, ServletException {

    if (isPassableRequest(request)) {
      chain.doFilter(request, response);
      return;
    }
    LocalDate currentDate = LocalDate.now();

    if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
      throw new ServiceUnavailableException(DATE_RESTRICTED_ERROR);
    }
    chain.doFilter(request, response);
  }

  private boolean isPassableRequest(final ServletRequest request) {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return isOptionsRequest(httpRequest) || !isRequestNotPermitted(httpRequest);
  }

  private boolean isOptionsRequest(final HttpServletRequest request) {
    return Objects.equals(request.getMethod(), "OPTIONS");
  }

  private boolean isRequestNotPermitted(final HttpServletRequest request) {
    return getNotAllowedRequestMatchers().stream()
        .anyMatch(matcher -> matcher.matches(request));
  }

  private List<RequestMatcher> getNotAllowedRequestMatchers() {
    return List.of(
        new RequestMatcher(HttpMethod.POST, USER_LOGIN),
        new RequestMatcher(HttpMethod.GET, ISSUE_BOOTH_QR),
        new RequestMatcher(HttpMethod.POST, VISIT_BOOTH)
    );
  }
}

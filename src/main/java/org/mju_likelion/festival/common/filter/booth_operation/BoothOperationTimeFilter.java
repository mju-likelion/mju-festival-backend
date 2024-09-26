package org.mju_likelion.festival.common.filter.booth_operation;

import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_RESTRICTED_ERROR;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import java.time.LocalTime;
import java.util.List;
import org.mju_likelion.festival.common.exception.ServiceUnavailableException;
import org.mju_likelion.festival.common.filter.RestFilter;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;
import org.springframework.http.HttpMethod;

public class BoothOperationTimeFilter extends RestFilter {

  private LocalTime startTime;
  private LocalTime endTime;

  @Override
  public void init(FilterConfig filterConfig) {
    String startTimeString = filterConfig.getInitParameter("startTimeString");
    String endTimeString = filterConfig.getInitParameter("endTimeString");

    startTime = LocalTime.parse(startTimeString);
    endTime = LocalTime.parse(endTimeString);
  }

  @Override
  protected void process(ServletRequest request) {
    LocalTime currentTime = LocalTime.now();

    if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
      throw new ServiceUnavailableException(BOOTH_RESTRICTED_ERROR);
    }
  }

  @Override
  protected List<RequestMatcher> getNotAllowedRequestMatchers() {
    return List.of(
        new RequestMatcher(HttpMethod.GET, ISSUE_BOOTH_QR),
        new RequestMatcher(HttpMethod.POST, VISIT_BOOTH)
    );
  }
}

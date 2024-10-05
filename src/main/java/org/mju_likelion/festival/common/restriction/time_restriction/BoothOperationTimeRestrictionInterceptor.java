package org.mju_likelion.festival.common.restriction.time_restriction;

import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_RESTRICTED_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;
import org.mju_likelion.festival.common.exception.ServiceUnavailableException;
import org.mju_likelion.festival.common.interceptor.RestInterceptor;
import org.mju_likelion.festival.common.util.request_matcher.RequestMatcher;
import org.mju_likelion.festival.common.util.request_matcher.RequestRestrictionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class BoothOperationTimeRestrictionInterceptor extends RestInterceptor implements
    RequestRestrictionProvider {

  private final LocalTime startTime;
  private final LocalTime endTime;

  public BoothOperationTimeRestrictionInterceptor(
      @Value("${booth.open.time}") String boothOpenTimeString,
      @Value("${booth.close.time}") String boothCloseTimeString) {
    this.startTime = LocalTime.parse(boothOpenTimeString);
    this.endTime = LocalTime.parse(boothCloseTimeString);
  }

  @Override
  protected boolean doInternal(HttpServletRequest request) {
    LocalTime currentTime = LocalTime.now();
    if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
      throw new ServiceUnavailableException(BOOTH_RESTRICTED_ERROR);
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
        new RequestMatcher(HttpMethod.GET, ISSUE_BOOTH_QR),
        new RequestMatcher(HttpMethod.POST, VISIT_BOOTH)
    );
  }
}

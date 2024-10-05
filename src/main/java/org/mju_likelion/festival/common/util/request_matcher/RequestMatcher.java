package org.mju_likelion.festival.common.util.request_matcher;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriTemplate;

/**
 * 요청 매칭을 위한 클래스
 */
@AllArgsConstructor
public class RequestMatcher {

  private final HttpMethod method;
  private final String path;

  /**
   * 요청이 매칭되는지 확인한다.
   *
   * @param request 요청
   * @return 매칭 여부
   */
  public boolean matches(final HttpServletRequest request) {
    UriTemplate template = new UriTemplate(path);
    return method.matches(request.getMethod()) && template.matches(request.getRequestURI());
  }
}

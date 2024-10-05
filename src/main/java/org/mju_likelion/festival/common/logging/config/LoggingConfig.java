package org.mju_likelion.festival.common.logging.config;

import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.logging.RequestIdInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Component
@RequiredArgsConstructor
public class LoggingConfig {

  private final RequestIdInterceptor requestIdInterceptor;

  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(requestIdInterceptor)
        .addPathPatterns("/**");
  }
}

package org.mju_likelion.festival.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.logging.RequestIdInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
  private final long MAX_AGE_SECS = 3600;
  private final RequestIdInterceptor requestIdInterceptor;
  @Value("${client.hosts}")
  private List<String> clientHosts;

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(clientHosts.toArray(new String[0]))
        .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(requestIdInterceptor)
        .addPathPatterns("/**");
  }
}

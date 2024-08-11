package org.mju_likelion.festival.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.authentication.AuthenticationArgumentResolver;
import org.mju_likelion.festival.common.authentication.interceptor.AdminAuthenticationInterceptor;
import org.mju_likelion.festival.common.authentication.interceptor.BoothAdminAuthenticationInterceptor;
import org.mju_likelion.festival.common.authentication.interceptor.StudentCouncilAuthenticationInterceptor;
import org.mju_likelion.festival.common.authentication.interceptor.UserAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {

  private final UserAuthenticationInterceptor userAuthenticationInterceptor;
  private final StudentCouncilAuthenticationInterceptor studentCouncilAuthenticationInterceptor;
  private final BoothAdminAuthenticationInterceptor boothAdminAuthenticationInterceptor;
  private final AdminAuthenticationInterceptor adminAuthenticationInterceptor;
  private final AuthenticationArgumentResolver authenticationArgumentResolver;

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    addUserAuthenticationInterceptor(registry);
    addStudentCouncilAuthenticationInterceptor(registry);
    addBoothAdminAuthenticationInterceptor(registry);
    addAdminAuthenticationInterceptor(registry);
  }

  /**
   * 부스 관리자 권한 인증 인터셉터를 추가한다. 부스 관리자만 접근 가능
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addBoothAdminAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(boothAdminAuthenticationInterceptor)
        .addPathPatterns("/booths/{id}/qr");
  }

  /**
   * 학생회 권한 인증 인터셉터를 추가한다. 학생회만 접근 가능
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addStudentCouncilAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(studentCouncilAuthenticationInterceptor)
        .addPathPatterns("/announcements");
  }

  /**
   * 관리자 권한 인증 인터셉터를 추가한다. 관리자만 접근 가능
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addAdminAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(adminAuthenticationInterceptor)
        .addPathPatterns("/images");
  }

  /**
   * 사용자 권한 인증 인터셉터를 추가한다. 로그인한 사용자만 접근 가능 관리자는 접근 불가
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addUserAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(userAuthenticationInterceptor)
        .addPathPatterns("/booths/{qrId}/visit");
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authenticationArgumentResolver);
  }
}

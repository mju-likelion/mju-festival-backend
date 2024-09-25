package org.mju_likelion.festival.common.authentication.config;

import static org.mju_likelion.festival.common.api.ApiPaths.DELETE_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.FOUND_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_BOOTH_MANAGING_DETAIL;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_MY_STAMP;
import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.PATCH_ANNOUNCEMENT;
import static org.mju_likelion.festival.common.api.ApiPaths.PATCH_BOOTH;
import static org.mju_likelion.festival.common.api.ApiPaths.PATCH_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.POST_ANNOUNCEMENT;
import static org.mju_likelion.festival.common.api.ApiPaths.POST_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.SEARCH_LOST_ITEMS;
import static org.mju_likelion.festival.common.api.ApiPaths.UPLOAD_IMAGE;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_WITHDRAW;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.authentication.AuthenticationArgumentResolver;
import org.mju_likelion.festival.common.authentication.interceptor.AdminAuthenticationInterceptor;
import org.mju_likelion.festival.common.authentication.interceptor.BoothAdminAuthenticationInterceptor;
import org.mju_likelion.festival.common.authentication.interceptor.StudentCouncilAuthenticationInterceptor;
import org.mju_likelion.festival.common.authentication.interceptor.UserAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Component
@RequiredArgsConstructor
public class AuthenticationConfig {

  private final UserAuthenticationInterceptor userAuthenticationInterceptor;
  private final StudentCouncilAuthenticationInterceptor studentCouncilAuthenticationInterceptor;
  private final BoothAdminAuthenticationInterceptor boothAdminAuthenticationInterceptor;
  private final AdminAuthenticationInterceptor adminAuthenticationInterceptor;
  private final AuthenticationArgumentResolver authenticationArgumentResolver;

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
        .addPathPatterns(ISSUE_BOOTH_QR)
        .addPathPatterns(PATCH_BOOTH)
        .addPathPatterns(GET_BOOTH_MANAGING_DETAIL);
  }

  /**
   * 학생회 권한 인증 인터셉터를 추가한다. 학생회만 접근 가능
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addStudentCouncilAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(studentCouncilAuthenticationInterceptor)
        .addPathPatterns(POST_ANNOUNCEMENT)
        .addPathPatterns(PATCH_ANNOUNCEMENT)
        .addPathPatterns(DELETE_LOST_ITEM)
        .addPathPatterns(POST_LOST_ITEM)
        .addPathPatterns(PATCH_LOST_ITEM)
        .addPathPatterns(DELETE_LOST_ITEM)
        .addPathPatterns(FOUND_LOST_ITEM)
        .excludePathPatterns(SEARCH_LOST_ITEMS);
  }

  /**
   * 관리자 권한 인증 인터셉터를 추가한다. 관리자만 접근 가능
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addAdminAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(adminAuthenticationInterceptor)
        .addPathPatterns(UPLOAD_IMAGE);
  }

  /**
   * 사용자 권한 인증 인터셉터를 추가한다. 로그인한 사용자만 접근 가능 관리자는 접근 불가
   *
   * @param registry 인터셉터 레지스트리
   */
  private void addUserAuthenticationInterceptor(final InterceptorRegistry registry) {
    registry.addInterceptor(userAuthenticationInterceptor)
        .addPathPatterns(VISIT_BOOTH)
        .addPathPatterns(GET_MY_STAMP)
        .addPathPatterns(USER_WITHDRAW);
  }

  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authenticationArgumentResolver);
  }
}

package org.mju_likelion.festival.common.config;

import org.mju_likelion.festival.common.filter.FrontFilter;
import org.mju_likelion.festival.common.filter.date_restriction.DateRestrictionFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Value("${application.service.end.date}")
  private String endDateString;
  @Value("${application.service.start.date}")
  private String startDateString;

  @Bean
  public FilterRegistrationBean<DateRestrictionFilter> dateRestrictedFilterFilterRegistrationBean() {
    FilterRegistrationBean<DateRestrictionFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new DateRestrictionFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.addInitParameter("startDateString", startDateString);
    registrationBean.addInitParameter("endDateString", endDateString);

    return registrationBean;
  }
}

package org.mju_likelion.festival.common.config;

import org.mju_likelion.festival.common.filter.booth_operation.BoothOperationTimeFilter;
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
  @Value("${booth.open.time}")
  private String boothOpenTimeString;
  @Value("${booth.close.time}")
  private String boothCloseTimeString;

  @Bean
  public FilterRegistrationBean<DateRestrictionFilter> dateRestrictedFilterFilterRegistrationBean() {
    FilterRegistrationBean<DateRestrictionFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new DateRestrictionFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.addInitParameter("startDateString", startDateString);
    registrationBean.addInitParameter("endDateString", endDateString);
    registrationBean.setOrder(0);

    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<BoothOperationTimeFilter> boothOperationFilterFilterRegistrationBean() {
    FilterRegistrationBean<BoothOperationTimeFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new BoothOperationTimeFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.addInitParameter("startTimeString", boothOpenTimeString);
    registrationBean.addInitParameter("endTimeString", boothCloseTimeString);
    registrationBean.setOrder(1);

    return registrationBean;
  }
}

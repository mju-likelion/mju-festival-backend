package org.mju_likelion.festival.common.filter.date_restriction;

import static org.mju_likelion.festival.common.exception.type.ErrorType.DATE_RESTRICTED_ERROR;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import org.mju_likelion.festival.common.exception.ServiceUnavailableException;

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

    LocalDate currentDate = LocalDate.now();

    if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
      throw new ServiceUnavailableException(DATE_RESTRICTED_ERROR);
    }
    chain.doFilter(request, response);
  }
}

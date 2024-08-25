package org.mju_likelion.festival.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import org.mju_likelion.festival.common.exception.CustomException;

public class FrontFilter implements Filter {

  @Override
  public void doFilter(
      final ServletRequest request,
      final ServletResponse response,
      final FilterChain chain)
      throws IOException, ServletException {

    try {
      chain.doFilter(request, response);
    } catch (CustomException customException) {
      ErrorResponseHandler.writeErrorResponse(response, customException);
      response.getWriter().flush();
    }
  }
}

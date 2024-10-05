package org.mju_likelion.festival.common.filter;

import static org.apache.commons.codec.CharEncoding.UTF_8;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.mju_likelion.festival.common.exception.CustomException;
import org.mju_likelion.festival.common.exception.dto.ErrorResponse;

public class ErrorResponseHandler {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void writeErrorResponse(
      final ServletResponse response,
      final CustomException customException)
      throws IOException {

    ErrorResponse errorResponse = ErrorResponse.res(customException);

    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    httpServletResponse.setStatus(customException.getHttpStatus().value());
    httpServletResponse.setContentType(APPLICATION_JSON.getMimeType());
    httpServletResponse.setCharacterEncoding(UTF_8);
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}


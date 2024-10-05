package org.mju_likelion.festival.common.authentication;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationExtractor {

  private static final String AUTHORIZATION_HEADER_TYPE = "Authorization";
  private static final String TOKEN_TYPE = "Bearer";

  public static Optional<String> extract(final HttpServletRequest request) {
    String header = request.getHeader(AUTHORIZATION_HEADER_TYPE);
    if (Strings.isEmpty(header)) {
      return Optional.empty();
    }
    return checkMatch(header.split(" "));
  }

  private static Optional<String> checkMatch(final String[] parts) {
    if (parts.length == 2 && parts[0].equals(TOKEN_TYPE)) {
      return Optional.ofNullable(parts[1]);
    }
    return Optional.empty();
  }
}

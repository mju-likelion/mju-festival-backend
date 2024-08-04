package org.mju_likelion.festival.common.util.qr;

import java.util.Map;
import java.util.StringJoiner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * QR 코드를 생성하는 클래스.
 */
@Component
public class QrGenerator {

  @Value("${client.host}")
  private String clientHost;

  /**
   * QR 코드를 생성한다.
   *
   * @param path  경로 문자열
   * @param value 값 문자열
   * @param query 쿼리 맵
   * @return QR 코드 URL
   */
  public String generateQrCode(final String path, final String value,
      final Map<String, String> query) {
    String queryString = generateQueryString(query);
    return clientHost + path + value + queryString;
  }

  /**
   * 쿼리 맵을 쿼리 문자열로 변환한다.
   *
   * @param query 쿼리 맵
   * @return 쿼리 문자열
   */
  private String generateQueryString(Map<String, String> query) {
    if (query == null || query.isEmpty()) {
      return "";
    }

    StringJoiner joiner = new StringJoiner("&", "?", "");
    query.forEach((key, value) -> joiner.add(key + "=" + value));
    return joiner.toString();
  }
}

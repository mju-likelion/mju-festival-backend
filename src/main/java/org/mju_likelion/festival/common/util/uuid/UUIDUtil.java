package org.mju_likelion.festival.common.util.uuid;

import static org.mju_likelion.festival.common.exception.type.ErrorType.UUID_FORMAT_ERROR;

import java.util.UUID;
import org.mju_likelion.festival.common.exception.InternalServerException;

/**
 * UUID 유틸리티.
 */
public class UUIDUtil {

  private static final String DASH = "-";

  /**
   * 32자리 16진수 문자열을 UUID 로 변환한다.
   *
   * @param hex 16진수 문자열
   * @return UUID
   */
  public static UUID hexToUUID(final String hex) {
    if (hex.length() != 32) {
      throw new InternalServerException(UUID_FORMAT_ERROR);
    }
    StringBuilder sb = new StringBuilder(hex);
    sb.insert(8, DASH);
    sb.insert(13, DASH);
    sb.insert(18, DASH);
    sb.insert(23, DASH);
    return UUID.fromString(sb.toString());
  }

  /**
   * UUID 를 32자리 16진수 문자열로 변환한다.
   *
   * @param uuid UUID
   * @return 16진수 문자열
   */
  public static String uuidToHex(final UUID uuid) {
    return uuid.toString().replace(DASH, "");
  }
}

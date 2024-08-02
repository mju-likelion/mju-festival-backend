package org.mju_likelion.festival.common.util.uuid;

import static org.mju_likelion.festival.common.exception.type.ErrorType.UUID_FORMAT_ERROR;

import java.util.UUID;
import org.mju_likelion.festival.common.exception.InternalServerException;

public class UUIDUtil {

  private static final String DASH = "-";

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

  public static String uuidToHex(final UUID uuid) {
    return uuid.toString().replace(DASH, "");
  }
}

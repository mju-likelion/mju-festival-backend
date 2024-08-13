package org.mju_likelion.festival.common.util.string;

/**
 * 문자열 유틸 클래스
 */
public class StringUtil {

  public static boolean isBlankOrLargerThan(final String str, final int length) {
    return str == null || str.isBlank() || str.length() > length;
  }
}

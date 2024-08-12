package org.mju_likelion.festival.common.util.string;

/**
 * 문자열 유틸 클래스
 */
public class StringUtil {

  public static boolean isBlankOrLargerThan(String str, int length) {
    return str.isBlank() || str.length() > length;
  }
}

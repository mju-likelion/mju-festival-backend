package org.mju_likelion.festival.common.util.string;

/**
 * 문자열 유틸 클래스
 */
public class StringUtil {

  public static boolean isEmptyOrLargerThan(String str, int length) {
    return str.isEmpty() || str.length() > length;
  }
}

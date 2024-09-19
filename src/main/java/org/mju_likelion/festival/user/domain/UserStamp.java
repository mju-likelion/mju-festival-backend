package org.mju_likelion.festival.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStamp {

  int totalStampCount;
  int stampCount;

  @Override
  public String toString() {
    return "UserStamp{" +
        "totalStampCount=" + totalStampCount + '\'' +
        ", stampCount=" + stampCount + '\'' +
        '}';
  }
}

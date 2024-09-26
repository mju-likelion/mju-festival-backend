package org.mju_likelion.festival.booth.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 부스 간단 정보.
 */
@Getter
@AllArgsConstructor
public class SimpleBooth {

  private final UUID id;
  private final String departmentName;
  private final String name;
  private final String imageUrl;

  @Override
  public String toString() {
    return "SimpleBooth{" +
        "id=" + id +
        ", departmentName='" + departmentName + '\'' +
        ", name='" + name + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }
}

package org.mju_likelion.festival.booth.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoothNames {

  private final List<String> boothNames;

  @Override
  public String toString() {
    return "BoothNames{" +
        "boothNames=" + boothNames +
        '}';
  }
}

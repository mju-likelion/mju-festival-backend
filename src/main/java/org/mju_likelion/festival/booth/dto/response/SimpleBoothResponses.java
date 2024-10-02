package org.mju_likelion.festival.booth.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SimpleBoothResponses {

  private List<SimpleBoothResponse> simpleBoothResponseList;

  public static SimpleBoothResponses from(final List<SimpleBoothResponse> simpleBoothResponses) {
    return new SimpleBoothResponses(simpleBoothResponses);
  }

  @Override
  public String toString() {
    return "SimpleBoothResponses{" +
        "simpleBoothResponseList=" + simpleBoothResponseList +
        '}';
  }
}

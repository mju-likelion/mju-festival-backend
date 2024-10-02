package org.mju_likelion.festival.booth.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.booth.domain.SimpleBooth;

/**
 * 부스 간단 정보 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SimpleBoothResponse {

  private UUID id;
  private String departmentName;
  private String name;
  private String imageUrl;

  public static SimpleBoothResponse from(final SimpleBooth simpleBooths) {

    return new SimpleBoothResponse(
        simpleBooths.getId(),
        simpleBooths.getDepartmentName(),
        simpleBooths.getName(),
        simpleBooths.getImageUrl()
    );
  }

  @Override
  public String toString() {
    return "SimpleBoothsResponse{" +
        "id=" + id +
        ", departmentName='" + departmentName + '\'' +
        ", name='" + name + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }
}

package org.mju_likelion.festival.booth.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.BoothDepartment;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoothDepartmentResponse {

  private final UUID id;
  private final String name;
  private final String categoryName;

  public static BoothDepartmentResponse from(final BoothDepartment boothDepartment) {
    return new BoothDepartmentResponse(
        boothDepartment.getId(),
        boothDepartment.getName(),
        boothDepartment.getCategoryName()
    );
  }

  @Override
  public String toString() {
    return "BoothDepartmentResponse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", categoryName='" + categoryName + '\'' +
        '}';
  }
}

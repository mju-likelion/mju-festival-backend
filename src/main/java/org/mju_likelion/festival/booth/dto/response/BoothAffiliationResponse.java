package org.mju_likelion.festival.booth.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.BoothAffiliation;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoothAffiliationResponse {

  private final UUID id;
  private final String name;
  private final String categoryName;

  public static BoothAffiliationResponse from(final BoothAffiliation boothAffiliation) {
    return new BoothAffiliationResponse(
        boothAffiliation.getId(),
        boothAffiliation.getName(),
        boothAffiliation.getCategoryName()
    );
  }

  @Override
  public String toString() {
    return "BoothAffiliationResponse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", categoryName='" + categoryName + '\'' +
        '}';
  }
}

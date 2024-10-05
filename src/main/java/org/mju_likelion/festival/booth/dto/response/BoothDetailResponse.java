package org.mju_likelion.festival.booth.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothDetail;

/**
 * 부스 상세 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BoothDetailResponse {

  private UUID id;
  private String name;
  private String description;
  private String department;
  private String location;
  private String imageUrl;
  private String locationImageUrl;
  private Boolean isEventBooth;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  public static BoothDetailResponse from(final BoothDetail boothDetail) {
    return new BoothDetailResponse(boothDetail.getId(), boothDetail.getName(),
        boothDetail.getDescription(), boothDetail.getDepartment(), boothDetail.getLocation(),
        boothDetail.getImageUrl(), boothDetail.getLocationImageUrl(),
        boothDetail.getIsEventBooth(), boothDetail.getCreatedAt());
  }

  @Override
  public String toString() {
    return "BoothDetailResponse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", department='" + department + '\'' +
        ", location='" + location + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", locationImageUrl='" + locationImageUrl + '\'' +
        ", isEventBooth=" + isEventBooth + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}

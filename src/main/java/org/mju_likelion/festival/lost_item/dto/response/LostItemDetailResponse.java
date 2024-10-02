package org.mju_likelion.festival.lost_item.dto.response;

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
import org.mju_likelion.festival.lost_item.domain.LostItemDetail;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LostItemDetailResponse {

  private UUID id;
  private String title;
  private String content;
  private String imageUrl;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;
  private Boolean isFounded;

  public static LostItemDetailResponse from(final LostItemDetail lostItemDetail) {
    return new LostItemDetailResponse(
        lostItemDetail.getId(),
        lostItemDetail.getTitle(),
        lostItemDetail.getContent(),
        lostItemDetail.getImageUrl(),
        lostItemDetail.getCreatedAt(),
        lostItemDetail.getIsFounded()
    );
  }

  @Override
  public String toString() {
    return "SimpleLostItem{" +
        "id=" + id + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", createdAt=" + createdAt + '\'' +
        ", isFounded=" + isFounded +
        '}';
  }
}

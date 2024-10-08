package org.mju_likelion.festival.lost_item.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleLostItem {

  private final UUID id;
  private final String title;
  private final String content;
  private final String imageUrl;
  private final LocalDateTime createdAt;
  private final Boolean isFounded;

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

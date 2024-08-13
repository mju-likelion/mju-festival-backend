package org.mju_likelion.festival.announcement.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공지사항 상세 정보.
 */
@Getter
@AllArgsConstructor
public class AnnouncementDetail {

  private final UUID id;
  private final String title;
  private final String content;
  private final LocalDateTime createdAt;
  private final String imageUrl;
}

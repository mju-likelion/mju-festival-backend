package org.mju_likelion.festival.announcement.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleAnnouncement {

  private final UUID id;
  private final String title;
  private final String content;
  private final LocalDateTime createdAt;
}

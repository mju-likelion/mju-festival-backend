package org.mju_likelion.festival.announcement.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleAnnouncementResponse {

  private final UUID id;
  private final String title;
  private final String content;

  public static SimpleAnnouncementResponse from(SimpleAnnouncement simpleAnnouncement) {
    return new SimpleAnnouncementResponse(simpleAnnouncement.getId(), simpleAnnouncement.getTitle(),
        simpleAnnouncement.getContent());
  }
}

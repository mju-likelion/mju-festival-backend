package org.mju_likelion.festival.announcement.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleAnnouncementsResponse {

  private final List<SimpleAnnouncement> simpleAnnouncements;
  private final int totalPage;

  public static SimpleAnnouncementsResponse of(
      final List<SimpleAnnouncement> simpleAnnouncements,
      final int totalPage) {

    return new SimpleAnnouncementsResponse(simpleAnnouncements, totalPage);
  }

  @Override
  public String toString() {
    return "SimpleAnnouncementsResponse{" +
        "simpleAnnouncements=" + simpleAnnouncements +
        ", totalPage=" + totalPage +
        '}';
  }
}

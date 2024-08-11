package org.mju_likelion.festival.announcement.dto.response;


import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.announcement.domain.AnnouncementDetail;

/**
 * 공지사항 상세 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnouncementDetailResponse {

  private final UUID id;
  private final String title;
  private final String content;
  private final String imageUrl;

  public static AnnouncementDetailResponse from(final AnnouncementDetail announcementDetail) {

    return new AnnouncementDetailResponse(
        announcementDetail.getId(),
        announcementDetail.getTitle(),
        announcementDetail.getContent(),
        announcementDetail.getImageUrl()
    );
  }
}

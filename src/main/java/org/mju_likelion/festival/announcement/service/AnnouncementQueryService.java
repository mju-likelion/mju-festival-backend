package org.mju_likelion.festival.announcement.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ANNOUNCEMENT_NOT_FOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementQueryRepository;
import org.mju_likelion.festival.announcement.dto.response.AnnouncementDetailResponse;
import org.mju_likelion.festival.announcement.dto.response.SimpleAnnouncementsResponse;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementQueryService {

  private final AnnouncementQueryRepository announcementQueryRepository;
  private final AnnouncementServiceUtil announcementServiceUtil;

  public SimpleAnnouncementsResponse getAnnouncements(
      final SortOrder sort,
      final int page,
      final int size) {

    int totalPage = announcementQueryRepository.getTotalPage(size);

    announcementServiceUtil.validatePage(page, totalPage);

    List<SimpleAnnouncement> simpleAnnouncements = announcementQueryRepository
        .findOrderedSimpleAnnouncementsWithPagenation(sort, page, size);

    return SimpleAnnouncementsResponse.of(simpleAnnouncements, totalPage);
  }

  public AnnouncementDetailResponse getAnnouncement(final UUID id) {
    return AnnouncementDetailResponse.from(
        announcementQueryRepository.findAnnouncementById(id).orElseThrow(
            () -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND_ERROR)
        )
    );
  }
}

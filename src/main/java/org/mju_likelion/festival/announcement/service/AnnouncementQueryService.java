package org.mju_likelion.festival.announcement.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.List;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.announcement.domain.SimpleAnnouncement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementQueryRepository;
import org.mju_likelion.festival.announcement.dto.response.SimpleAnnouncementsResponse;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementQueryService {

  private final AnnouncementQueryRepository announcementQueryRepository;

  public SimpleAnnouncementsResponse getAnnouncements(Direction sort, int page, int size) {

    int totalPage = announcementQueryRepository.getTotalPage(size);

    validatePage(page, totalPage);

    List<SimpleAnnouncement> simpleAnnouncements = announcementQueryRepository
        .findSimpleAnnouncements(sort, page, size);

    return SimpleAnnouncementsResponse.from(simpleAnnouncements, totalPage);
  }

  private void validatePage(int page, int totalPage) {
    if (page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }
}

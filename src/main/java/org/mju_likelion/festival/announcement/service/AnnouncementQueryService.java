package org.mju_likelion.festival.announcement.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementQueryRepository;
import org.mju_likelion.festival.announcement.dto.response.SimpleAnnouncementResponse;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementQueryService {

  private final AnnouncementQueryRepository announcementQueryRepository;

  public List<SimpleAnnouncementResponse> getAnnouncements(Direction sort, int page, int size) {
    return announcementQueryRepository.findSimpleAnnouncements(sort, page, size).stream()
        .map(SimpleAnnouncementResponse::from)
        .toList();
  }
}

package org.mju_likelion.festival.announcement.domain.repository;

import java.util.List;
import java.util.UUID;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementJpaRepository extends JpaRepository<Announcement, UUID> {

  /**
   * 작성자를 기준으로 공지사항을 조회한다.
   * <p>
   * 테스트에서만 사용한다.
   */
  List<Announcement> findByWriterId(UUID writerId);
}

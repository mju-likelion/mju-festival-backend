package org.mju_likelion.festival.announcement.domain.repository;

import java.util.List;
import java.util.UUID;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementJpaRepository extends JpaRepository<Announcement, UUID> {

  List<Announcement> findByWriterId(final UUID writerId);
}

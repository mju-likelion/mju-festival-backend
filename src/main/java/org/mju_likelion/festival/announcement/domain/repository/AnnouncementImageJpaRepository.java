package org.mju_likelion.festival.announcement.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.announcement.domain.AnnouncementImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementImageJpaRepository extends JpaRepository<AnnouncementImage, UUID> {

}

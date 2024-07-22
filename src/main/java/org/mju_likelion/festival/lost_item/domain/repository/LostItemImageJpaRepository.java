package org.mju_likelion.festival.lost_item.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.lost_item.domain.LostItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostItemImageJpaRepository extends JpaRepository<LostItemImage, UUID> {

}

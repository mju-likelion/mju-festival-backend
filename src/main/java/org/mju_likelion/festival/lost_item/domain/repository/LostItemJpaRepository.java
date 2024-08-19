package org.mju_likelion.festival.lost_item.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostItemJpaRepository extends JpaRepository<LostItem, UUID> {

}

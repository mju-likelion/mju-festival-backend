package org.mju_likelion.festival.lost_item.domain.repository;

import java.util.List;
import java.util.UUID;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostItemJpaRepository extends JpaRepository<LostItem, UUID> {

  /**
   * 작성자 ID로 분실물을 조회한다.
   * <p>
   * 테스트에서만 사용
   *
   * @param writerId 작성자 ID
   * @return 분실물 목록
   */
  List<LostItem> findByWriterId(UUID writerId);
}

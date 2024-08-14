package org.mju_likelion.festival.booth.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothUser;
import org.mju_likelion.festival.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothUserJpaRepository extends JpaRepository<BoothUser, UUID> {

  /**
   * 사용자와 부스로 부스 사용자 조회. 테스트 코드 작성을 위해 추가함.
   *
   * @param user  사용자
   * @param booth 부스
   * @return 부스 사용자
   */
  Optional<BoothUser> findByUserAndBooth(final User user, final Booth booth);
}

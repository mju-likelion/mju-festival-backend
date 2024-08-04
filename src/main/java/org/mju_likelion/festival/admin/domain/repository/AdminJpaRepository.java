package org.mju_likelion.festival.admin.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.mju_likelion.festival.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminJpaRepository extends JpaRepository<Admin, UUID> {

  /**
   * 로그인 ID 와 비밀번호로 Admin 조회.
   *
   * @param loginId  로그인 ID
   * @param password 비밀번호
   * @return Admin
   */
  Optional<Admin> findByLoginIdAndPassword(String loginId, String password);

  /**
   * 부스 ID 로 Admin 조회.
   */
  Optional<Admin> findByBoothId(UUID boothId);
}

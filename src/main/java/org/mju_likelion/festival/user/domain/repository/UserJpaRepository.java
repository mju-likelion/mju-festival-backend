package org.mju_likelion.festival.user.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.mju_likelion.festival.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {

  /**
   * 학번으로 사용자 ID 조회.
   *
   * @param studentId 학번
   * @return 사용자 ID
   */
  @Query("SELECT u.id FROM user u WHERE u.studentId = :studentId")
  Optional<UUID> findIdByStudentId(@Param("studentId") final String studentId);

  @Modifying
  @Query("DELETE FROM user u WHERE u.id = :userId")
  void deleteById(@NonNull @Param("userId") final UUID userId);
}

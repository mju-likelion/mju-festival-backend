package org.mju_likelion.festival.user.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import java.util.UUID;
import org.mju_likelion.festival.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {

  @Query("SELECT u.id FROM user u WHERE u.studentId = :studentId")
  Optional<UUID> findIdByStudentId(@Param("studentId") String studentId);
}

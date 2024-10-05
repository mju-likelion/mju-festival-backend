package org.mju_likelion.festival.term.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.UUID;
import lombok.NonNull;
import org.mju_likelion.festival.term.domain.TermUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TermUserJpaRepository extends JpaRepository<TermUser, UUID> {

  @Modifying
  @Query("DELETE FROM term_user tu WHERE tu.user.id = :userId")
  void deleteByUserId(@NonNull @Param("userId") final UUID userId);
}

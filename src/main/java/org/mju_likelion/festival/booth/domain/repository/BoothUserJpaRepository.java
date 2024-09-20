package org.mju_likelion.festival.booth.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.UUID;
import lombok.NonNull;
import org.mju_likelion.festival.booth.domain.BoothUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothUserJpaRepository extends JpaRepository<BoothUser, UUID> {

  @Modifying
  @Query("DELETE FROM booth_user bu WHERE bu.user.id = :userId")
  void deleteByUserId(@NonNull @Param("userId") final UUID userId);
}

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

  Optional<BoothUser> findByUserAndBooth(final User user, final Booth booth);
}

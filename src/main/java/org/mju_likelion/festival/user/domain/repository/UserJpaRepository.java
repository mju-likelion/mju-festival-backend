package org.mju_likelion.festival.user.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {

}

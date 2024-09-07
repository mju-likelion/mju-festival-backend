package org.mju_likelion.festival.admin.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.mju_likelion.festival.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminJpaRepository extends JpaRepository<Admin, UUID> {

  Optional<Admin> findByLoginId(final String loginId);
}

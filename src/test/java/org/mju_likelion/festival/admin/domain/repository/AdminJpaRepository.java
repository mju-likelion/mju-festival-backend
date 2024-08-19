package org.mju_likelion.festival.admin.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminJpaRepository extends JpaRepository<Admin, UUID> {

  Optional<Admin> findByRole(final AdminRole role);
}

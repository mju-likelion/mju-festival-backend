package org.mju_likelion.festival.booth.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.booth.domain.BoothDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothDepartmentJpaRepository extends JpaRepository<BoothDepartment, UUID> {

}

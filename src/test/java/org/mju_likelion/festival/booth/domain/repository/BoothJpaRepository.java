package org.mju_likelion.festival.booth.domain.repository;

import java.util.List;
import java.util.UUID;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothJpaRepository extends JpaRepository<Booth, UUID> {

  long countByBoothInfo_Department(BoothDepartment department);

  List<Booth> findAllByIsEventBooth(boolean isEventBooth);
}

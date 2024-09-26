package org.mju_likelion.festival.booth.domain.repository;

import java.util.List;
import java.util.UUID;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothAffiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothJpaRepository extends JpaRepository<Booth, UUID> {

  long countByBoothInfo_Affiliation(BoothAffiliation department);

  List<Booth> findAllByIsEventBooth(boolean isEventBooth);
}

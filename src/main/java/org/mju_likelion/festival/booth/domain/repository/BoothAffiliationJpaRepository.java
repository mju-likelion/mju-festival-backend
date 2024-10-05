package org.mju_likelion.festival.booth.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.booth.domain.BoothAffiliation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothAffiliationJpaRepository extends JpaRepository<BoothAffiliation, UUID> {

}

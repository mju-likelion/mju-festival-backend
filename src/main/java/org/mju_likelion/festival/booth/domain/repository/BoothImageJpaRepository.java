package org.mju_likelion.festival.booth.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.booth.domain.BoothImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothImageJpaRepository extends JpaRepository<BoothImage, UUID> {

}

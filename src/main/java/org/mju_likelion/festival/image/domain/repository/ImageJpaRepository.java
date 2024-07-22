package org.mju_likelion.festival.image.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageJpaRepository extends JpaRepository<Image, UUID> {

}

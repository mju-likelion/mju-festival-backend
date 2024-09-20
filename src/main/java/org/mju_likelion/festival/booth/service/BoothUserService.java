package org.mju_likelion.festival.booth.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.booth.domain.repository.BoothUserJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothUserService {

  private final BoothUserJpaRepository boothUserJpaRepository;

  public void deleteAllByUserId(final UUID userId) {
    boothUserJpaRepository.deleteByUserId(userId);
  }
}

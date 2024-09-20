package org.mju_likelion.festival.term.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.term.domain.repository.TermUserJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TermUserService {

  private final TermUserJpaRepository termUserJpaRepository;

  public void deleteAllByUserId(final UUID userId) {
    termUserJpaRepository.deleteByUserId(userId);
  }
}

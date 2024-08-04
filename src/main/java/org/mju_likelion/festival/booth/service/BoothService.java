package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.USER_NOT_FOUND_ERROR;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothQrStrategy;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.util.qr.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.BoothQrManagerContext;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.user.domain.User;
import org.mju_likelion.festival.user.domain.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class BoothService {

  private final BoothQrManagerContext boothQrManagerContext;
  private final BoothJpaRepository boothJpaRepository;
  private final UserJpaRepository userJpaRepository;

  public void visitBooth(final String qrId, final BoothQrStrategy boothQrStrategy,
      final UUID userId) {
    BoothQrManager boothQrManager = boothQrManagerContext.boothQrManager(boothQrStrategy);

    UUID boothId = boothQrManager.getBoothIdFromQrId(qrId);

    User user = getExistingUser(userId);
    Booth booth = getExistingBooth(boothId);

    user.visitBooth(booth);
    userJpaRepository.save(user);
  }

  private User getExistingUser(final UUID userId) {
    return userJpaRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_ERROR));
  }

  private Booth getExistingBooth(final UUID boothId) {
    return boothJpaRepository.findById(boothId)
        .orElseThrow(() -> new NotFoundException(BOOTH_NOT_FOUND_ERROR));
  }
}
package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.util.null_handler.NullHandler.doIfNotNull;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothQrStrategy;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.dto.request.UpdateBoothRequest;
import org.mju_likelion.festival.booth.util.qr.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.BoothQrManagerContext;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManagerContext;
import org.mju_likelion.festival.image.domain.Image;
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
  private final BoothServiceUtils boothServiceUtils;

  public void visitBooth(
      final String qrId,
      final BoothQrStrategy boothQrStrategy,
      final UUID userId) {

    BoothQrManager boothQrManager = boothQrManagerContext.boothQrManager(boothQrStrategy);

    UUID boothId = boothQrManager.getBoothIdFromQrId(qrId);

    User user = boothServiceUtils.getExistingUser(userId);
    Booth booth = boothServiceUtils.getExistingBooth(boothId);

    user.visitBooth(booth);
    userJpaRepository.save(user);
  }

  public void updateBooth(
      final UUID boothId,
      final UpdateBoothRequest updateBoothRequest,
      final UUID boothAdminId) {

    Booth booth = boothServiceUtils.getExistingBooth(boothId);
    Admin admin = boothServiceUtils.getExistingAdmin(boothAdminId);

    boothServiceUtils.validateBoothAdminOwner(admin, booth);

    doIfNotNull(updateBoothRequest.getName(), booth::updateName);
    doIfNotNull(updateBoothRequest.getDescription(), booth::updateDescription);
    doIfNotNull(updateBoothRequest.getLocation(), booth::updateLocation);
    doIfNotNull(updateBoothRequest.getLocationImageUrl(),
        url -> booth.updateLocationImage(new Image(url)));
    doIfNotNull(updateBoothRequest.getImageUrl(), url -> booth.updateImage(new Image(url)));

    boothJpaRepository.save(booth);
  }
}
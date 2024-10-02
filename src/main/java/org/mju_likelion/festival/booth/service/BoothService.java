package org.mju_likelion.festival.booth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.dto.request.UpdateBoothRequest;
import org.mju_likelion.festival.booth.util.qr.BoothQrStrategy;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManager;
import org.mju_likelion.festival.user.domain.User;
import org.mju_likelion.festival.user.service.UserQueryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {

  private final UserQueryService userQueryService;
  private final AdminQueryService adminQueryService;
  private final BoothQueryService boothQueryService;
  private final BoothJpaRepository boothJpaRepository;
  private final BoothServiceUtil boothServiceUtil;

  public void visitBooth(
      final String qrId,
      final BoothQrStrategy boothQrStrategy,
      final UUID userId) {

    BoothQrManager boothQrManager = boothServiceUtil.boothQrManager(boothQrStrategy);

    UUID boothId = boothQrManager.getBoothIdFromQrId(qrId);

    Booth booth = boothQueryService.getExistingBooth(boothId);
    boothQueryService.validateEventBooth(booth);
    User user = userQueryService.getExistingUser(userId);

    user.visitBooth(booth);
    userQueryService.saveUser(user);
  }

  @CacheEvict(value = "boothDetail", key = "#boothId")
  public void updateBooth(
      final UUID boothId,
      final UpdateBoothRequest updateBoothRequest,
      final UUID boothAdminId) {

    Admin admin = adminQueryService.getExistingAdmin(boothAdminId);
    Booth booth = boothQueryService.getExistingBooth(boothId);

    boothQueryService.validateBoothAdminOwner(admin, booth);

    booth.getBoothInfo().updateDescription(updateBoothRequest.getDescription());

    boothJpaRepository.save(booth);
  }
}
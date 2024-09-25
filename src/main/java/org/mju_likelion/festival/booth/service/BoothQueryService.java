package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_DEPARTMENT_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.NOT_BOOTH_OWNER_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothDetail;
import org.mju_likelion.festival.booth.domain.BoothNames;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.mju_likelion.festival.booth.domain.repository.BoothDepartmentJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDepartmentResponse;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothManagingDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothsResponse;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManager;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothQueryService {

  private final AdminQueryService adminQueryService;
  private final BoothServiceUtil boothServiceUtil;
  private final BoothQueryRepository boothQueryRepository;
  private final BoothJpaRepository boothJpaRepository;
  private final BoothDepartmentJpaRepository boothDepartmentJpaRepository;

  public List<BoothDepartmentResponse> getBoothDepartments() {
    return boothDepartmentJpaRepository.findAll().stream()
        .map(BoothDepartmentResponse::from)
        .toList();
  }

  public SimpleBoothsResponse getBooths(final UUID departmentId) {
    validateBoothDepartment(departmentId);

    List<SimpleBooth> simpleBooths = boothQueryRepository.findAllSimpleBoothByDepartmentId(
        departmentId);

    return SimpleBoothsResponse.from(simpleBooths);
  }

  public BoothDetailResponse getBooth(final UUID id) {
    return BoothDetailResponse.from(getExistingBoothDetail(id));
  }

  public BoothManagingDetailResponse getBoothManagingDetail(final UUID boothId,
      final UUID boothAdminId) {
    validateBoothExistence(boothId);
    adminQueryService.validateAdminExistence(boothAdminId);
    boolean isBoothOwner = boothQueryRepository.isBoothOwner(boothId, boothAdminId);
    boolean isEventBooth = boothQueryRepository.isEventBooth(boothId);

    return BoothManagingDetailResponse.of(isBoothOwner, isEventBooth);
  }

  public BoothQrResponse getBoothQr(final UUID boothId, final UUID boothAdminId) {
    BoothQrManager boothQrManager = boothServiceUtil.boothQrManager();

    Admin admin = adminQueryService.getExistingAdmin(boothAdminId);
    Booth booth = getExistingBooth(boothId);

    validateBoothAdminOwner(admin, booth);

    return new BoothQrResponse(boothQrManager.generateBoothQr(boothId));
  }

  public BoothNames getVisitedBoothNamesByUserId(final UUID userId) {
    return boothQueryRepository.findBoothNamesByUserId(userId);
  }

  private void validateBoothDepartment(final UUID departmentId) {
    if (!boothDepartmentJpaRepository.existsById(departmentId)) {
      throw new NotFoundException(BOOTH_DEPARTMENT_NOT_FOUND_ERROR);
    }
  }

  public void validateBoothExistence(final UUID boothId) {
    if (!boothJpaRepository.existsById(boothId)) {
      throw new NotFoundException(BOOTH_NOT_FOUND_ERROR);
    }
  }

  public BoothDetail getExistingBoothDetail(final UUID boothId) {
    return boothQueryRepository.findBoothById(boothId).orElseThrow(
        () -> new NotFoundException(BOOTH_NOT_FOUND_ERROR)
    );
  }

  public void validateBoothAdminOwner(final Admin admin, final Booth booth) {
    if (!booth.isManagedBy(admin)) {
      throw new ForbiddenException(NOT_BOOTH_OWNER_ERROR);
    }
  }

  public Booth getExistingBooth(final UUID boothId) {
    return boothJpaRepository.findById(boothId).orElseThrow(
        () -> new NotFoundException(BOOTH_NOT_FOUND_ERROR)
    );
  }
}


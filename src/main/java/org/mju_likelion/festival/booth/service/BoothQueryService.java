package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.NOT_BOOTH_OWNER_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothsResponse;
import org.mju_likelion.festival.booth.util.qr.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.BoothQrManagerContext;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BoothQueryService {

  private final BoothQueryRepository boothQueryRepository;
  private final BoothJpaRepository boothJpaRepository;
  private final AdminJpaRepository adminJpaRepository;
  private final BoothQrManagerContext boothQrManagerContext;

  public SimpleBoothsResponse getBooths(final int page, final int size) {

    int totalPage = boothQueryRepository.findTotalPage(size);

    validatePage(page, totalPage);

    List<SimpleBooth> simpleBooths = boothQueryRepository.findOrderedSimpleBoothsWithPagination(
        page, size);

    return SimpleBoothsResponse.from(simpleBooths, totalPage);
  }

  public BoothDetailResponse getBooth(final UUID id) {
    return BoothDetailResponse.from(
        boothQueryRepository.findBoothById(id).orElseThrow(
            () -> new NotFoundException(BOOTH_NOT_FOUND_ERROR)
        )
    );
  }

  public BoothQrResponse getBoothQr(final UUID boothId, final UUID boothAdminId) {
    BoothQrManager boothQrManager = boothQrManagerContext.boothQrManager();

    Admin admin = getExistingAdmin(boothAdminId);
    Booth booth = getExistingBooth(boothId);

    validateBoothAdminOwner(admin, booth);

    return new BoothQrResponse(boothQrManager.generateBoothQr(boothId));
  }

  private void validatePage(int page, int totalPage) {
    if (page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }

  private void validateBoothAdminOwner(Admin admin, Booth booth) {
    if (!admin.isOwner(booth)) {
      throw new ForbiddenException(NOT_BOOTH_OWNER_ERROR);
    }
  }

  private Admin getExistingAdmin(UUID adminId) {
    return adminJpaRepository.findById(adminId).orElseThrow(
        () -> new NotFoundException(ADMIN_NOT_FOUND_ERROR)
    );
  }

  private Booth getExistingBooth(UUID boothId) {
    return boothJpaRepository.findById(boothId).orElseThrow(
        () -> new NotFoundException(BOOTH_NOT_FOUND_ERROR)
    );
  }
}


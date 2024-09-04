package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.NOT_BOOTH_OWNER_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothOwnershipResponse;
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

  public BoothOwnershipResponse isBoothOwner(final UUID boothId, final UUID boothAdminId) {
    adminQueryService.validateAdminExistence(boothAdminId);

    return BoothOwnershipResponse.from(boothQueryRepository.isBoothOwner(boothId, boothAdminId));
  }

  public BoothQrResponse getBoothQr(final UUID boothId, final UUID boothAdminId) {
    BoothQrManager boothQrManager = boothServiceUtil.boothQrManager();

    Admin admin = adminQueryService.getExistingAdmin(boothAdminId);
    Booth booth = getExistingBooth(boothId);

    validateBoothAdminOwner(admin, booth);

    return new BoothQrResponse(boothQrManager.generateBoothQr(boothId));
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

  public void validatePage(final int page, final int totalPage) {
    if (page != 0 && page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }
}


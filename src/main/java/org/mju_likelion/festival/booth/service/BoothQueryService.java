package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothOwnershipResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothsResponse;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManagerContext;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothQueryService {

  private final BoothQueryRepository boothQueryRepository;
  private final BoothQrManagerContext boothQrManagerContext;
  private final BoothServiceUtil boothServiceUtil;

  public SimpleBoothsResponse getBooths(final int page, final int size) {
    int totalPage = boothQueryRepository.findTotalPage(size);

    boothServiceUtil.validatePage(page, totalPage);

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
    boothServiceUtil.validateAdminExists(boothAdminId);

    return BoothOwnershipResponse.from(boothQueryRepository.isBoothOwner(boothId, boothAdminId));
  }

  public BoothQrResponse getBoothQr(final UUID boothId, final UUID boothAdminId) {
    BoothQrManager boothQrManager = boothQrManagerContext.boothQrManager();

    Admin admin = boothServiceUtil.getExistingAdmin(boothAdminId);
    Booth booth = boothServiceUtil.getExistingBooth(boothId);

    boothServiceUtil.validateBoothAdminOwner(admin, booth);

    return new BoothQrResponse(boothQrManager.generateBoothQr(boothId));
  }
}


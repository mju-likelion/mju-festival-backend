package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothResponse;
import org.mju_likelion.festival.booth.util.qr.BoothQrManager;
import org.mju_likelion.festival.booth.util.qr.BoothQrManagerContext;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BoothQueryService {

  private final BoothQueryRepository boothQueryRepository;
  private final BoothQrManagerContext boothQrManagerContext;

  public List<SimpleBoothResponse> getBooths(final int page, final int size) {
    return boothQueryRepository.findOrderedSimpleBoothsWithPagination(page, size)
        .stream().map(SimpleBoothResponse::from).toList();
  }

  public BoothDetailResponse getBooth(final UUID id) {
    return BoothDetailResponse.from(
        boothQueryRepository.findBoothById(id).orElseThrow(
            () -> new NotFoundException(BOOTH_NOT_FOUND_ERROR)
        )
    );
  }

  public BoothQrResponse getBoothQr(final UUID id) {
    BoothQrManager boothQrManager = boothQrManagerContext.boothQrManager();

    validateBooth(id);
    return new BoothQrResponse(boothQrManager.generateBoothQr(id));
  }

  private void validateBooth(final UUID id) {
    if (!boothQueryRepository.existsById(id)) {
      throw new NotFoundException(BOOTH_NOT_FOUND_ERROR);
    }
  }
}


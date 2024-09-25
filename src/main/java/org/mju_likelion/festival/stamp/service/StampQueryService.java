package org.mju_likelion.festival.stamp.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothNames;
import org.mju_likelion.festival.booth.service.BoothQueryService;
import org.mju_likelion.festival.user.dto.response.StampResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampQueryService {

  private final BoothQueryService boothQueryService;
  private final int boothsCountToComplete = 5;

  public StampResponse getStampResponseByUserId(final UUID userId) {
    BoothNames boothNames = boothQueryService.getVisitedBoothNamesByUserId(userId);
    return StampResponse.of(boothNames, boothsCountToComplete);
  }
}

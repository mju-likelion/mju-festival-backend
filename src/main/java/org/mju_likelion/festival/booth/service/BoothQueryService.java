package org.mju_likelion.festival.booth.service;

import static org.mju_likelion.festival.common.config.Resilience4jConfig.REDIS_CIRCUIT_BREAKER;
import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_DEPARTMENT_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.NOT_BOOTH_OWNER_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.NOT_EVENT_BOOTH_ERROR;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothDetail;
import org.mju_likelion.festival.booth.domain.BoothNames;
import org.mju_likelion.festival.booth.domain.SimpleBooth;
import org.mju_likelion.festival.booth.domain.repository.BoothAffiliationJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothQueryRepository;
import org.mju_likelion.festival.booth.dto.response.BoothAffiliationResponse;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothManagingDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothResponses;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManager;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.common.util.circuit_breaker.FallBackUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothQueryService {

  private final AdminQueryService adminQueryService;
  private final BoothServiceUtil boothServiceUtil;
  private final BoothQueryRepository boothQueryRepository;
  private final BoothJpaRepository boothJpaRepository;
  private final BoothAffiliationJpaRepository boothAffiliationJpaRepository;

  public List<BoothAffiliationResponse> getBoothAffiliations() {
    return boothAffiliationJpaRepository.findAll().stream()
        .map(BoothAffiliationResponse::from)
        .toList();
  }

  @Cacheable(value = "simpleBooths", key = "#affiliationId")
  @CircuitBreaker(name = REDIS_CIRCUIT_BREAKER, fallbackMethod = "getBoothsFallback")
  public SimpleBoothResponses getBooths(final UUID affiliationId) {
    return getBoothsLogic(affiliationId);
  }

  public SimpleBoothResponses getBoothsFallback(final UUID affiliationId, final Exception e) {
    FallBackUtil.handleFallBack(e);
    return getBoothsLogic(affiliationId);
  }

  private SimpleBoothResponses getBoothsLogic(final UUID affiliationId) {
    validateBoothDepartment(affiliationId);

    List<SimpleBooth> simpleBooths = boothQueryRepository.findAllSimpleBoothByAffiliationId(
        affiliationId);

    List<SimpleBoothResponse> simpleBoothResponseList = simpleBooths.stream()
        .map(SimpleBoothResponse::from)
        .toList();

    return SimpleBoothResponses.from(simpleBoothResponseList);
  }

  @Cacheable(value = "boothDetail", key = "#id")
  @CircuitBreaker(name = REDIS_CIRCUIT_BREAKER, fallbackMethod = "getBoothFallback")
  public BoothDetailResponse getBooth(final UUID id) {
    return getBoothLogic(id);
  }

  public BoothDetailResponse getBoothFallback(final UUID id, final Exception e) {
    FallBackUtil.handleFallBack(e);
    return getBoothLogic(id);
  }

  private BoothDetailResponse getBoothLogic(final UUID id) {
    return BoothDetailResponse.from(getExistingBoothDetail(id));
  }

  @Cacheable(value = "boothManagingDetail", key = "#boothId + ' : ' + #boothAdminId")
  @CircuitBreaker(name = REDIS_CIRCUIT_BREAKER, fallbackMethod = "getBoothManagingDetailFallback")
  public BoothManagingDetailResponse getBoothManagingDetail(
      final UUID boothId,
      final UUID boothAdminId) {

    return getBoothManagingDetailLogic(boothId, boothAdminId);
  }

  public BoothManagingDetailResponse getBoothManagingDetailFallback(
      final UUID boothId,
      final UUID boothAdminId,
      final Exception e) {

    FallBackUtil.handleFallBack(e);
    return getBoothManagingDetailLogic(boothId, boothAdminId);
  }

  private BoothManagingDetailResponse getBoothManagingDetailLogic(final UUID boothId,
      final UUID boothAdminId) {
    validateBoothExistence(boothId);
    adminQueryService.validateAdminExistence(boothAdminId);
    boolean isBoothOwner = boothQueryRepository.isBoothOwner(boothId, boothAdminId);

    return BoothManagingDetailResponse.of(isBoothOwner);
  }

  public BoothQrResponse getBoothQr(final UUID boothId, final UUID boothAdminId) {
    BoothQrManager boothQrManager = boothServiceUtil.boothQrManager();

    Booth booth = getExistingBooth(boothId);
    validateEventBooth(booth);
    Admin admin = adminQueryService.getExistingAdmin(boothAdminId);

    validateBoothAdminOwner(admin, booth);

    return new BoothQrResponse(boothQrManager.generateBoothQr(boothId));
  }

  public BoothNames getVisitedBoothNamesByUserId(final UUID userId) {
    return boothQueryRepository.findBoothNamesSortedByCreatedAt(userId);
  }

  private void validateBoothDepartment(final UUID departmentId) {
    if (!boothAffiliationJpaRepository.existsById(departmentId)) {
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

  public void validateEventBooth(final Booth booth) {
    if (!booth.getIsEventBooth()) {
      throw new BadRequestException(NOT_EVENT_BOOTH_ERROR);
    }
  }
}


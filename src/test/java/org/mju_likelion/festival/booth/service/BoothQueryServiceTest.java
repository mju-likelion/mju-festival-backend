package org.mju_likelion.festival.booth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.willReturn;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothDepartment;
import org.mju_likelion.festival.booth.domain.repository.BoothDepartmentJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothsResponse;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManagerContext;
import org.mju_likelion.festival.booth.util.qr.manager.RedisBoothQrManager;
import org.mju_likelion.festival.booth.util.qr.manager.TokenBoothQrManager;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("BoothQueryService")
@ApplicationTest
public class BoothQueryServiceTest {

  @Autowired
  private BoothQueryService boothQueryService;
  @Autowired
  private BoothJpaRepository boothJpaRepository;
  @Autowired
  private BoothDepartmentJpaRepository boothDepartmentJpaRepository;
  @MockBean
  private BoothQrManagerContext boothQrManagerContext;
  @Autowired
  private RedisBoothQrManager redisBoothQrManager;
  @Autowired
  private TokenBoothQrManager tokenBoothQrManager;

  @DisplayName("부스 간단 정보 List 조회")
  @Test
  public void testGetBooths() {
    // given
    BoothDepartment department = boothDepartmentJpaRepository.findAll().get(0);

    // when
    SimpleBoothsResponse booths = boothQueryService.getBooths(department.getId());

    // then
    assertThat(booths.getSimpleBooths()).isNotEmpty();
  }

  @DisplayName("부스 상세 정보 조회")
  @Test
  public void testGetBooth() {
    // given
    UUID boothIdToFind = boothJpaRepository.findAll().get(0).getId();

    // when
    BoothDetailResponse boothDetailResponse = boothQueryService.getBooth(boothIdToFind);

    // then
    assertThat(boothDetailResponse.getId()).isEqualTo(boothIdToFind);
  }

  @DisplayName("부스 소유 여부를 확인한다 - 소유")
  @Test
  public void testIsBoothOwner() {
    // given
    List<Booth> booths = boothJpaRepository.findAll();
    Booth booth = booths.get(0);
    Admin admin = booth.getOwner();

    // when
    boolean isOwner = boothQueryService.isBoothOwner(booth.getId(), admin.getId()).getIsOwner();

    // then
    assertThat(isOwner).isTrue();
  }

  @DisplayName("부스 소유 여부를 확인한다 - 비소유")
  @Test
  public void testIsNotBoothOwner() {
    // given
    List<Booth> booths = boothJpaRepository.findAll();
    Booth boothA = booths.get(0);
    Booth boothB = booths.get(1);
    Admin adminB = boothB.getOwner();

    // when
    boolean isOwner = boothQueryService.isBoothOwner(boothA.getId(), adminB.getId()).getIsOwner();

    // then
    assertThat(isOwner).isFalse();
  }

  @DisplayName("RedisBoothQrManager 를 사용하여 부스 QR 코드를 생성한다.")
  @Test
  public void testCreateBoothQrRedisBoothQrManager() {
    // given
    willReturn(redisBoothQrManager).given(boothQrManagerContext).boothQrManager();

    Booth booth = boothJpaRepository.findAll().get(0);
    Admin admin = booth.getOwner();

    // when
    String qrCode = createBoothQrCode(booth, admin);

    // then
    assertThat(qrCode).isNotNull();
  }

  @DisplayName("TokenBoothQrManager 를 사용하여 부스 QR 코드를 생성한다.")
  @Test
  public void testCreateBoothQrTokenBoothQrManager() {
    // given
    willReturn(tokenBoothQrManager).given(boothQrManagerContext).boothQrManager();

    Booth booth = boothJpaRepository.findAll().get(0);
    Admin admin = booth.getOwner();

    // when
    String qrCode = createBoothQrCode(booth, admin);

    // then
    assertThat(qrCode).isNotNull();
  }

  @DisplayName("RedisBoothQrManager 를 사용하여 해당 부스의 관리자가 아니라면 QR 코드를 생성할 수 없다.")
  @Test
  public void testCreateBoothQrRedisBoothQrManagerNotOwner() {
    // given
    willReturn(redisBoothQrManager).given(boothQrManagerContext).boothQrManager();

    List<Booth> booths = boothJpaRepository.findAll();
    Booth boothA = booths.get(0);
    Booth boothB = booths.get(1);

    Admin adminB = boothB.getOwner();

    // when & then
    assertThatThrownBy(() -> createBoothQrCode(boothA, adminB)).isInstanceOf(
        ForbiddenException.class);
  }

  @DisplayName("TokenBoothQrManager 를 사용하여 해당 부스의 관리자가 아니라면 QR 코드를 생성할 수 없다.")
  @Test
  public void testCreateBoothQrTokenBoothQrManagerNotOwner() {
    // given
    willReturn(tokenBoothQrManager).given(boothQrManagerContext).boothQrManager();

    List<Booth> booths = boothJpaRepository.findAll();
    Booth boothA = booths.get(0);
    Booth boothB = booths.get(1);

    Admin adminB = boothB.getOwner();

    // when & then
    assertThatThrownBy(() -> createBoothQrCode(boothA, adminB)).isInstanceOf(
        ForbiddenException.class);
  }

  private String createBoothQrCode(Booth booth, Admin admin) {
    return boothQueryService.getBoothQr(booth.getId(), admin.getId()).getQrCode();
  }
}

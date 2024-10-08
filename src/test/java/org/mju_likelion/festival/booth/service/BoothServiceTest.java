package org.mju_likelion.festival.booth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.willReturn;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothUser;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.booth.domain.repository.BoothUserJpaRepository;
import org.mju_likelion.festival.booth.dto.request.UpdateBoothRequest;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.util.qr.BoothQrStrategy;
import org.mju_likelion.festival.booth.util.qr.manager.BoothQrManagerContext;
import org.mju_likelion.festival.booth.util.qr.manager.RedisBoothQrManager;
import org.mju_likelion.festival.booth.util.qr.manager.TokenBoothQrManager;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.exception.ConflictException;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.user.domain.User;
import org.mju_likelion.festival.user.domain.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("BoothService")
@ApplicationTest
@Transactional
public class BoothServiceTest {

  @Autowired
  private BoothService boothService;
  @Autowired
  private BoothQueryService boothQueryService;
  @Autowired
  private BoothJpaRepository boothJpaRepository;
  @Autowired
  private UserJpaRepository userJpaRepository;
  @Autowired
  private BoothUserJpaRepository boothUserJpaRepository;
  @MockBean
  private BoothQrManagerContext boothQrManagerContext;
  @Autowired
  private RedisBoothQrManager redisBoothQrManager;
  @Autowired
  private TokenBoothQrManager tokenBoothQrManager;
  private User user;
  private Booth booth;
  private Admin admin;

  @BeforeEach
  void setUp() {
    booth = boothJpaRepository.findAllByIsEventBooth(true).get(0);
    user = userJpaRepository.findAll().get(0);
    admin = booth.getOwner();
  }

  @AfterEach
  void cleanUp() {
    cleanUpBoothUser();
  }

  @DisplayName("RedisBoothQrManager 를 사용하여 발급받은 QR 코드를 통해 부스에 방문 처리를 한다.")
  @Test
  void testVisitBoothByQrCode() {
    // given
    willReturn(redisBoothQrManager).given(boothQrManagerContext).boothQrManager();
    willReturn(redisBoothQrManager).given(boothQrManagerContext)
        .boothQrManager(BoothQrStrategy.REDIS);

    BoothQrResponse boothQrResponse = boothQueryService.getBoothQr(booth.getId(), admin.getId());
    String qrId = getQrIdFromQrCode(boothQrResponse.getQrCode());

    // when
    boothService.visitBooth(qrId, BoothQrStrategy.REDIS, user.getId());

    // then
    Optional<BoothUser> boothUser = boothUserJpaRepository.findByUserAndBooth(user, booth);
    assertThat(boothUser).isPresent();
  }

  @DisplayName("TokenBoothQrManager 를 사용하여 발급받은 QR 코드를 통해 부스에 방문 처리를 한다.")
  @Test
  void testVisitBoothByQrCodeWithTokenBoothQrManager() {
    // given
    willReturn(tokenBoothQrManager).given(boothQrManagerContext).boothQrManager();
    willReturn(tokenBoothQrManager).given(boothQrManagerContext)
        .boothQrManager(BoothQrStrategy.TOKEN);

    BoothQrResponse boothQrResponse = boothQueryService.getBoothQr(booth.getId(), admin.getId());
    String qrId = getQrIdFromQrCode(boothQrResponse.getQrCode());

    // when
    boothService.visitBooth(qrId, BoothQrStrategy.TOKEN, user.getId());

    // then
    Optional<BoothUser> boothUser = boothUserJpaRepository.findByUserAndBooth(user, booth);
    assertThat(boothUser).isPresent();
  }

  @DisplayName("RedisBoothQrManager 를 사용하여 이미 방문한 부스의 QR 방문 처리를 하면 예외가 발생한다.")
  @Test
  void testVisitBoothByQrCodeAlreadyVisited() {
    // given
    willReturn(redisBoothQrManager).given(boothQrManagerContext).boothQrManager();
    willReturn(redisBoothQrManager).given(boothQrManagerContext)
        .boothQrManager(BoothQrStrategy.REDIS);

    BoothQrResponse boothQrResponse = boothQueryService.getBoothQr(booth.getId(), admin.getId());
    String qrId = getQrIdFromQrCode(boothQrResponse.getQrCode());

    boothService.visitBooth(qrId, BoothQrStrategy.REDIS, user.getId());

    // when & then
    assertThatThrownBy(() -> boothService.visitBooth(qrId, BoothQrStrategy.REDIS, user.getId()))
        .isInstanceOf(NotFoundException.class);
  }

  @DisplayName("TokenBoothQrManager 를 사용하여 이미 방문한 부스의 QR 방문 처리를 하면 예외가 발생한다.")
  @Test
  void testVisitBoothByQrCodeAlreadyVisitedWithTokenBoothQrManager() {
    // given
    willReturn(tokenBoothQrManager).given(boothQrManagerContext).boothQrManager();
    willReturn(tokenBoothQrManager).given(boothQrManagerContext)
        .boothQrManager(BoothQrStrategy.TOKEN);

    BoothQrResponse boothQrResponse = boothQueryService.getBoothQr(booth.getId(), admin.getId());
    String qrId = getQrIdFromQrCode(boothQrResponse.getQrCode());
    boothService.visitBooth(qrId, BoothQrStrategy.TOKEN, user.getId());

    // when & then
    assertThatThrownBy(() -> boothService.visitBooth(qrId, BoothQrStrategy.TOKEN, user.getId()))
        .isInstanceOf(ConflictException.class);
  }

  @DisplayName("부스 설명을 업데이트한다.")
  @Test
  void testUpdateBoothDescription() {
    // given
    UpdateBoothRequest updateBoothRequest = new UpdateBoothRequest("new description");

    // when
    boothService.updateBooth(booth.getId(), updateBoothRequest, admin.getId());

    // then
    Booth updatedBooth = boothJpaRepository.findById(booth.getId()).get();
    assertThat(updatedBooth.getBoothInfo().getDescription()).isEqualTo(
        updateBoothRequest.getDescription());
  }

  private String getQrIdFromQrCode(String qrCode) {
    return qrCode.split("/")[4].split("\\?")[0];
  }

  private void cleanUpBoothUser() {
    Optional<BoothUser> boothUser = boothUserJpaRepository.findByUserAndBooth(user, booth);
    boothUser.ifPresent(boothUserJpaRepository::delete);
  }
}

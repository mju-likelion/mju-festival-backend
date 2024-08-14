package org.mju_likelion.festival.booth.util.qr;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mju_likelion.festival.booth.domain.BoothQrStrategy;
import org.mju_likelion.festival.common.util.redis.RedisAvailabilityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 부스 QR 코드 관리자 컨텍스트.
 */
@Service
public class BoothQrManagerContext {

  private final RedisAvailabilityChecker redisAvailabilityChecker;
  private final Map<BoothQrStrategy, BoothQrManager> boothQrManagers;

  @Autowired
  public BoothQrManagerContext(
      final RedisAvailabilityChecker redisAvailabilityChecker,
      final List<BoothQrManager> boothQrManagerList) {

    this.redisAvailabilityChecker = redisAvailabilityChecker;
    this.boothQrManagers = boothQrManagerList.stream()
        .collect(Collectors.toMap(BoothQrManager::boothQrStrategy, Function.identity()));
  }

  private BoothQrManager redisBoothQrManager() {
    return boothQrManagers.get(BoothQrStrategy.REDIS);
  }

  private BoothQrManager tokenBoothQrManager() {
    return boothQrManagers.get(BoothQrStrategy.TOKEN);
  }

  /**
   * 사용 가능한 부스 QR 코드 관리자를 반환한다.
   *
   * @return 사용 가능한 부스 QR 코드 관리자
   */
  public BoothQrManager boothQrManager() {
    if (redisAvailabilityChecker.isAvailable()) {
      return redisBoothQrManager();
    }
    return tokenBoothQrManager();
  }

  /**
   * BoothQrStrategy 에 해당하는 부스 QR 코드 관리자를 반환한다.
   *
   * @param boothQrStrategy 부스 QR 전략
   * @return 부스 QR 코드 관리자
   */
  public BoothQrManager boothQrManager(final BoothQrStrategy boothQrStrategy) {
    return boothQrManagers.get(boothQrStrategy);
  }
}

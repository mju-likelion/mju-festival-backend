package org.mju_likelion.festival.common.util.redis;

import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.config.RedisConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAvailabilityChecker {

  private static final long REDIS_CHECK_INTERVAL = 10000; // 10초
  private final RedisConfig redisConfig;
  private volatile boolean redisAvailable = false; // 멀티스레드 환경 가시성 보장

  public boolean isAvailable() {
    return redisAvailable;
  }

  /**
   * Redis 연결 상태를 주기적으로 확인하여 업데이트한다.
   */
  @Scheduled(fixedDelay = REDIS_CHECK_INTERVAL)
  public void updateAvailability() {
    redisAvailable = redisConfig.isConnectionAvailable();
  }
}

package org.mju_likelion.festival.auth.util.key.manager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.common.util.redis.RedisAvailabilityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RsaKeyManager 컨텍스트
 * <p>
 * Redis 가 사용 가능한 경우 RedisRsaKeyManager 를 사용하고, 그렇지 않은 경우 TokenRsaKeyManager 를 사용한다.
 */
@Service
public class RsaKeyManagerContext {

  private final RedisAvailabilityChecker redisAvailabilityChecker;
  private final Map<RsaKeyStrategy, RsaKeyManager> rsaKeyManagers;

  @Autowired
  public RsaKeyManagerContext(
      final RedisAvailabilityChecker redisAvailabilityChecker,
      final List<RsaKeyManager> rsaKeyManagerList) {

    this.redisAvailabilityChecker = redisAvailabilityChecker;
    this.rsaKeyManagers = rsaKeyManagerList.stream()
        .collect(Collectors.toMap(RsaKeyManager::rsaKeyStrategy, Function.identity()));
  }

  /**
   * RedisRsaKeyManager 를 반환한다.
   *
   * @return RedisRsaKeyManager
   */
  private RsaKeyManager redisRsaKeyManager() {
    return rsaKeyManagers.get(RsaKeyStrategy.REDIS);
  }

  /**
   * TokenRsaKeyManager 를 반환한다.
   *
   * @return TokenRsaKeyManager
   */
  private RsaKeyManager tokenRsaKeyManager() {
    return rsaKeyManagers.get(RsaKeyStrategy.TOKEN);
  }

  /**
   * 사용 가능한 RsaKeyManager 를 반환한다.
   *
   * @return RsaKeyManager
   */
  public RsaKeyManager rsaKeyManager() {
    if (redisAvailabilityChecker.isAvailable()) {
      return redisRsaKeyManager();
    }
    return tokenRsaKeyManager();
  }

  /**
   * 전략에 따른 RsaKeyManager 를 반환한다.
   *
   * @param rsaKeyStrategy RsaKey 전략
   * @return RsaKeyManager
   */
  public RsaKeyManager rsaKeyManager(final RsaKeyStrategy rsaKeyStrategy) {
    return rsaKeyManagers.get(rsaKeyStrategy);
  }
}

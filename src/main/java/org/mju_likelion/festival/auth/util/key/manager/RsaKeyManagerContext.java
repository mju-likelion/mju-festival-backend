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
  public RsaKeyManagerContext(RedisAvailabilityChecker redisAvailabilityChecker,
      List<RsaKeyManager> rsaKeyManagerList) {
    this.redisAvailabilityChecker = redisAvailabilityChecker;
    this.rsaKeyManagers = rsaKeyManagerList.stream()
        .collect(Collectors.toMap(RsaKeyManager::rsaKeyStrategy, Function.identity()));
  }

  private RsaKeyManager redisRsaKeyManager() {
    return rsaKeyManagers.get(RsaKeyStrategy.REDIS);
  }

  private RsaKeyManager tokenRsaKeyManager() {
    return rsaKeyManagers.get(RsaKeyStrategy.TOKEN);
  }

  public RsaKeyManager rsaKeyManager() {
    if (redisAvailabilityChecker.isAvailable()) {
      return redisRsaKeyManager();
    }
    return tokenRsaKeyManager();
  }

  public RsaKeyManager rsaKeyManager(RsaKeyStrategy rsaKeyStrategy) {
    return rsaKeyManagers.get(rsaKeyStrategy);
  }
}

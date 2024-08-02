package org.mju_likelion.festival.auth.util.key.manager;

import static org.mju_likelion.festival.common.exception.type.ErrorType.CREDENTIAL_KEY_INVALID;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.domain.RsaKey;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.key.RsaKeyUtil;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.mju_likelion.festival.common.util.redis.RedisUtil;
import org.springframework.stereotype.Service;

/**
 * Redis 를 이용한 RsaKeyManager
 *
 * @see RsaKeyManager
 */
@Service
@AllArgsConstructor
public class RedisRsaKeyManager implements RsaKeyManager {

  private final RedisUtil<String, String> redisUtil;
  private final RsaKeyUtil rsaKeyUtil;
  private final String keyPrefix = "PRIVATE_KEY:";

  @Override
  public RsaKey generateRsaKey() {
    return rsaKeyUtil.generateRsaKey();
  }

  @Override
  public String savePrivateKey(String privateKey) {
    String redisKey = UUID.randomUUID().toString();
    redisUtil.insert(formatRedisKey(redisKey), privateKey, this.rsaKeyExpireSecond);
    return redisKey;
  }

  @Override
  public String decryptByKey(String encryptedText, String key) {
    String redisKey = formatRedisKey(key);

    String privateKey = getPrivateKey(redisKey);
    deletePrivateKey(redisKey);
    return rsaKeyUtil.rsaDecode(encryptedText, privateKey);
  }

  @Override
  public RsaKeyStrategy rsaKeyStrategy() {
    return RsaKeyStrategy.REDIS;
  }

  private void deletePrivateKey(String key) {
    redisUtil.delete(formatRedisKey(key));
  }

  private String getPrivateKey(String key) {
    return redisUtil.select(key).orElseThrow(
        () -> new InternalServerException(CREDENTIAL_KEY_INVALID));
  }

  private String formatRedisKey(String key) {
    return keyPrefix + key;
  }
}

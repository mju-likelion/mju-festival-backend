package org.mju_likelion.festival.common.util.redis;


import static org.mju_likelion.festival.common.exception.type.ErrorType.REDIS_ERROR;

import java.time.Duration;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisUtil<K, V> {

  private final RedisTemplate<K, V> redisTemplate;

  public void insert(final K key, final V value, final long offset) {
    try {
      this.redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(offset));
    } catch (Exception e) {
      throw new InternalServerException(REDIS_ERROR, e.getMessage());
    }
  }

  public Optional<V> select(final K key) {
    try {
      V value = this.redisTemplate.opsForValue().get(key);
      return Optional.ofNullable(value);
    } catch (Exception e) {
      throw new InternalServerException(REDIS_ERROR, e.getMessage());
    }
  }

  public void delete(final K key) {
    try {
      this.redisTemplate.delete(key);
    } catch (Exception e) {
      throw new InternalServerException(REDIS_ERROR, e.getMessage());
    }
  }
}

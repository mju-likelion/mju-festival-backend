package org.mju_likelion.festival.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.lettuce.core.RedisException;
import java.time.Duration;
import org.mju_likelion.festival.common.exception.CustomException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;

@Configuration
public class Resilience4jConfig {

  public static final String REDIS_CIRCUIT_BREAKER = "redis";

  @Bean
  public CircuitBreakerRegistry redisCircuitBreakerRegistry() {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(80)
        .slidingWindowSize(10)
        .permittedNumberOfCallsInHalfOpenState(5)
        .waitDurationInOpenState(Duration.ofHours(1))
        .recordExceptions(RedisException.class, RedisSystemException.class,
            RedisConnectionFailureException.class)
        .ignoreExceptions(CustomException.class)
        .build();

    CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
    circuitBreakerRegistry.circuitBreaker(REDIS_CIRCUIT_BREAKER, circuitBreakerConfig);
    return circuitBreakerRegistry;
  }
}

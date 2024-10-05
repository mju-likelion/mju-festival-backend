package org.mju_likelion.festival.common.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.DefaultClientResources;
import java.time.Duration;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;
  @Value("${spring.data.redis.port}")
  private int redisPort;
  @Value("${spring.data.redis.password}")
  private String redisPassword;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
    redisConfiguration.setHostName(redisHost);
    redisConfiguration.setPort(redisPort);
    redisConfiguration.setPassword(redisPassword);

    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .clientResources(DefaultClientResources.create())
        .clientOptions(ClientOptions.builder()
            .autoReconnect(true) // 자동 재연결
            .disconnectedBehavior(
                ClientOptions.DisconnectedBehavior.REJECT_COMMANDS) // 연결이 끊겼을 때 명령 거부
            .build())
        .build();

    return new LettuceConnectionFactory(redisConfiguration, clientConfig);
  }

  @Bean
  public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<K, V> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .disableCachingNullValues()
                .serializeKeysWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer()))
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()))
        )
        .build();
  }

  /**
   * Redis 서버와 연결 가능한지 확인한다.
   *
   * @return 연결 가능 여부
   */
  public boolean isConnectionAvailable() {
    try (RedisConnection connection = RedisConnectionUtils.getConnection(
        redisConnectionFactory())) {
      return Objects.equals(connection.ping(), "PONG");
    } catch (Exception e) {
      return false;
    }
  }
}
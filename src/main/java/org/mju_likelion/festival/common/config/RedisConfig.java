package org.mju_likelion.festival.common.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.DefaultClientResources;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;
  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost,
        redisPort);

    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .clientResources(DefaultClientResources.create())
        .clientOptions(ClientOptions.builder()
            .autoReconnect(true) // 자동 재연결
            .disconnectedBehavior(
                ClientOptions.DisconnectedBehavior.REJECT_COMMANDS) // 연결이 끊겼을 때 명령 거부
            .build())
        .build();

    return new LettuceConnectionFactory(serverConfig, clientConfig);
  }

  @Bean
  public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<K, V> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
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
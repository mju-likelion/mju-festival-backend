package org.mju_likelion.festival.booth.util.qr;

import static org.mju_likelion.festival.common.exception.type.ErrorType.BOOTH_QR_NOT_FOUND_ERROR;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothQrStrategy;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.common.util.qr.QrGenerator;
import org.mju_likelion.festival.common.util.redis.RedisUtil;
import org.springframework.stereotype.Component;

/**
 * Redis 를 이용한 부스 QR 코드를 관리하는 클래스.
 *
 * @see BoothQrManager
 */
@Component
@AllArgsConstructor
public class RedisBoothQrManager implements BoothQrManager {

  private final QrGenerator qrGenerator;
  private final RedisUtil<UUID, UUID> redisUtil;

  @Override
  public String generateBoothQr(final UUID boothId) {
    UUID qrId = UUID.randomUUID();
    redisUtil.insert(qrId, boothId, qrExpireTime);
    return qrGenerator.generateQrCode("/booths/", qrId.toString(), getQuery());
  }

  @Override
  public UUID getBoothIdFromQrId(final String qrId) {
    UUID redisKey = UUID.fromString(qrId);

    UUID boothId = getBoothId(redisKey);
    deleteBoothId(redisKey);

    return boothId;
  }

  @Override
  public BoothQrStrategy boothQrStrategy() {
    return BoothQrStrategy.REDIS;
  }

  private UUID getBoothId(UUID qrId) {
    return redisUtil.select(qrId)
        .orElseThrow(() -> new NotFoundException(BOOTH_QR_NOT_FOUND_ERROR));
  }

  private void deleteBoothId(UUID qrId) {
    redisUtil.delete(qrId);
  }
}

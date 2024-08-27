package org.mju_likelion.festival.booth.util.qr.manager;

import java.util.Map;
import java.util.UUID;
import org.mju_likelion.festival.booth.util.qr.BoothQrStrategy;

/**
 * 부스 QR 코드를 관리하는 인터페이스.
 *
 * @implSpec RedisBoothQrManager, TokenBoothQrManager
 */
public interface BoothQrManager {

  int qrExpireTime = 30; // qr 만로 시간 (30 초)
  String path = "/booths/"; // qr 코드 경로

  /**
   * 부스 QR 코드를 생성한다.
   *
   * @param boothId 부스 ID
   * @return QR 코드 URL
   */
  String generateBoothQr(final UUID boothId);

  /**
   * QR 코드로 부스 ID를 가져온다.
   *
   * @param qrId QR 코드 ID
   * @return 부스 ID
   */
  UUID getBoothIdFromQrId(final String qrId);

  /**
   * 부스 QR 전략을 반환한다.
   *
   * @return 부스 QR 전략
   */
  BoothQrStrategy boothQrStrategy();

  /**
   * QR 코드 생성에 필요한 쿼리 맵을 반환한다.
   *
   * @return 쿼리 맵
   */
  default Map<String, String> getQuery() {
    return Map.of("strategy", boothQrStrategy().toString()); // 전략 정보를 담은 맵
  }
}

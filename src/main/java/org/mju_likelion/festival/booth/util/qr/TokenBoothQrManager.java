package org.mju_likelion.festival.booth.util.qr;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothQrStrategy;
import org.mju_likelion.festival.common.util.qr.QrGenerator;
import org.mju_likelion.festival.common.util.token.TokenUtil;
import org.springframework.stereotype.Component;

/**
 * Token 을 이용한 부스 QR 코드를 관리하는 클래스.
 *
 * @see BoothQrManager
 */
@Component
@AllArgsConstructor
public class TokenBoothQrManager implements BoothQrManager {

  private final QrGenerator qrGenerator;
  private final TokenUtil tokenUtil;

  @Override
  public String generateBoothQr(final UUID boothId) {
    String encryptedToken = tokenUtil.getEncryptedToken(boothId.toString(), this.qrExpireTime);
    return qrGenerator.generateQrCode("/booths/", encryptedToken, getQuery());
  }

  @Override
  public UUID getBoothIdFromQrId(final String qrId) {
    return UUID.fromString(tokenUtil.parseValue(qrId));
  }

  @Override
  public BoothQrStrategy boothQrStrategy() {
    return BoothQrStrategy.TOKEN;
  }
}

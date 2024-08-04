package org.mju_likelion.festival.booth.util.qr;

import java.util.Base64;
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
    return qrGenerator.generateQrCode("/booths/",
        Base64.getEncoder().encodeToString(encryptedToken.getBytes()),
        getQuery());
  }

  @Override
  public UUID getBoothIdFromQrId(final String qrId) {
    String base64Decoded = new String(Base64.getDecoder().decode(qrId));
    return UUID.fromString(tokenUtil.parseValue(base64Decoded));
  }

  @Override
  public BoothQrStrategy boothQrStrategy() {
    return BoothQrStrategy.TOKEN;
  }
}

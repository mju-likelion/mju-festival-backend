package org.mju_likelion.festival.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;

/**
 * Key 응답 DTO.
 */
@Getter
@AllArgsConstructor
public class KeyResponse {

  private final String rsaPublicKey;
  private final String credentialKey;
  private final RsaKeyStrategy rsaKeyStrategy;

  @Override
  public String toString() {
    return "KeyResponse{" +
        "rsaPublicKey='" + rsaPublicKey + '\'' +
        ", credentialKey='" + credentialKey + '\'' +
        ", rsaKeyStrategy=" + rsaKeyStrategy +
        '}';
  }
}

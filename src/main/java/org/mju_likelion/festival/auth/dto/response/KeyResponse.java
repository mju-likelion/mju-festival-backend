package org.mju_likelion.festival.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;

@Getter
@AllArgsConstructor
public class KeyResponse {

  private final String rsaPublicKey;
  private final String credentialKey;
  private final RsaKeyStrategy rsaKeyStrategy;
}
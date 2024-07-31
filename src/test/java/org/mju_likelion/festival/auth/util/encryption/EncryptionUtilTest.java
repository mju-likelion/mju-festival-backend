package org.mju_likelion.festival.auth.util.encryption;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("EncryptionUtil")
@ApplicationTest
public class EncryptionUtilTest {

  @Autowired
  private EncryptionUtil encryptionUtil;

  private static Stream<String> plainTexts() {
    return Stream.of(
        "asdfsagsaewr",
        "asfdccc",
        "12132353246234234"
    );
  }

  @DisplayName("암호화된 문자열을 복호화하면 원래 문자열이 나온다.")
  @ParameterizedTest(name = "plainText: {0}")
  @MethodSource("plainTexts")
  void testEncryptDecrypt(String plainText) {
    // when
    String encryptedText = encryptionUtil.encrypt(plainText);
    String decryptedText = encryptionUtil.decrypt(encryptedText);

    // then
    assertThat(decryptedText).isEqualTo(plainText);
  }

}

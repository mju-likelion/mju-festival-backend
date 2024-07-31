package org.mju_likelion.festival.auth.util.key;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mju_likelion.festival.auth.domain.RsaKey;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("RsaKeyUtil")
@ApplicationTest
public class RsaKeyUtilTest {

  @Autowired
  private RsaKeyUtil rsaKeyUtil;

  private static Stream<String> plainTexts() {
    return Stream.of(
        " ",
        "",
        "asdfsagsaewr",
        "asfdccc",
        "12132353246234234"
    );
  }

  private static Stream<String> invalidPrivateKeys() {
    return Stream.of(
        " ",
        "",
        "asdfsagsaewr",
        "asfdccc",
        "12132353246234234"
    );
  }

  @DisplayName("Public Key 로 암호화된 문자열을 쌍이 맞는 Private Key 로 복호화하면 원래 문자열이 나온다.")
  @ParameterizedTest(name = "plainText: {0}")
  @MethodSource("plainTexts")
  void testRsaEncodeDecode(String plainText) {
    RsaKey rsaKey = rsaKeyUtil.generateRsaKey();
    String publicKey = rsaKey.publicKey();
    String privateKey = rsaKey.privateKey();

    // when
    String encryptedText = rsaKeyUtil.rsaEncode(plainText, publicKey);
    String decryptedText = rsaKeyUtil.rsaDecode(encryptedText, privateKey);

    // then
    assertThat(decryptedText).isEqualTo(plainText);
  }

  @DisplayName("Public Key 로 복호화하면 예외가 발생한다.")
  @ParameterizedTest(name = "plainText: {0}")
  @MethodSource("plainTexts")
  void testRsaDecodeWithPublicKey(String plainText) {
    //given
    RsaKey rsaKey = rsaKeyUtil.generateRsaKey();
    String publicKey = rsaKey.publicKey();
    String encryptedText = rsaKeyUtil.rsaEncode(plainText, publicKey);

    // when, then
    assertThatThrownBy(() -> rsaKeyUtil.rsaDecode(encryptedText, publicKey))
        .isInstanceOf(RuntimeException.class);
  }

  @DisplayName("Public Key 로 암호화하면 예외가 발생한다.")
  @ParameterizedTest(name = "plainText: {0}")
  @MethodSource("plainTexts")
  void testRsaEncodeWithPublicKey(String plainText) {
    //given
    RsaKey rsaKey = rsaKeyUtil.generateRsaKey();
    String privateKey = rsaKey.privateKey();

    // when, then
    assertThatThrownBy(() -> rsaKeyUtil.rsaEncode(plainText, privateKey))
        .isInstanceOf(RuntimeException.class);
  }

  @DisplayName("다른 Public Key 로 암호화된 문자열을 복호화하면 예외가 발생한다.")
  @ParameterizedTest(name = "plainText: {0}")
  @MethodSource("plainTexts")
  void testRsaDecodeFromAnotherPublicKey(String plainText) {
    //given
    RsaKey originRsaKey = rsaKeyUtil.generateRsaKey();
    String originPrivateKey = originRsaKey.privateKey();

    RsaKey anotherRsaKey = rsaKeyUtil.generateRsaKey();
    String anotherPublicKey = anotherRsaKey.publicKey();

    // when
    String encryptedText = rsaKeyUtil.rsaEncode(plainText, anotherPublicKey);

    // when, then
    assertThatThrownBy(() -> rsaKeyUtil.rsaDecode(encryptedText, originPrivateKey))
        .isInstanceOf(RuntimeException.class);
  }

  @DisplayName("잘못된 Private Key 로 복호화하면 예외가 발생한다.")
  @ParameterizedTest(name = "invalidPrivateKey: {0}")
  @MethodSource("invalidPrivateKeys")
  void testRsaDecodeWithInvalidPrivateKey(String plainText) {
    //given
    RsaKey rsaKey = rsaKeyUtil.generateRsaKey();
    String publicKey = rsaKey.publicKey();

    // when
    String encryptedText = rsaKeyUtil.rsaEncode(plainText, publicKey);

    // when, then
    assertThatThrownBy(() -> rsaKeyUtil.rsaDecode(encryptedText, "invalidPrivateKey"))
        .isInstanceOf(RuntimeException.class);
  }

  @DisplayName("Private Key 가 null 이면 예외가 발생한다.")
  @Test
  void testRsaDecodeWithNullPrivateKey() {
    //given
    RsaKey rsaKey = rsaKeyUtil.generateRsaKey();
    String publicKey = rsaKey.publicKey();

    // when
    String encryptedText = rsaKeyUtil.rsaEncode("plainText", publicKey);

    // when, then
    assertThatThrownBy(() -> rsaKeyUtil.rsaDecode(encryptedText, null))
        .isInstanceOf(RuntimeException.class);
  }

  @DisplayName("Plain Text 가 null 이면 예외가 발생한다.")
  @Test
  void testRsaEncodeWithNullPlainText() {
    //given
    RsaKey rsaKey = rsaKeyUtil.generateRsaKey();
    String publicKey = rsaKey.publicKey();

    // when, then
    assertThatThrownBy(() -> rsaKeyUtil.rsaEncode(null, publicKey))
        .isInstanceOf(RuntimeException.class);
  }
}

package org.mju_likelion.festival.auth.util.rsa_key;

/**
 * RSA Key 정보 클래스
 *
 * @param publicKey  RSA Public Key
 * @param privateKey RSA Private Key
 */
public record RsaKey(String publicKey, String privateKey) {

}

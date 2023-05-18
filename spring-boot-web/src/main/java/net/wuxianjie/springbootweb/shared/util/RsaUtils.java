package net.wuxianjie.springbootweb.shared.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * RSA 加密算法相关工具类。
 *
 * @author 吴仙杰
 **/
public class RsaUtils {

  /**
   * 生成新的 Base64 公私钥对。
   *
   * @return Base64 公私钥对
   */
  public static KeyPair generateKeyPair() {
    final RSA rsa = new RSA();
    final String privateKeyBase64 = rsa.getPrivateKeyBase64();
    final String publicKeyBase64 = rsa.getPublicKeyBase64();

    return new KeyPair(privateKeyBase64, publicKeyBase64);
  }

  /**
   * 将字节数组加密为大写十六进制字符串。
   *
   * @param privateKey Base64 私钥
   * @param rawBytes 要被加密的原始字节数组
   * @return 大写十六进制字符串
   */
  public static String encrypt(final String privateKey, final byte[] rawBytes) {
    final RSA rsa = new RSA(privateKey, null);
    final byte[] encryptedBytes = rsa.encrypt(rawBytes, KeyType.PrivateKey);

    return HexUtil.encodeHexStr(encryptedBytes, false);
  }

  /**
   * 使用 UTF-8 字符编码对十六进制或 Base64 字符串进行解密。
   *
   * @param publicKey Base64 公钥
   * @param encryptedData 已加密的十六进制或 Base64 字符串
   * @return 解密后的字节数据
   */
  public static byte[] decrypt(final String publicKey, final String encryptedData) {
    final RSA rsa = new RSA(null, publicKey);

    return rsa.decrypt(encryptedData, KeyType.PublicKey);
  }

  /**
   * Base64 字符串格式的密钥对。
   *
   * @param privateKey Base64 私钥
   * @param publicKey Base64 公钥
   */
  public record KeyPair(String privateKey, String publicKey) {
  }
}

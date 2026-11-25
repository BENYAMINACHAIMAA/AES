
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EcbMode {

  public static byte[] encrypt(byte[] data, byte[] key)
      throws Exception {

    if (data == null) {
      throw new IllegalArgumentException("Data must not be null");
    }

    if (key == null ||
    (key.length != 16 && key.length != 24 && key.length != 32)) {
    throw new IllegalArgumentException("Invalid AES key size");
}

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    return cipher.doFinal(data);
  }

  public static byte[] decrypt(byte[] encryptedData, byte[] key)
      throws Exception {

    if (encryptedData == null || encryptedData.length == 0) {
      throw new IllegalArgumentException("Invalid ciphertext");
    }

    if (key == null ||
    (key.length != 16 && key.length != 24 && key.length != 32)) {
    throw new IllegalArgumentException("Invalid AES key size");
}

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    return cipher.doFinal(encryptedData);
  }
}

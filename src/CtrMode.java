
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CtrMode {

  private static void incrementCounter(byte[] counter) {

    for (int i = counter.length - 1; i >= 0; i--) {

      counter[i]++;

      if ((counter[i] & 0xFF) != 0) {
        break;
      }
    }
  }

  private static byte[] encryptCounter(byte[] counter, byte[] key)
      throws Exception {

    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    return cipher.doFinal(counter);
  }

  public static byte[] encrypt(byte[] data, byte[] key, byte[] counter)
      throws Exception {

    if (data == null) {
      throw new IllegalArgumentException("Data must not be null");
    }

    if (key == null ||
        (key.length != 16 && key.length != 24 && key.length != 32)) {
      throw new IllegalArgumentException("Invalid AES key size");
    }

    if (counter == null || counter.length != 16) {
      throw new IllegalArgumentException("Counter must be 16 bytes");
    }

    byte[] cipherText = new byte[data.length];

    byte[] currentCounter = counter.clone();

    for (int i = 0; i < data.length; i += 16) {

      byte[] keystream = encryptCounter(currentCounter, key);

      int blockSize = Math.min(16, data.length - i);

      for (int j = 0; j < blockSize; j++) {
        cipherText[i + j] = (byte) (data[i + j] ^ keystream[j]);
      }

      incrementCounter(currentCounter);
    }

    return cipherText;
  }

  public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] counter)
      throws Exception {

    if (cipherText == null) {
      throw new IllegalArgumentException("Ciphertext must not be null");
    }

    if (key == null ||
        (key.length != 16 && key.length != 24 && key.length != 32)) {
      throw new IllegalArgumentException("Invalid AES key size");
    }

    if (counter == null || counter.length != 16) {
      throw new IllegalArgumentException("Counter must be 16 bytes");
    }

    byte[] plain = new byte[cipherText.length];

    byte[] currentCounter = counter.clone();

    for (int i = 0; i < cipherText.length; i += 16) {

      byte[] keystream = encryptCounter(currentCounter, key);

      int blockSize = Math.min(16, cipherText.length - i);

      for (int j = 0; j < blockSize; j++) {
        plain[i + j] = (byte) (cipherText[i + j] ^ keystream[j]);
      }

      incrementCounter(currentCounter);
    }

    return plain;
  }
}

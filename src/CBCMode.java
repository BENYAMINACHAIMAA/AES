
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CBCMode {

    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv)
            throws Exception {

        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }

        if (key == null ||
    (key.length != 16 && key.length != 24 && key.length != 32)) {
    throw new IllegalArgumentException("Invalid AES key size");
}

        if (iv == null || iv.length != 16) {
            throw new IllegalArgumentException("IV must be 16 bytes");
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv)
            throws Exception {

        if (encryptedData == null || encryptedData.length == 0) {
            throw new IllegalArgumentException("Invalid ciphertext");
        }

        if (key == null ||
    (key.length != 16 && key.length != 24 && key.length != 32)) {
    throw new IllegalArgumentException("Invalid AES key size");
}

        if (iv == null || iv.length != 16) {
            throw new IllegalArgumentException("IV must be 16 bytes");
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        return cipher.doFinal(encryptedData);
    }
}


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;

public class AES {

    public static byte[] encryptBlock(byte[] plain, byte[] key)
            throws Exception {

        if (key == null ||
            (key.length != 16 && key.length != 24 && key.length != 32)) {
            throw new IllegalArgumentException("Invalid AES key size");
        }

        if (plain == null || plain.length != 16) {
            throw new IllegalArgumentException("AES block must be 16 bytes");
        }

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(plain);
    }

    public static byte[] decryptBlock(byte[] encrypted, byte[] key)
            throws Exception {

        if (key == null ||
            (key.length != 16 && key.length != 24 && key.length != 32)) {
            throw new IllegalArgumentException("Invalid AES key size");
        }

        if (encrypted == null || encrypted.length != 16) {
            throw new IllegalArgumentException("AES block must be 16 bytes");
        }

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(encrypted);
    }

    public static byte[] readBytes(Scanner sc, int size, String name) {
        byte[] data = new byte[size];

        System.out.println("Enter " + name + " in Hex:");

        for (int i = 0; i < size; i++) {
            System.out.print("Octet " + (i + 1) + ": ");

            int value = Integer.parseInt(sc.nextLine(), 16);

            if (value < 0 || value > 255) {
                throw new IllegalArgumentException("Invalid octet");
            }

            data[i] = (byte) value;
        }

        return data;
    }
}

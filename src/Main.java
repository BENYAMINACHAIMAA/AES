
import java.io.IOException;
import java.util.Scanner;
import java.security.SecureRandom;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        SecureRandom random = new SecureRandom();

        byte[] data = FileUtils.readFile("data/input/photo.bmp");

        int pixelOffset = (data[10] & 0xFF)
                | ((data[11] & 0xFF) << 8)
                | ((data[12] & 0xFF) << 16)
                | ((data[13] & 0xFF) << 24);

        byte[] header = Arrays.copyOfRange(data, 0, pixelOffset);
        byte[] pixelData = Arrays.copyOfRange(data, pixelOffset, data.length);

        System.out.println("Pixel data starts at: " + pixelOffset);
        System.out.println("Header size: " + header.length);
        System.out.println("Pixel data size: " + pixelData.length);

        // AES-128 Key
        System.out.println("Choose AES key size:");
System.out.println("1. AES-128");
System.out.println("2. AES-192");
System.out.println("3. AES-256");

System.out.print("Your choice: ");
int choice = sc.nextInt();
sc.nextLine();
int keySize;

switch (choice) {
    case 1:
        keySize = 16;
        break;
    case 2:
        keySize = 24;
        break;
    case 3:
        keySize = 32;
        break;
    default:
        throw new IllegalArgumentException("Invalid choice!");
}

byte[] key = AES.readBytes(sc, keySize, "Key");

        // AES block size = 16 bytes
        byte[] iv = new byte[16];
        byte[] counter = new byte[16];

        random.nextBytes(iv);
        random.nextBytes(counter);

        // ECB
        byte[] encryptedEcb = EcbMode.encrypt(pixelData, key);
        byte[] decryptedEcb = EcbMode.decrypt(encryptedEcb, key);

        saveImage(header, encryptedEcb, pixelOffset,
                "data/output/encrypted_ecb.bmp");

        saveImage(header, decryptedEcb, pixelOffset,
                "data/output/decrypted_ecb.bmp");

        System.out.println("ECB - Same image: "
                + Arrays.equals(pixelData, decryptedEcb));

        // CBC
        byte[] encryptedCbc = CBCMode.encrypt(pixelData, key, iv);
        byte[] decryptedCbc = CBCMode.decrypt(encryptedCbc, key, iv);

        saveImage(header, encryptedCbc, pixelOffset,
                "data/output/encrypted_cbc.bmp");

        saveImage(header, decryptedCbc, pixelOffset,
                "data/output/decrypted_cbc.bmp");

        System.out.println("CBC - Same image: "
                + Arrays.equals(pixelData, decryptedCbc));

        // CTR
        byte[] encryptedCtr = CtrMode.encrypt(pixelData, key, counter);
        byte[] decryptedCtr = CtrMode.decrypt(encryptedCtr, key, counter);

        saveImage(header, encryptedCtr, pixelOffset,
                "data/output/encrypted_ctr.bmp");

        saveImage(header, decryptedCtr, pixelOffset,
                "data/output/decrypted_ctr.bmp");

        System.out.println("CTR - Same image: "
                + Arrays.equals(pixelData, decryptedCtr));

        sc.close();
    }

    public static void saveImage(byte[] header, byte[] pixels,
                                 int pixelOffset, String path)
            throws IOException {

        byte[] updatedHeader = header.clone();

        byte[] image = new byte[pixelOffset + pixels.length];

        System.arraycopy(updatedHeader, 0, image, 0, pixelOffset);
        System.arraycopy(pixels, 0, image, pixelOffset, pixels.length);

        // Update BMP file size (offset 2)
        writeIntLE(image, 2, image.length);

        // Update BMP image size (offset 34)
        writeIntLE(image, 34, pixels.length);

        FileUtils.writeFile(path, image);
    }

    private static void writeIntLE(byte[] data, int offset, int value) {

        data[offset]     = (byte) value;
        data[offset + 1] = (byte) (value >>> 8);
        data[offset + 2] = (byte) (value >>> 16);
        data[offset + 3] = (byte) (value >>> 24);
    }
}

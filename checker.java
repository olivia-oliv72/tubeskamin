import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class checker {
    // Generate SHA-256 Hash
    public static String generateHash(String name, String idNumber, String certificateNumber, String datePublished, String position) {
        String data = name + ", " + idNumber + ", " + certificateNumber + ", " + datePublished + ", " + position;

        // Initialize MessageDigest
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "Error";
        }

        // Generate hash bytes
        byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to bit string
        StringBuilder bitDigest = new StringBuilder();
        for (byte b : hashBytes) {
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            bitDigest.append(binary);
        }
        return bitDigest.toString();
    }

    // Extract the embedded hash from the image
    public static String extractHash(String imagePath, String certificateNumber) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            int width = image.getWidth();
            int height = image.getHeight();

            // Define the embedding area (same as used during embedding)
            int x_center = (int) Math.floor(width * 0.5);
            int y_center = (int) Math.floor(height * 0.5);
            int x_start = x_center - (int) Math.floor(width * 0.3);
            int x_end = x_center + (int) Math.floor(width * 0.3);
            int y_start = y_center - (int) Math.floor(height * 0.3);
            int y_end = y_center + (int) Math.floor(height * 0.3);

            int areaWidth = (int) Math.floor(width * 0.6);
            int areaHeight = (int) Math.floor(height * 0.6);
            int areaSize = areaWidth * areaHeight;
            int pixelSpacing = (int) ((areaSize / 256.0));

            // Check if certificate number is odd or even
            char[] oddNumbers = {'1', '3', '5', '7', '9'};
            boolean isOdd = false;
            for (char ch : oddNumbers) {
                if (certificateNumber.charAt(certificateNumber.length() - 1) == ch) {
                    isOdd = true;
                    break;
                }
            }

            StringBuilder extractedHash = new StringBuilder();
            int n = 1;

            // Extract bits from the embedding area
            // for (int y = y_start; y <= y_end; y++) {
            //     for (x = x; x <= x_end; x += pixelSpacing) {
            //         if (x < x_start) {
            //             x += x_start;
            //         }

            //         int pixel = image.getRGB(x, y);
            //         int colorChannel = isOdd ? (pixel & 0xFF) : ((pixel >> 8) & 0xFF); // Blue if odd, green if even
            //         int bit = colorChannel & 0x01; // Extract the least significant bit
            //         extractedHash.append(bit);

            //         if (extractedHash.length() >= 256) {
            //             break; // Stop once we've extracted 256 bits
            //         }
            //     }
            //     if (x > x_end) {
            //         x -= x_end;
            //     }
            // }
            System.out.print("Generated Hash: ");
            for (int y = y_start; y <= y_end && extractedHash.length() < 256; y++) {
                for (int x = x_start; x <= x_end && extractedHash.length() < 256; x++) {
                    if ((x - x_start + (y - y_start) * areaWidth) % pixelSpacing == 0) {
                        // Embedding logic here
                        int pixel = image.getRGB(x, y);
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;
            
                        int colorChannel = isOdd ? (pixel & 0xFF) : ((pixel >> 8) & 0xFF); // Blue if odd, green if even
                        int bit = colorChannel & 0x01; // Extract the least significant bit
                        extractedHash.append(bit);
                    }
                }
            }
            System.out.print(extractedHash);
            System.out.println();
            return extractedHash.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Main verification method
    public static boolean verifyCertificate(String imagePath, String name, String idNumber, String certificateNumber, String datePublished, String position) {
        // Extract the embedded hash
        String extractedHash = extractHash(imagePath, certificateNumber);
        if (extractedHash == null) {
            System.out.println("Failed to extract hash from image.");
            return false;
        }
        // System.out.println(extractedHash);

        // Recompute the hash from provided details
        String recomputedHash = generateHash(name, idNumber, certificateNumber, datePublished, position);
        System.out.println("Generated Hash: "+recomputedHash);

        // Compare the extracted hash and recomputed hash
        if (extractedHash.equals(recomputedHash)) {
            System.out.println("Certificate is verified as original.");
            return true;
        } else {
            System.out.println("Certificate is not original or has been tampered with.");
            return false;
        }
    }

    public static void main(String[] args) {
        // Example input details
        String imagePath = "output.png";
        String name = "Alice";
        String idNumber = "6182201000";
        String certificateNumber = "56789";
        String datePublished = "2024-11-30";
        String position = "Software Engineer";        

        // Verify the certificate
        boolean isOriginal = verifyCertificate(imagePath, name, idNumber, certificateNumber, datePublished, position);
        System.out.println("Verification Result: " + (isOriginal ? "Original" : "Not Original"));
    }
}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class fix {

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

    public static void main(String[] args) {
        try {
            // Example user data for watermarking
            String name = "Alice";
            String idNumber = "6182201000";
            String certificateNumber = "56789"; // Odd number
            String datePublished = "2024-11-30";
            String position = "Software Engineer";

            // Generate hash of user data
            String hash = generateHash(name, idNumber, certificateNumber, datePublished, position);
            System.out.println("Generated Hash: " + hash);

            // Path to the input image
            String inputImagePath = "Salam FTIS.jpg";
            File inputFile = new File(inputImagePath);
            if (!inputFile.exists()) {
                System.out.println("File not found: " + inputImagePath);
                return;
            }

            // Change image by embedding hash using LSB
            changeImage(inputImagePath, "output.png", certificateNumber, hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to embed hash into the image using LSB
    public static void changeImage(String inputImagePath, String outputImagePath, String certificateNumber, String digest) {
        try {
            // Read the image into a BufferedImage
            File inputFile = new File(inputImagePath);
            BufferedImage image = ImageIO.read(inputFile);
    
            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();
            System.out.println("panjang digest:" +digest.length());
    
            // Define the area where watermark will be embedded
            // int x_start = (int) Math.floor(width * 0.2);
            // int y_start = (int) Math.floor(height * 0.2);
            // int x_end = (int) Math.floor(width * 0.8);
            // int y_end = (int) Math.floor(height * 0.8);
            int x_center = (int) Math.floor(width * 0.5);
            int y_center = (int) Math.floor(height * 0.5);

            int x_start = x_center - (int) Math.floor(width * 0.3);
            int x_end = x_center + (int) Math.floor(width * 0.3);

            int y_start = y_center - (int) Math.floor(height * 0.3);
            int y_end = y_center + (int) Math.floor(height * 0.3);

            // Calculate pixel distance for LSB
            int areaWidth = (int) Math.floor(width * 0.6);
            int areaHeight = (int) Math.floor(height * 0.6);
            int areaSize = areaWidth * areaHeight;
            System.out.println("luas=" + areaSize);
            System.out.println("x_akhir=" + x_end + " y_akhir" + y_end);
            // BERUBAH
            int pixelSpacing = (int) ((areaSize / 256.0)); //Menghitung jarak antar pixel
            // int pixelSpacing = Math.max(1, areaSize / digest.length());

            // int pixelSpacing = (int) Math.floor(Math.sqrt(areaSize / 256.0));
            //pixel = 256 --> 1
            //pixel = 300 --> akar (1,17) = 1.08 --> 1
            //1, 2.17, 3.34, 4.51, 5.68, 6.85, 8.02, 9.12, 10.29, 11.46, 12.63
            //1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13
            //pixel = 680 --> 2.34
            //1, 3.34, 5.68, 
    
            // Check if certificate number ends with odd number (for blue or green channel)
            char[] oddNumbers = {'1', '3', '5', '7', '9'};
            boolean isOdd = false;
            for (char ch : oddNumbers) {
                if (certificateNumber.charAt(certificateNumber.length() - 1) == ch) {
                    isOdd = true;
                    break;
                }
            }
            
            // BERUBAH
            // int digestIndex = 0;
            // System.out.println("jarak= " + pixelSpacing);
            // // Iterate through pixels in the defined area and embed hash
            // int n = 1;

            // //400  + 3975 = 4375
            // //4375 - 1600 = 2775
            // //2775 - 1600 = 1175
            

            // // 2 --> 40
            // // jarak = 3
            // // 2,5,8,11,14,17,20,23,26,29,32,35,38 (41%40=1)
            // // 3,6,9

            // //x1,y1 = 400, 282 --> Tandain
            // //x2,y2 = (4375-1600=2775), 283 --> tidak ditandain
            // //x3,y3 = (2775-1600=1175), 284 --> Tandain
            
            // int x = x_start;
            // for (int y = y_start; y <= y_end; y++) {
            //     for (x = x; x <= x_end; x+=pixelSpacing) {
            //         if (x < x_start) {
            //             x+=x_start;
            //         }
            //         // Get pixel color
            //         int pixel = image.getRGB(x, y);
            //         int r = (pixel >> 16) & 0xFF; // Red channel
            //         int g = (pixel >> 8) & 0xFF;  // Green channel
            //         int b = pixel & 0xFF;         // Blue channel
    
            //         // maks hingga 256 kali
            //         if (digestIndex < digest.length()) {
            //             System.out.println("titik: "+x+" "+y + " Ke-"+n);
            //             n+=1;
            //             // Get the bit to embed from the hash
            //             char hashBit = digest.charAt(digestIndex);
            //             int value = (hashBit == '1') ? 1 : 0;
    
            //             // Modify the color channel (LSB embedding)
            //             if (isOdd) {
            //                 b = (b & 0xFE) | value;  // Modify blue if odd
            //             } else {
            //                 g = (g & 0xFE) | value;  // Modify green if even
            //             }
    
            //             // Set the modified pixel back to the image
            //             pixel = (r << 16) | (g << 8) | b;
            //             image.setRGB(x, y, pixel);
    
            //             digestIndex++;  // Move to the next bit in the hash
            //         } else {
            //             // If digest is exhausted, stop embedding
            //             break;
            //         }

            //             // masalah
                    
            //     }
            //     if (x > x_end) {
            //         x-= x_end;
            //     }
            // }

            int digestIndex = 0;
            for (int y = y_start; y <= y_end && digestIndex < digest.length(); y++) {
                for (int x = x_start; x <= x_end && digestIndex < digest.length(); x++) {
                    if ((x - x_start + (y - y_start) * areaWidth) % pixelSpacing == 0) {
                        // Embedding logic here
                        int pixel = image.getRGB(x, y);
                        int a = (pixel >> 24) & 0xFF;  // Extract the alpha channel
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;
            
                        char hashBit = digest.charAt(digestIndex++);
                        int value = (hashBit == '1') ? 1 : 0;
                        if (isOdd) {
                            b = (b & 0xFE) | value;
                        } else {
                            g = (g & 0xFE) | value;
                        }
            
                        pixel = (a << 24) | (r << 16) | (g << 8) | b;
                        image.setRGB(x, y, pixel);
                    }
                }
            }
            //         int pixel = image.getRGB(x, y);
            //         int r = 255; // Red
            //         int g = (pixel >> 8) & 0xFF;
            //         int b = pixel & 0xFF;
            //         image.setRGB(x, y, new Color(r, g, b).getRGB());

                        // Change every pixel to red for testing

            // // testing
            // for (int y = 0; y < height; y++) {
            //     for (int x = 0; x < width; x++) {
            //         int pixel = image.getRGB(x, y);
            //         int r = 255; // Red
            //         int g = (pixel >> 8) & 0xFF;
            //         int b = pixel & 0xFF;
            //         image.setRGB(x, y, new Color(r, g, b).getRGB());
            //     }
            // }
            // ImageIO.write(image, "PNG", new File("output.png"));
    
            // Save the modified image
            File outputFile = new File("output.png");
            ImageIO.write(image, "PNG", new File("output.png"));
            System.out.println("Image manipulation completed successfully!");
            
        } catch (IOException e) {
            System.out.println("hi");
            System.out.println("Error saving image: "+ e.getMessage());
            e.printStackTrace();
        }
    }
}

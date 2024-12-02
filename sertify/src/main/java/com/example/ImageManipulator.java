package com.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class temp {
    //Generate SHA-256
    public static String generateHash(String name, String idNumber, String certificateNumber, String datePublished, String position) {

        //Satukan input menjadi 1 string
        String data = name + ", " + idNumber + ", " + certificateNumber + ", " + datePublished + ", " + position;

        //Inisialisasi MessageDigest
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "Error";
        }

        //Generate hash byte --> ada 32 byte (1 byte = 8 bit)
        byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        //Convert byte ke bit (256 bit)
        StringBuilder bitDigest = new StringBuilder();
        for (byte b : hashBytes) {
            //Convert setiap bit lalu satukan (append)
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            bitDigest.append(binary);
        }
        return bitDigest.toString();
    }

    public static void main(String[] args) {
        //Contoh input untuk watermark
        String name = "Alice";
        String idNumber = "6182201000";
        String certificateNumber = "56789";
        String datePublished = "2024-11-30";
        String position = "Software Engineer";

        //Buat digest
        String hash = generateHash(name, idNumber, certificateNumber, datePublished, position);
        System.out.println("Digest= " + hash);
    }
}

public class ImageManipulator {

    // Method to change the color of half of the image
    public static void changeHalfImageToRed(String inputImagePath, String outputImagePath) {
        try {
            // Step 1: Read the image into a BufferedImage
            File inputFile = new File(inputImagePath);
            BufferedImage image = ImageIO.read(inputFile);

            // Step 2: Get the image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            // Step 3: Define the color to change the pixels to (e.g., red)
            Color red = Color.RED;
            System.out.println("red: "+red);
            int redRGB = red.getRGB();  // Convert the color to an integer RGB value
            System.out.println("RGB: "+redRGB);

            // Step 4: Loop through half of the image and change the pixels
            // Let's modify the left half of the image to red
            for (int x = 0; x < width / 2; x++) {  // Loop through the left half
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, redRGB);  // Set the pixel at (x, y) to red
                }
            }
            System.out.println("height: "+height);
            System.out.println("width: "+width);
            System.out.println("Get rgb: "+image.getRGB(0, 0));

            // Step 5: Save the modified image to a new file
            File outputFile = new File(outputImagePath);
            ImageIO.write(image, "png", outputFile);  // Save as PNG (or JPEG if needed)

            // System.out.println("Half of the image has been changed to red and saved to " + outputImagePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
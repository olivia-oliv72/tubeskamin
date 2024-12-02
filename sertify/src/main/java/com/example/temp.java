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
        // ganjil = blue diganti
        // genap = green diganti

        //DIGEST: 101010 
        // RGB(174,234,98)
        //karena ganjil --> blue
        //98 = 1100010
        //AMBIL BIT DIGEST MULAI DARI KIRI --> 1
        //sisipkan pada LSB 
        //awal: 0110 0010 (98)
        //akhir: 0110 0011 (99)
        String certificateNumber = "56789"; //Ganjil
        String datePublished = "2024-11-30";
        String position = "Software Engineer";

        //Buat digest
        String hash = generateHash(name, idNumber, certificateNumber, datePublished, position);
        System.out.println("Digest= " + hash);

        ImageManipulator im = new ImageManipulator();
        im.changeImage("input.png", "output.png", certificateNumber, hash);
    }
}

class ImageManipulator {
    public ImageManipulator() {

    }

    // Method to change the color of half of the image
    public void changeImage(String inputImagePath, String outputImagePath, String certificateNumber, String digest) {
        try {
            // Step 1: Read the image into a BufferedImage
            File inputFile = new File(inputImagePath);
            BufferedImage image = ImageIO.read(inputFile);

            // Step 2: Get the image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            // Step 3: Menentukan batas area tengah
            int x_awal = (int)Math.floor(width * 0.2);
            int y_awal = (int)Math.floor(height * 0.2);
            int x_akhir = (int)Math.floor(width * 0.8);
            int y_akhir = (int)Math.floor(height * 0.8);

            int lebarTengah = (int) Math.floor(width * 0.6);
            int tinggiTengah = (int) Math.floor(height * 0.6);

            int luasAreaTengah = lebarTengah * tinggiTengah;

            //Menentukan jarak antar pixel yang disisipkan pada LSB
            //Jarak antar piksel = ⌊akar (luas area tengah / 256 )⌋
            int jarak = (int) Math.floor(Math.sqrt(luasAreaTengah / 256.0));

            char[] ganjil = {'1', '3', '5', '7', '9'};
            boolean isGanjil = false;
            for (int i = 0; i < ganjil.length; i++) {
                if (certificateNumber.charAt(certificateNumber.length()-1) == ganjil[i]) {
                    isGanjil = true;
                }
            }
            int digestKe = 0;
            for (int index_y = y_awal; index_y < y_akhir ; index_y++) {
                for (int index_x = x_awal ; index_x < x_akhir; index_x++) {                    
                    if ((index_x - x_awal) % jarak == 0){
                        //AMBIL BIT DIGEST
                        //SISIPKAN LSB
                        int pixel = image.getRGB(index_x, index_y);

                        // ambil nilai rgb sekarang
                        int r = (pixel >> 16) & 0xFF; // ambil nilai red
                        int g = (pixel >> 8) & 0xFF; // ambil nilai green
                        int b = pixel & 0xFF; // ambil nilai blue

                        //ambil nilai digest ke - n
                        char nilaiDigest = digest.charAt(digestKe);
                        int value = 0;
                        if (nilaiDigest == '1') {
                            value = 1;
                        }

                        // jika ganjil, ubah nilai blue
                        if (isGanjil) {
                            b = (b & 0xFE) | value;
                        }
                        
                        // jika genap, ubah nilai green
                        else {
                            g = (g & 0xFE) | value;
                        }

                        pixel = (r << 16) | (g << 8) | b;
                        image.setRGB(index_x, index_y, pixel);
                        
                        digestKe++;
                    }
                }
            }

            // Step 5: Save the modified image to a new file
            File outputFile = new File(outputImagePath);
            ImageIO.write(image, "png", outputFile);  // Save as PNG (or JPEG if needed)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
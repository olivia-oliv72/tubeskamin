import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Contoh data
        String name = "Alice";
        String idNumber = "6182201000";
        String certificateNumber = "56789"; // Ganjil
        String datePublished = "2024-11-30";
        String position = "Software Engineer";

        //Buat digest
        String hash = generateHash(name, idNumber, certificateNumber, datePublished, position);
        System.out.println("Digest= " + hash);

        String pathInput = "(Bulk 1) Seitu - Sertifikat.png";
        String out = "output.png";

        changeImage(pathInput, out, certificateNumber, hash);
    }

    public static void changeImage(String pathInput, String pathOutput, String certificateNumber, String digest) throws IOException {
        File input = new File(pathInput);
        BufferedImage image = ImageIO.read(input);

        int lebar = image.getWidth();
        int tinggi = image.getHeight();
        
        int x_mulai, x_akhir, y_mulai, y_akhir;
        x_mulai = (int) Math.floor(lebar * 0.2); 
        y_mulai = (int) Math.floor(tinggi * 0.2); 
        x_akhir = (int) Math.floor(lebar * 0.8);
        y_akhir = (int) Math.floor(tinggi * 0.8);

        int lebarArea, tinggiArea, luasArea, spacing;
        lebarArea = (int) Math.floor(lebar * 0.6);
        tinggiArea = (int) Math.floor(tinggi * 0.6);
        luasArea = lebarArea * tinggiArea;
        spacing = (int) Math.floor(Math.sqrt(luasArea / 256.0));

        char[] ganjil = {'1', '3', '5', '7', '9'};
        boolean isGanjil = false;
        for (char c : ganjil) {
            if (certificateNumber.charAt(certificateNumber.length()-1) == c) {
                isGanjil = true;
                break;
            }
        }

        int index = 0;
        
        for (int y = y_mulai; y <= y_akhir; y++) {
            for (int x = x_mulai; x <= x_akhir; x++) {
                if ((x - x_mulai) % spacing == 0) {
                    System.out.println(x);
                    int pixel = image.getRGB(x, y);
                    int r, g, b;
                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = pixel & 0xFF;

                    System.out.println("RGB value dari "+x+" "+y);
                    System.out.println("R: "+r+" G: "+g+" B: "+b);
                }
            }
        }
    }

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

}

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulation {

    public static void main(String[] args) {
        try {
            // Step 1: Load the PNG image
            File inputFile = new File("(Bulk 1) Seitu - Sertifikat.png");
            BufferedImage image = ImageIO.read(inputFile);
            
            // Step 2: Manipulate the image (e.g., change the color of pixels)
            // For demonstration, let's invert the colors of the image
            int width = image.getWidth();
            int height = image.getHeight();
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Get the pixel color
                    Color pixelColor = new Color(image.getRGB(x, y));
                    
                    // Invert the colors
                    int red = 255 - pixelColor.getRed();
                    int green = 255 - pixelColor.getGreen();
                    int blue = 255 - pixelColor.getBlue();
                    
                    // Set the new color back to the image
                    image.setRGB(x, y, new Color(red, green, blue).getRGB());
                }
            }

            // Step 3: Save the modified image
            File outputFile = new File("output.png");
            ImageIO.write(image, "PNG", outputFile);
            
            System.out.println("Image manipulation successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

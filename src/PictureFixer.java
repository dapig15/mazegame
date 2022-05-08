import java.awt.image.BufferedImage;
import java.awt.*;
public class PictureFixer {
    public static java.awt.image.BufferedImage removeBackground(java.awt.image.BufferedImage image) {
        java.awt.image.BufferedImage newImage = new java.awt.image.BufferedImage(image.getWidth(), image.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);

        for(int x = 0; x<image.getWidth(); x++) {
            for(int y = 0; y<image.getHeight(); y++) {
                Color temp = new Color(image.getRGB(x, y));
                if(temp.getRed() > 20 || temp.getBlue() > 20 || temp.getGreen() > 20) {
                    newImage.setRGB(x, y, image.getRGB(x, y));
                }

            }
        }

        java.awt.image.BufferedImage newerImage = convertToBufferedImage(newImage.getScaledInstance(256, 256, java.awt.Image.SCALE_SMOOTH));
        return newerImage;
    }
    public static BufferedImage convertToBufferedImage(Image img) {

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bi = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }
}
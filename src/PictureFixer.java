import java.awt.Color;
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
        return newImage;
    }
}
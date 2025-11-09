package comparator;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageComparator {

    public int countDifferentPixels(BufferedImage img1, BufferedImage img2) {
        int diffCount = 0;
        int width = img1.getWidth();
        int height = img1.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    diffCount++;
                }
            }
        }
        return diffCount;
    }

    public BufferedImage generateDifferentialImage(BufferedImage img1, BufferedImage img2) { // [cite: 52]
        int width = img1.getWidth();
        int height = img1.getHeight();

        BufferedImage diffImage = new BufferedImage(width, height, img1.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                if (rgb1 == rgb2) {
                    diffImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    Color c1 = new Color(rgb1);
                    Color c2 = new Color(rgb2);

                    int r = Math.abs(c1.getRed() - c2.getRed());
                    int g = Math.abs(c1.getGreen() - c2.getGreen());
                    int b = Math.abs(c1.getBlue() - c2.getBlue());

                    Color diffColor = new Color(r, g, b);
                    diffImage.setRGB(x, y, diffColor.getRGB());
                }
            }
        }
        return diffImage;
    }
}

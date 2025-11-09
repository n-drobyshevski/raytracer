package main;
import comparator.ImageComparator;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImgCompareMain {
    private static final Logger logger = LoggerFactory.getLogger(ImgCompareMain.class);

    public static void main(String[] args) {
        String imagePath1 = "image1.png";
        String imagePath2 = "image2.png";

        if (args.length >= 2) {
            imagePath1 = args[0];
            imagePath2 = args[1];
        }

        try {
            BufferedImage image1;
            BufferedImage image2;

            if (args.length >= 2) {
                image1 = ImageIO.read(new File(imagePath1));
                image2 = ImageIO.read(new File(imagePath2));
            } else {
                InputStream is1 = ImgCompareMain.class.getClassLoader().getResourceAsStream(imagePath1);
                InputStream is2 = ImgCompareMain.class.getClassLoader().getResourceAsStream(imagePath2);

                if (is1 == null || is2 == null) {
                    logger.error("Impossible de lire resources folder");
                    return;
                }

                image1 = ImageIO.read(is1);
                image2 = ImageIO.read(is2);
            }

            if (image1 == null || image2 == null) {
                logger.error("Erreur : Impossible de lire un ou les deux fichiers. Vérifiez le format.");
                return;
            }

            if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) { //
                logger.info("KO");
                logger.error("Erreur : Les images n'ont pas les mêmes dimensions.");
                return;
            }
            ImageComparator comparator = new ImageComparator();

            int differentPixels = comparator.countDifferentPixels(image1, image2);

            if (differentPixels < 1000) {
                logger.info("OK");
            } else {
                logger.info("KO");
            }
            logger.info("Les deux images diffèrent de {} pixels.", differentPixels);


            if (differentPixels > 0) {
                BufferedImage diffImage = comparator.generateDifferentialImage(image1, image2);

                File outputFile = new File("diff.png");
                ImageIO.write(diffImage, "PNG", outputFile);
                logger.info("Image différentielle générée : diff.png");
            }

        } catch (IOException e) { //
            logger.error("Erreur IO lors de la lecture ou écriture des images : {}", e.getMessage());
            logger.error("Stack trace:", e);
        }
    }
}
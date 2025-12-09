package imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Classe utilitaire pour écrire le fichier image
 */
public class ImageWriter {

    /**
     * Sauvegarde une image au format PNG sur disque.
     *
     * @param image L'image à sauver
     * @param filePath Chemin du fichier cible (ex: "image.png")
     * @throws IOException si erreur d'écriture
     */
    public void saveImage(BufferedImage image, String filePath) throws IOException {
        File outputFile = new File(filePath);
        ImageIO.write(image, "PNG", outputFile);
    }
}

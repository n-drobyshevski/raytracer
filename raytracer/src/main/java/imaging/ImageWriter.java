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
     * Sauvegarde une BufferedImage dans un fichier PNG.
     * @param image L'image à sauvegarder
     * @param filePath Le chemin du fichier de sortie (ex: "output.png")
     * @throws IOException Si l'écriture échoue
     */
    public void saveImage(BufferedImage image, String filePath) throws IOException {
        File outputFile = new File(filePath);
        ImageIO.write(image, "PNG", outputFile);
    }
}
package imaging;

import scene.Scene;
import raytracer.RayTracer;
import imaging.Color;
import java.awt.image.BufferedImage;

/**
 * Classe pour le rendu de l'image
 * Contient la boucle principale de génération d'image.
 */
public class Renderer {

    /**
     * Effectue le rendu pixellisé d'une scène.
     *
     * @param scene La scène à rendre
     * @return Image générée après raytracing
     */
    public BufferedImage render(Scene scene) {
        int width = scene.getWidth();
        int height = scene.getHeight();

        // Créer l'image en mémoire
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Initialiser le traceur de rayons
        RayTracer rayTracer = new RayTracer(scene);

        // Boucle principale : pour chaque pixel (i, j)
        for (int j = 0; j < height; j++) { // Lignes (y)
            for (int i = 0; i < width; i++) { // Colonnes (x)

                // Interroger le RayTracer pour la couleur
                Color pixelColor = rayTracer.getPixelColor(i, j);

                // Peindre le pixel
                image.setRGB(i, j, pixelColor.toRGB());
            }
        }

        return image; // Retourner l'image à main
    }
}

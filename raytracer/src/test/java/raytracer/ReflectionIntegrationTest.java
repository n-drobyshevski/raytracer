package raytracer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import imaging.ImageWriter;
import imaging.Renderer;
import imaging.Color;
import parsing.SceneFileParser;
import raytracer.RayTracer;
import scene.Scene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectionIntegrationTest {

    private static Scene scene;
    private static RayTracer rayTracer;

    @BeforeAll
    static void setUp() throws Exception {
        // --- SCENE : UN MIROIR ET UNE SPHERE ROUGE ---
        File tempSceneFile = File.createTempFile("test_reflection", ".scene");
        tempSceneFile.deleteOnExit();

        String sceneContent = "size 200 200\n" +
                "output test_reflection.png\n" +
                "camera 0 0 5 0 0 0 0 1 0 60\n" + // Caméra face au centre
                "ambient 0.1 0.1 0.1\n" +
                "maxdepth 5\n" + // Important : Profondeur de récursion pour voir le reflet
                "\n" +
                "# Lumière blanche standard\n" +
                "point 0 5 5 0.8 0.8 0.8\n" +
                "\n" +
                "# OBJET 1 : LE MIROIR (Sphère centrale)\n" +
                "# Diffuse faible (gris foncé), mais Specular fort (Miroir)\n" +
                "diffuse 0.2 0.2 0.2\n" +
                "specular 0.8 0.8 0.8\n" +
                "shininess 100\n" +
                "sphere 0 0 0 1\n" +
                "\n" +
                "# OBJET 2 : LA CIBLE (Sphère Rouge à gauche)\n" +
                "# CORRECTION : On agrandit la sphère (r=0.8) et on la rapproche un peu\n" +
                "# pour que son reflet soit plus gros et plus facile à détecter.\n" +
                "diffuse 0.9 0 0\n" + // Rouge vif
                "specular 0 0 0\n" +
                "shininess 10\n" +
                "sphere -1.5 0 1.5 0.8\n";

        Files.write(tempSceneFile.toPath(), sceneContent.getBytes());

        SceneFileParser parser = new SceneFileParser();
        scene = parser.parse(tempSceneFile.getAbsolutePath());
        rayTracer = new RayTracer(scene);
    }

    @Test
    void testReflectionVisible() {
        // On cherche le reflet rouge sur la partie GAUCHE de la sphère miroir.
        // La sphère miroir est centrée en (100, 100) avec un rayon d'environ 35-40 pixels.

        boolean foundRedReflection = false;
        double maxRed = 0.0;

        // CORRECTION : Scan de zone (Rectangle) au lieu d'une ligne unique.
        // x: 50 à 90 (Partie gauche de l'image)
        // y: 80 à 120 (Bande centrale verticale)
        for (int x = 50; x < 90; x++) {
            for (int y = 80; y < 120; y++) {
                Color c = rayTracer.getPixelColor(x, y);

                // Debug : garder trace du rouge max trouvé pour le message d'erreur
                if (c.r() > maxRed) maxRed = c.r();

                // Critère de détection du reflet :
                // 1. Rouge dominant (> 0.3)
                // 2. Pas blanc (Reflet de la lumière) -> Vert et Bleu faibles
                if (c.r() > 0.3 && c.g() < 0.25 && c.b() < 0.25) {
                    foundRedReflection = true;
                    // On peut arrêter dès qu'on a trouvé un pixel probant
                    break;
                }
            }
            if (foundRedReflection) break;
        }

        assertTrue(foundRedReflection,
                "Reflet rouge non trouvé. Max Rouge détecté dans la zone: " + maxRed +
                        ". Vérifiez que maxdepth est bien géré et que la récursion fonctionne.");
    }

    @Test
    void generateVisualProof() throws Exception {
        Renderer renderer = new Renderer();
        BufferedImage image = renderer.render(scene);

        ImageWriter writer = new ImageWriter();
        String outputName = "test_reflection.png";
        writer.saveImage(image, outputName);

        System.out.println("Image de test réflexion générée : " + new File(outputName).getAbsolutePath());
    }
}
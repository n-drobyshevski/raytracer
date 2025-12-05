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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalIntegrationTestJalon5 {

    private static Scene scene;
    private static RayTracer rayTracer;

    @BeforeAll
    static void setUp() throws Exception {
        // --- CREATION D'UNE SCENE TEMPORAIRE VALIDE ---
        File tempSceneFile = File.createTempFile("test_global_valid", ".scene");
        tempSceneFile.deleteOnExit();

        String sceneContent = "size 200 200\n" +
                "output test_global.png\n" +
                "camera 0 0 5 0 0 0 0 1 0 45\n" +
                "ambient 0.1 0.1 0.1\n" +
                "\n" +
                "# Lumières réduites pour que la somme soit < 1.0\n" +
                "point 0 5 0 0.4 0.4 0.4\n" +
                "point 2 0 5 0.5 0.5 0.5\n" +
                "\n" +
                "# Objets\n" +
                "diffuse 0.2 0.2 0.2\n" +
                "specular 0 0 0\n" +
                "shininess 1\n" +
                "sphere 0 2 0 0.5\n" +
                "\n" +
                "diffuse 0 0 0.9\n" +
                "specular 1 1 1\n" +
                "shininess 50\n" +
                "sphere 0 0 0 1\n";

        Files.write(tempSceneFile.toPath(), sceneContent.getBytes());

        SceneFileParser parser = new SceneFileParser();
        scene = parser.parse(tempSceneFile.getAbsolutePath());
        rayTracer = new RayTracer(scene);
    }

    @Test
    void testShadowOnTop() {
        // --- TEST OMBRES (Jalon 5) ---
        // On vise le sommet de la sphère bleue (j=55).
        Color topPixel = rayTracer.getPixelColor(100, 55);

        // Analyse :
        // - Ambiante (0.1) : Présente.
        // - Lumière 1 (Haut) : BLOQUÉE par la sphère bloqueuse.
        // - Lumière 2 (Côté) : Illumine un peu (car située en z=5, devant).

        // Total attendu : ~0.25.
        // Si l'ombre ne marchait pas, on aurait > 0.55 (0.25 + 0.3 contribution Lumière 1).

        // CORRECTION : On augmente le seuil à 0.3 pour tolérer la Lumière 2.
        assertTrue(topPixel.b() < 0.3, "Le haut de la sphère devrait être à l'ombre. Valeur actuelle: " + topPixel.b());

        // On vérifie qu'on n'est pas dans le noir total (fond) ou erreur de calcul
        assertTrue(topPixel.b() >= 0.1, "L'ombre ne doit pas être noir pur, il faut l'ambiante.");
    }

    @Test
    void testPhongAndDiffuseOnSide() {
        // --- TEST PHONG & LAMBERT (Jalon 4 & 5) ---
        // On vise le côté droit (x > 0)
        Color sidePixel = rayTracer.getPixelColor(120, 100);

        // 1. Il doit être Bleu (Diffuse)
        assertTrue(sidePixel.b() > 0.5, "Le côté droit doit être bleu (éclairé par Lumière 2).");

        // 2. Testons le spéculaire pur (Reflet blanc)
        boolean foundHighlight = false;
        for(int i=100; i<140; i++) {
            Color c = rayTracer.getPixelColor(i, 100);
            // Si R et G montent, c'est le reflet blanc sur la sphère bleue
            if (c.r() > 0.3 && c.g() > 0.3) {
                foundHighlight = true;
                break;
            }
        }
        assertTrue(foundHighlight, "On devrait trouver un reflet spéculaire (Phong) blanc sur la droite.");
    }

    @Test
    void testBackground() {
        // Coin (0,0) -> Fond noir
        Color bg = rayTracer.getPixelColor(0, 0);
        assertEquals(0.0, bg.r(), 0.001);
    }

    @Test
    void generateVisualProof() throws Exception {
        Renderer renderer = new Renderer();
        BufferedImage image = renderer.render(scene);

        ImageWriter writer = new ImageWriter();
        String outputName = "test_global.png";
        writer.saveImage(image, outputName);

        System.out.println("Image de validation globale générée : " + new File(outputName).getAbsolutePath());
    }
}
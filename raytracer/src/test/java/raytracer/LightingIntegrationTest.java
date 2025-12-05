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
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LightingIntegrationTest {

    private static Scene scene;
    private static RayTracer rayTracer;

    @BeforeAll
    static void setUp() throws Exception {
        // Chargement de la scène d'éclairage
        URL sceneUrl = LightingIntegrationTest.class.getClassLoader().getResource("test_lighting.scene");
        assertNotNull(sceneUrl, "Fichier test_lighting.scene introuvable !");

        // Utilisation de toURI() pour éviter les erreurs de chemin sur Windows
        String path = Paths.get(sceneUrl.toURI()).toString();
        SceneFileParser parser = new SceneFileParser();
        scene = parser.parse(path);
        rayTracer = new RayTracer(scene);
    }

    @Test
    void testLambertCenterPixel() {
        // Le pixel central (100, 100) tape le centre de la sphère face à la lumière.
        Color pixelColor = rayTracer.getPixelColor(100, 100);

        // Attendu : Rouge pur (1.0, 0.0, 0.0)
        assertEquals(1.0, pixelColor.r(), 0.01, "Le rouge doit être à 1.0 (max)");
        assertEquals(0.0, pixelColor.g(), 0.01, "Le vert doit être à 0");
        assertEquals(0.0, pixelColor.b(), 0.01, "Le bleu doit être à 0");
    }

    @Test
    void testLambertSidePixel() {
        // Pixel sur le côté (plus sombre)
        Color centerColor = rayTracer.getPixelColor(100, 100);
        Color sideColor = rayTracer.getPixelColor(80, 100);

        // Vérification que le côté est bien rouge mais plus sombre que le centre
        assertEquals(0.0, sideColor.g(), 0.01);

        boolean isDarkerThanCenter = sideColor.r() < centerColor.r();
        boolean isBrighterThanAmbient = sideColor.r() > 0.15;

        assertEquals(true, isDarkerThanCenter, "Le côté doit être plus sombre (Lambert).");
        assertEquals(true, isBrighterThanAmbient, "Le côté doit être éclairé.");
    }

    /**
     * Ce test génère l'image 'test_lighting.png' à la racine du projet
     * pour que vous puissiez voir le résultat visuellement.
     */
    @Test
    void generateDebugImage() throws Exception {
        System.out.println("Génération de l'image de test visuel...");

        Renderer renderer = new Renderer();
        BufferedImage image = renderer.render(scene);

        ImageWriter writer = new ImageWriter();
        // On force le nom de sortie pour le retrouver facilement
        String outputName = "test_lighting.png";
        writer.saveImage(image, outputName);

        File file = new File(outputName);
        System.out.println("Image générée ici : " + file.getAbsolutePath());

        // Vérifie que le fichier a bien été créé
        assertEquals(true, file.exists());
    }
}
package raytracer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import imaging.Color;
import parsing.SceneFileParser;
import raytracer.RayTracer;
import scene.Scene;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Test d'intégration qui vérifie le pipeline complet :
 * Parser -> Scène -> RayTracer -> Pixel Couleur
 * * Il utilise la scène de 'src/test/resources/integration-scene.txt'.
 */
public class RayTracerIntegrationTest {

    private static Scene testScene;
    private static RayTracer rayTracer;
    private static BufferedImage resultImage;

    @BeforeAll
    static void setUp() throws Exception {
        // 1. Trouver et parser le fichier de scène de test
        URL sceneUrl = RayTracerIntegrationTest.class.getClassLoader().getResource("test0.scene");
        assertNotNull(sceneUrl, "Impossible de trouver integration-scene.txt dans src/test/resources");

        SceneFileParser parser = new SceneFileParser();

        String correctPath = Paths.get(sceneUrl.toURI()).toString();
        testScene = parser.parse(correctPath);

        // 2. Initialiser le RayTracer pour cette scène
        rayTracer = new RayTracer(testScene);

        // 3. (Optionnel) Créer une image de 1x1 juste pour avoir un 'Renderer'
        // Nous allons plutôt appeler getPixelColor directement.
    }

    @Test
    void testSceneSetup() {
        // Vérifie que le parser a bien fonctionné
        assertEquals(100, testScene.getWidth());
        assertEquals(100, testScene.getHeight());
        assertEquals("test-output.png", testScene.getOutput());
        assertEquals(new Color(0.1, 0.1, 0.1), testScene.getAmbient());
        assertEquals(1, testScene.getShapes().size(), "La scène doit contenir 1 sphère.");
    }

    @Test
    void testCenterPixel_HitsSphere() {
        // Arrange
        // (50, 50) est le pixel central d'une image 100x100
        // D'après la config (caméra à 0,0,5 regardant 0,0,0), ce rayon
        // va tout droit et doit toucher la sphère à (0,0,0).

        Color expectedColor = testScene.getAmbient(); // Jalon 3 : couleur = ambient
        int expectedRGB = expectedColor.toRGB();

        // Act
        Color pixelColor = rayTracer.getPixelColor(50, 50);

        // Assert
        assertEquals(expectedRGB, pixelColor.toRGB(), "Le pixel central (50, 50) doit toucher la sphère.");
    }

    @Test
    void testCornerPixel_MissesSphere() {
        // Arrange
        // (0, 0) est le pixel en haut à gauche.
        // Ce rayon partira en haut à gauche et manquera la sphère.

        Color expectedColor = new Color(0, 0, 0); // Jalon 3 : couleur = noir
        int expectedRGB = expectedColor.toRGB();

        // Act
        Color pixelColor = rayTracer.getPixelColor(0, 0);

        // Assert
        assertEquals(expectedRGB, pixelColor.toRGB(), "Le pixel de coin (0, 0) ne doit pas toucher la sphère.");
    }
}
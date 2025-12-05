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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhongIntegrationTest {

    private static Scene scene;
    private static RayTracer rayTracer;

    @BeforeAll
    static void setUp() throws Exception {
        URL sceneUrl = PhongIntegrationTest.class.getClassLoader().getResource("test_phong.scene");
        assertNotNull(sceneUrl, "Fichier test_phong.scene introuvable !");

        String path = Paths.get(sceneUrl.toURI()).toString();
        SceneFileParser parser = new SceneFileParser();
        scene = parser.parse(path);
        rayTracer = new RayTracer(scene);
    }

    @Test
    void testSpecularHighlightCenter() {
        // Au centre (100, 100), la normale (0,0,1), la lumière (0,0,1) et la vue (0,0,1) sont alignées.
        // H = (L + V).normalize() = (0,0,1)
        // N.H = 1.0
        // Specular = 1.0^50 * Light(1,1,1) * Spec(1,1,1) = (1,1,1)

        Color centerPixel = rayTracer.getPixelColor(100, 100);

        // On s'attend à du blanc pur
        assertEquals(1.0, centerPixel.r(), 0.01, "Le centre doit être blanc (reflet spéculaire max)");
        assertEquals(1.0, centerPixel.g(), 0.01);
        assertEquals(1.0, centerPixel.b(), 0.01);
    }

    @Test
    void testSpecularFalloff() {
        // Un pixel un peu décalé (ex: 110, 100)
        // L'angle change, donc N.H < 1.
        // Avec shininess=50, la valeur doit chuter rapidement.

        Color offCenterPixel = rayTracer.getPixelColor(110, 100);

        // 1. Ce n'est pas noir (il y a un peu de reflet)
        assertTrue(offCenterPixel.r() > 0.0, "Il devrait y avoir un peu de reflet autour du centre");

        // 2. C'est moins brillant que le centre
        assertTrue(offCenterPixel.r() < 0.9, "L'intensité spéculaire doit diminuer rapidement en s'éloignant du centre");

        // Comme la diffuse est à 0, la couleur vient uniquement du spéculaire
    }

    @Test
    void generateDebugImage() throws Exception {
        // Génère l'image pour validation visuelle
        Renderer renderer = new Renderer();
        BufferedImage image = renderer.render(scene);

        ImageWriter writer = new ImageWriter();
        String outputName = "test_phong_debug.png";
        writer.saveImage(image, outputName);

        File file = new File(outputName);
        System.out.println("Image de debug générée : " + file.getAbsolutePath());
        assertTrue(file.exists());
    }
}
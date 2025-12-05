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

public class MultiShapeIntegrationTest {

    private static Scene scene;
    private static RayTracer rayTracer;

    @BeforeAll
    static void setUp() throws Exception {
        // --- SCENE : UNE SPHERE AU-DESSUS D'UN PLAN ---
        File tempSceneFile = File.createTempFile("test_multishape", ".scene");
        tempSceneFile.deleteOnExit();

        String sceneContent = "size 200 200\n" +
                "output test_multishape.png\n" +
                // Caméra regardant légèrement vers le bas
                "camera 0 1 4 0 0 0 0 1 0 60\n" +
                "ambient 0.1 0.1 0.1\n" +
                "\n" +
                "# Lumière zénithale (au-dessus)\n" +
                "point 0 5 0 0.8 0.8 0.8\n" +
                "\n" +
                "# FORME 1 : PLAN (Le sol)\n" +
                "# Plan passant par (0, -1, 0) avec normale vers le haut (0, 1, 0)\n" +
                "diffuse 0.5 0.5 0.5\n" + // Gris
                "specular 0 0 0\n" +
                "shininess 10\n" +
                "plane 0 -1 0 0 1 0\n" +
                "\n" +
                "# FORME 2 : SPHERE\n" +
                "# Sphère en (0, 1, 0) au-dessus du plan\n" +
                // CORRECTION : Diffuse réduite à 0.9 pour que 0.9 + 0.1 (ambient) <= 1.0
                "diffuse 0.9 0 0\n" + // Rouge
                "specular 0.5 0.5 0.5\n" +
                "shininess 50\n" +
                "sphere 0 1 0 1\n";

        Files.write(tempSceneFile.toPath(), sceneContent.getBytes());

        SceneFileParser parser = new SceneFileParser();
        scene = parser.parse(tempSceneFile.getAbsolutePath());
        rayTracer = new RayTracer(scene);
    }

    @Test
    void testSphereIsLit() {
        // Le haut de la sphère (0, 2, 0) fait face à la lumière.
        // Au lieu de deviner le pixel exact, on scanne la colonne centrale (i=100)
        // pour trouver le point le plus brillant (le "sommet" visible).

        double maxRed = 0.0;

        // On scanne la moitié supérieure de l'image
        for (int j = 0; j < 100; j++) {
            Color pixel = rayTracer.getPixelColor(100, j);
            if (pixel.r() > maxRed) {
                maxRed = pixel.r();
            }
        }

        // La valeur max attendue est ~0.82 (0.1 amb + 0.9 diff * 0.8 light)
        // On vérifie qu'on a trouvé au moins un pixel bien éclairé (> 0.5)
        assertTrue(maxRed > 0.5, "Le sommet de la sphère devrait être éclairé en rouge vif. Max trouvé: " + maxRed);
    }

    @Test
    void testFloorShadow() {
        // Le sol est à y=-1. La sphère est en y=1. La lumière est en y=5.
        // L'ombre est centrée autour de (0, -1, 0).

        // CORRECTION : j=140 vise le point (0, -1, 0).
        Color shadowPixel = rayTracer.getPixelColor(100, 140);

        // Doit être sombre (seulement ambiante 0.1 + un peu de diff indirecte ou bruit)
        assertTrue(shadowPixel.r() < 0.2, "Le sol sous la sphère doit être à l'ombre. Actuel: " + shadowPixel.r());
    }

    @Test
    void testFloorLit() {
        // Un point sur le plan mais loin de la sphère (sur le côté).
        // Pixel (20, 180) -> Côté gauche de l'image.
        Color floorPixel = rayTracer.getPixelColor(20, 180);

        // Doit être gris éclairé
        assertTrue(floorPixel.r() > 0.2, "Le sol loin de la sphère doit être éclairé.");
        assertTrue(floorPixel.g() > 0.2, "Le sol doit être gris.");
        assertTrue(floorPixel.b() > 0.2, "Le sol doit être gris.");
    }

    @Test
    void generateVisualProof() throws Exception {
        Renderer renderer = new Renderer();
        BufferedImage image = renderer.render(scene);

        ImageWriter writer = new ImageWriter();
        String outputName = "test_multishape.png";
        writer.saveImage(image, outputName);

        System.out.println("Image multi-formes générée : " + new File(outputName).getAbsolutePath());
    }
}
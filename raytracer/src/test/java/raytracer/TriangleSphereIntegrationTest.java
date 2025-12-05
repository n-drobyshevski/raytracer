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

public class TriangleSphereIntegrationTest {

    private static Scene scene;
    private static RayTracer rayTracer;

    @BeforeAll
    static void setUp() throws Exception {
        // --- SCENE : SPHERE DEVANT UN TRIANGLE ---
        File tempSceneFile = File.createTempFile("test_triangle_sphere", ".scene");
        tempSceneFile.deleteOnExit();

        String sceneContent = "size 200 200\n" +
                "output test_triangle_sphere.png\n" +
                "camera 0 1 5 0 1 0 0 1 0 60\n" + // Caméra en (0,1,5) regardant (0,1,0)
                "ambient 0.1 0.1 0.1\n" +
                "\n" +
                "# Lumière décalée sur la droite pour projeter une ombre visible sur le triangle\n" +
                "point 2 1 4 0.8 0.8 0.8\n" +
                "\n" +
                "# --- GEOMETRIE DU TRIANGLE ---\n" +
                "maxverts 3\n" +
                "# Un grand triangle en arrière-plan (z = -2)\n" +
                "vertex -3 -2 -2\n" + // Bas Gauche
                "vertex 3 -2 -2\n" +  // Bas Droite
                "vertex 0 5 -2\n" +   // Haut Centre
                "\n" +
                "# FORME 1 : TRIANGLE VERT\n" +
                "# CORRECTION : Diffuse réduite à 0.9 pour que 0.9 + 0.1 (ambient) <= 1.0\n" +
                "diffuse 0 0.9 0\n" + // Vert
                "specular 0 0 0\n" +
                "shininess 1\n" +
                "tri 0 1 2\n" +
                "\n" +
                "# FORME 2 : SPHERE ROUGE\n" +
                "# Sphère en (0, 1, 0), devant le triangle\n" +
                "# CORRECTION : Diffuse à 0.8 pour éviter l'erreur de validation (0.8 + 0.1 < 1.0)\n" +
                "diffuse 0.8 0 0\n" +
                "specular 0.5 0.5 0.5\n" +
                "shininess 50\n" +
                "sphere 0 1 0 0.8\n";

        Files.write(tempSceneFile.toPath(), sceneContent.getBytes());

        SceneFileParser parser = new SceneFileParser();
        scene = parser.parse(tempSceneFile.getAbsolutePath());
        rayTracer = new RayTracer(scene);
    }

    @Test
    void testSphereHighlight() {
        // La sphère est rouge. On cherche un pixel rouge brillant au centre de l'image.
        // On scanne une petite zone centrale pour être sûr de trouver le point chaud.

        boolean foundRedHighlight = false;

        // Scan centre +/- 20 pixels
        for (int i = 80; i < 120; i++) {
            for (int j = 80; j < 120; j++) {
                Color c = rayTracer.getPixelColor(i, j);
                // Rouge dominant et assez lumineux
                if (c.r() > 0.5 && c.g() < 0.3) {
                    foundRedHighlight = true;
                    break;
                }
            }
            if (foundRedHighlight) break;
        }

        assertTrue(foundRedHighlight, "Impossible de trouver la sphère rouge éclairée au centre.");
    }

    @Test
    void testTriangleBackground() {
        // Le fond est un triangle vert.
        // CORRECTION : (20, 20) est trop dans le coin et rate le triangle.
        // On vise le Haut-Centre (Pixel 100, 30) qui tape assurément dans le sommet du triangle (0, 5, -2).

        Color triPixel = rayTracer.getPixelColor(100, 30);

        // Doit être vert éclairé
        assertTrue(triPixel.g() > 0.2, "Le triangle en arrière-plan doit être vert. Valeur: " + triPixel.g());
        assertTrue(triPixel.r() < 0.2, "Le triangle ne doit pas être rouge.");
    }

    @Test
    void testShadowOnTriangle() {
        // La lumière est à droite (x=2). La sphère est au centre.
        // L'ombre doit être projetée sur le triangle vers la GAUCHE de l'image.

        // On scanne la partie gauche de l'image, juste à côté de la sphère, pour trouver l'ombre.
        // La sphère a un rayon de 0.8. A gauche, x < 0.

        boolean foundShadow = false;

        // Scan horizontal à gauche de la sphère (x de 40 à 80)
        for (int i = 40; i < 80; i++) {
            Color c = rayTracer.getPixelColor(i, 100); // Ligne médiane

            // Un pixel d'ombre sur le triangle vert aura :
            // - Une composante verte faible (juste l'ambiante ~0.1)
            // - Pas de rouge (0.1 ambiante)

            // CORRECTION SEUIL : c.r() vaut 0.1 (ambiante). < 0.1 échoue. On met < 0.15.
            if (c.g() > 0.05 && c.g() < 0.15 && c.r() < 0.15) {
                foundShadow = true;
                break;
            }
        }

        assertTrue(foundShadow, "On devrait trouver une ombre portée sur le triangle vert (côté gauche).");
    }

    @Test
    void generateVisualProof() throws Exception {
        Renderer renderer = new Renderer();
        BufferedImage image = renderer.render(scene);

        ImageWriter writer = new ImageWriter();
        String outputName = "test_triangle_sphere.png";
        writer.saveImage(image, outputName);

        System.out.println("Image triangle+sphère générée : " + new File(outputName).getAbsolutePath());
    }
}
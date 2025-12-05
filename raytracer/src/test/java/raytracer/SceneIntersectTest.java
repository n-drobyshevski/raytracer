package raytracer;

import geometry.Sphere;
import imaging.Color;
import math.AbstractVec3;
import math.Point;
import math.Ray;
import math.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scene.Scene;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour la logique de 'findClosestIntersection' de la Scène.
 */
class SceneIntersectTest {

    private Scene scene;
    private Sphere sphereClose;
    private Sphere sphereFar;

    @BeforeEach
    void setUp() {
        scene = new Scene();
        Color black = new Color(0, 0, 0);
        double shininess = 32.0;

        // Sphère proche
        sphereClose = new Sphere(new Point(0, 0, 0), 1.0, black, black, shininess);
        // Sphère lointaine, sur le même axe
        sphereFar = new Sphere(new Point(0, 0, 5), 1.0, black, black, shininess);

        scene.addShape(sphereClose);
        scene.addShape(sphereFar);
    }

    @Test
    void testFindClosestIntersection_HitsBoth() {
        // Rayon qui traverse les deux sphères
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));

        Optional<Intersection> hit = scene.findClosestIntersection(ray);

        // Doit trouver une intersection
        assertTrue(hit.isPresent(), "La scène aurait dû trouver une intersection.");

        // sphereClose est touchée à t=4
        // sphereFar est touchée à t=9
        // Doit retourner la plus proche (t=4)
        assertEquals(4.0, hit.get().getT(), AbstractVec3.EPSILON, "Distance incorrecte.");
        assertEquals(sphereClose, hit.get().getShape(), "N'a pas retourné l'objet le plus proche.");
    }

    @Test
    void testFindClosestIntersection_HitsOnlyFar() {
        // Rayon qui part entre les deux sphères
        Ray ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));

        Optional<Intersection> hit = scene.findClosestIntersection(ray);

        // Doit trouver une intersection
        assertTrue(hit.isPresent(), "La scène aurait dû trouver une intersection.");

        // sphereClose est derrière (t < 0)
        // sphereFar est touchée à t=2
        assertEquals(2.0, hit.get().getT(), AbstractVec3.EPSILON, "Distance incorrecte pour la sphère lointaine.");
        assertEquals(sphereFar, hit.get().getShape(), "N'a pas retourné le bon objet.");
    }

    @Test
    void testFindClosestIntersection_NoHit() {
        // Rayon qui ne touche rien
        Ray ray = new Ray(new Point(0, 5, -5), new Vector(0, 0, 1));

        Optional<Intersection> hit = scene.findClosestIntersection(ray);

        // Ne doit rien trouver
        assertFalse(hit.isPresent(), "La scène n'aurait pas dû trouver d'intersection.");
    }
}
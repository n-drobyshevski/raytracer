package geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import math.AbstractVec3;
import imaging.Color;
import math.Point;
import math.Vector;
import math.Ray;
import raytracer.Intersection;
import java.util.Optional;

/**
 * Tests pour la logique d'intersection de la sphère.
 */
class SphereIntersectTest {

    private Sphere unitSphere; // Sphère de rayon 1 à l'origine

    @BeforeEach
    void setUp() {
        // Arrange: Une sphère unitaire simple à l'origine
        unitSphere = new Sphere(
                new Point(0, 0, 0),
                1.0,
                new Color(1, 0, 0),
                new Color(1, 1, 1)
        );
    }

    @Test
    void testIntersect_HitTwoPoints() {
        // Rayon partant de l'extérieur, visant le centre
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Optional<Intersection> hit = unitSphere.intersect(ray);

        // Doit toucher (t1 et t2 > 0), t2 est le plus proche
        // Touche à z=-1 (t=4) et z=1 (t=6). Le plus proche est t=4.
        assertTrue(hit.isPresent(), "Le rayon aurait dû toucher la sphère.");
        assertEquals(4.0, hit.get().getT(), AbstractVec3.EPSILON, "Distance d'intersection incorrecte.");
    }

    @Test
    void testIntersect_Miss() {
        // Rayon partant de l'extérieur, parallèle mais à côté
        Ray ray = new Ray(new Point(0, 2, -5), new Vector(0, 0, 1));
        Optional<Intersection> hit = unitSphere.intersect(ray);

        // Ne doit pas toucher (Delta < 0)
        assertFalse(hit.isPresent(), "Le rayon ne aurait pas dû toucher la sphère.");
    }

    @Test
    void testIntersect_InsideSphere() {
        // Rayon partant de l'intérieur de la sphère
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Optional<Intersection> hit = unitSphere.intersect(ray);

        // Doit toucher (t2 < 0, t1 > 0), t1 est le seul valide
        // Touche à z=1 (t=1) et z=-1 (t=-1). Le plus proche *positif* est t=1.
        assertTrue(hit.isPresent(), "Le rayon partant de l'intérieur aurait dû toucher.");
        assertEquals(1.0, hit.get().getT(), AbstractVec3.EPSILON, "Distance d'intersection (sortie) incorrecte.");
    }

    @Test
    void testIntersect_SphereBehindRay() {
        // Rayon partant devant la sphère, s'éloignant
        Ray ray = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        Optional<Intersection> hit = unitSphere.intersect(ray);

        // Ne doit pas toucher (t1 et t2 < 0)
        assertFalse(hit.isPresent(), "La sphère est derrière le rayon.");
    }

    @Test
    void testIntersect_Tangent() {
        // Rayon qui effleure la sphère
        Ray ray = new Ray(new Point(1, 0, -5), new Vector(0, 0, 1));
        Optional<Intersection> hit = unitSphere.intersect(ray);

        // Doit toucher en un seul point (Delta = 0)
        // Touche à (1,0,0), ce qui correspond à t=5
        assertTrue(hit.isPresent(), "Le rayon tangent aurait dû toucher la sphère.");
        assertEquals(5.0, hit.get().getT(), AbstractVec3.EPSILON, "Distance d'intersection tangente incorrecte.");
    }
}
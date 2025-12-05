package geometry;

import imaging.Color;
import math.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void testSphereConstruction() {
        Point center = new Point(1, 2, 3);
        double radius = 5.0;
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);
        double shininess = 32.0;

        Sphere sphere = new Sphere(center, radius, diffuse, specular, shininess);

        assertEquals(center, sphere.getCenter(), "Center point should match constructor argument");
        assertEquals(radius, sphere.getRadius(), 0.0001, "Radius should match constructor argument");
        assertEquals(diffuse, sphere.getDiffuse(), "Diffuse color should match constructor argument");
        assertEquals(specular, sphere.getSpecular(), "Specular color should match constructor argument");
    }

    @Test
    void testSphereWithZeroRadius() {
        Point center = new Point(0, 0, 0);
        double radius = 0.0;
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);
        double shininess = 32.0;

        Sphere sphere = new Sphere(center, radius, diffuse, specular, shininess);
        assertEquals(0.0, sphere.getRadius(), 0.0001, "Radius should be exactly zero");
    }

    @Test
    void testSphereWithNegativeRadius() {
        Point center = new Point(0, 0, 0);
        double radius = -1.0;
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);
        double shininess = 32.0;

        Sphere sphere = new Sphere(center, radius, diffuse, specular, shininess);
        assertTrue(sphere.getRadius() < 0, "Radius should remain negative if specified as such");
    }
}

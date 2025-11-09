package geometry;

import imaging.Color;
import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    @Test
    void testPlaneConstruction() {
        Point point = new Point(1, 2, 3);
        Vector normal = new Vector(0, 1, 0);
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);

        Plane plane = new Plane(point, normal, diffuse, specular);

        assertEquals(point, plane.getPoint(), "Point should match constructor argument");
        assertEquals(normal, plane.getNormal(), "Normal should match constructor argument");
        assertEquals(diffuse, plane.getDiffuse(), "Diffuse color should match constructor argument");
        assertEquals(specular, plane.getSpecular(), "Specular color should match constructor argument");
    }

    @Test
    void testPlaneNormalIsNormalized() {
        Point point = new Point(0, 0, 0);
        Vector normal = new Vector(0, 2, 0); // Non-normalized vector
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);

        Plane plane = new Plane(point, normal, diffuse, specular);
        Vector retrievedNormal = plane.getNormal();

        assertEquals(1.0, retrievedNormal.length(), 0.0001,
            "Normal vector should be normalized");
    }
}

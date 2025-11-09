package geometry;

import imaging.Color;
import math.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    @Test
    void testTriangleConstruction() {
        Point a = new Point(0, 0, 0);
        Point b = new Point(1, 0, 0);
        Point c = new Point(0, 1, 0);
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);

        Triangle triangle = new Triangle(a, b, c, diffuse, specular);

        assertEquals(a, triangle.getA(), "Vertex A should match constructor argument");
        assertEquals(b, triangle.getB(), "Vertex B should match constructor argument");
        assertEquals(c, triangle.getC(), "Vertex C should match constructor argument");
        assertEquals(diffuse, triangle.getDiffuse(), "Diffuse color should match constructor argument");
        assertEquals(specular, triangle.getSpecular(), "Specular color should match constructor argument");
    }

    @Test
    void testCoincidentVertices() {
        Point a = new Point(0, 0, 0);
        Point b = new Point(0, 0, 0); // Same as a
        Point c = new Point(1, 1, 0);
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);

        Triangle triangle = new Triangle(a, b, c, diffuse, specular);

        assertEquals(true, triangle.getA().equals(triangle.getB()),
            "Should allow coincident vertices even though it creates a degenerate triangle");
    }

    @Test
    void testCollinearVertices() {
        Point a = new Point(0, 0, 0);
        Point b = new Point(1, 1, 1);
        Point c = new Point(2, 2, 2); // Collinear with a and b
        Color diffuse = new Color(0.5, 0.5, 0.5);
        Color specular = new Color(1, 1, 1);

        Triangle triangle = new Triangle(a, b, c, diffuse, specular);

        assertNotNull(triangle, "Should allow creation of degenerate (collinear) triangle");
    }
}

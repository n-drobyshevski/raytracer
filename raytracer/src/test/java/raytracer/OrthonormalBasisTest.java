package raytracer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import raytracer.OrthonormalBasis;
import math.Point;
import math.Vector;

/**
 * Tests pour le calcul du repère orthonormé (ONB).
 */
class OrthonormalBasisTest {

    @Test
    void testStandardBasisCalculation() {
        // Arrange: Utilise l'exemple de caméra standard du Jalon 2
        Point lookFrom = new Point(0, 0, 4);
        Point lookAt = new Point(0, 0, 0);
        Vector up = new Vector(0, 1, 0);
        Camera camera = new Camera(lookFrom, lookAt, up, 45);

        // Act
        OrthonormalBasis onb = new OrthonormalBasis(camera);

        // Assert: Vérifie les vecteurs de base
        // w = (lookFrom - lookAt).normalize() = (0,0,4).normalize() = (0,0,1)
        Vector expectedW = new Vector(0, 0, 1);

        // u = (up x w).normalize() = (0,1,0) x (0,0,1) = (1,0,0)
        Vector expectedU = new Vector(1, 0, 0);

        // v = (w x u).normalize() = (0,0,1) x (1,0,0) = (0,1,0)
        Vector expectedV = new Vector(0, 1, 0);

        assertEquals(expectedU, onb.getU(), "Vecteur U incorrect.");
        assertEquals(expectedV, onb.getV(), "Vecteur V incorrect.");
        assertEquals(expectedW, onb.getW(), "Vecteur W incorrect.");
    }
}
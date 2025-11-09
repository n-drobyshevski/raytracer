package math;

import math.Point;
import math.Ray;
import math.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour la classe Ray.
 */
class RayTest {

    @Test
    void testPointAt() {
        // Arrange
        Point origin = new Point(0, 0, 0);
        Vector direction = new Vector(1, 2, 3).normalize();
        Ray ray = new Ray(origin, direction);
        double t = 10.0;

        // Act
        Point p = ray.pointAt(t);

        // Assert
        Point expectedPoint = new Point(
                direction.getX() * t,
                direction.getY() * t,
                direction.getZ() * t
        );

        // Utilise la méthode equals() de AbstractVec3 qui gère l'EPSILON
        assertEquals(expectedPoint, p, "Le point calculé le long du rayon est incorrect.");
    }
}
package raytracer;

import math.Point;
import math.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    @Test
    void testCameraConstruction() {
        Point lookFrom = new Point(0, 0, -5);
        Point lookAt = new Point(0, 0, 0);
        Vector up = new Vector(0, 1, 0);
        double fov = 90.0;

        Camera camera = new Camera(lookFrom, lookAt, up, fov);

        assertEquals(lookFrom, camera.getLookFrom(), "LookFrom point should match constructor argument");
        assertEquals(lookAt, camera.getLookAt(), "LookAt point should match constructor argument");
        assertEquals(up, camera.getUp(), "Up vector should match constructor argument");
        assertEquals(fov, camera.getFov(), 0.0001, "Field of view should match constructor argument");
    }

    @Test
    void testCameraWithExtremeFOV() {
        Point lookFrom = new Point(0, 0, -5);
        Point lookAt = new Point(0, 0, 0);
        Vector up = new Vector(0, 1, 0);

        // Test with extreme FOV values
        Camera camera1 = new Camera(lookFrom, lookAt, up, 0.1);
        Camera camera2 = new Camera(lookFrom, lookAt, up, 179.9);

        assertEquals(0.1, camera1.getFov(), 0.0001, "Should handle very small FOV");
        assertEquals(179.9, camera2.getFov(), 0.0001, "Should handle very large FOV");
    }

    @Test
    void testCameraWithNonStandardOrientation() {
        Point lookFrom = new Point(1, 2, 3);
        Point lookAt = new Point(-1, -2, -3);
        Vector up = new Vector(0, 0, 1); // Using Z-up orientation
        double fov = 45.0;

        Camera camera = new Camera(lookFrom, lookAt, up, fov);

        assertEquals(up, camera.getUp(), "Should handle non-standard up vector");
        assertEquals(lookFrom, camera.getLookFrom(), "Should handle arbitrary position");
        assertEquals(lookAt, camera.getLookAt(), "Should handle arbitrary look-at point");
    }
}

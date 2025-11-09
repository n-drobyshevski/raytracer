package raytracer;

import imaging.Color;
import math.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectionalLightTest {

    @Test
    void testDirectionalLightConstruction() {
        Vector direction = new Vector(1, -1, 0);
        Color color = new Color(1, 1, 1);

        DirectionalLight light = new DirectionalLight(direction, color);

        assertEquals(direction, light.getDirection(), "Direction should match constructor argument");
        assertEquals(color, light.getColor(), "Color should match constructor argument");
    }

    @Test
    void testDirectionalLightWithNormalizedDirection() {
        Vector direction = new Vector(2, 0, 0); // Non-normalized vector
        Color color = new Color(0.5, 0.5, 0.5);

        DirectionalLight light = new DirectionalLight(direction, color);
        Vector retrievedDirection = light.getDirection();

        assertEquals(direction, retrievedDirection,
            "Direction vector should be preserved as provided");
    }

    @Test
    void testDirectionalLightWithZeroIntensityColor() {
        Vector direction = new Vector(0, 1, 0);
        Color color = new Color(0, 0, 0);

        DirectionalLight light = new DirectionalLight(direction, color);

        assertEquals(new Color(0, 0, 0), light.getColor(),
            "Should handle zero intensity light color");
    }
}

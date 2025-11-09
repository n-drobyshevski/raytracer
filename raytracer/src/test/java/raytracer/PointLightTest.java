package raytracer;

import imaging.Color;
import math.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointLightTest {

    @Test
    void testBasicPointLight() {
        Point position = new Point(1, 2, 3);
        Color color = new Color(0.5, 0.7, 0.9);

        PointLight light = new PointLight(position, color);

        assertEquals(position, light.getPosition(), "The light moved from where we put it");
        assertEquals(color, light.getColor(), "light color isn't right");
    }

    @Test
    void testPointLightAtOrigin() {
        Point origin = new Point(0, 0, 0);
        Color white = new Color(1, 1, 1);

        PointLight light = new PointLight(origin, white);

        assertEquals(origin, light.getPosition(), "The light should be at (0,0,0)");
        assertEquals(white, light.getColor(), "This should be pure white light (1,1,1)");
    }

    @Test
    void testPointLightWithDimColor() {
        Point pos = new Point(-1, -1, -1);
        Color dimColor = new Color(0.1, 0.1, 0.1);

        PointLight light = new PointLight(pos, dimColor);

        assertEquals(dimColor, light.getColor(), "the dim light isn't as dim as it should be");
        assertEquals(pos, light.getPosition(), "The light wandered off from (-1,-1,-1)");
        assertEquals(0.1, light.getColor().r(), 0.0001, "Red should be barely there (0,1)");
    }
}

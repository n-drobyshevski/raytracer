package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void testDefaultConstructor() {
        Color black = new Color();
        assertEquals(0.0, black.r(), AbstractVec3.EPSILON);
        assertEquals(0.0, black.g(), AbstractVec3.EPSILON);
        assertEquals(0.0, black.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testConstructorWithValues() {
        Color color = new Color(0.5, 0.7, 0.9);
        assertEquals(0.5, color.r(), AbstractVec3.EPSILON);
        assertEquals(0.7, color.g(), AbstractVec3.EPSILON);
        assertEquals(0.9, color.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testColorClamping() {
        Color color = new Color(1.5, -0.3, 2.0);
        // Values should be clamped between 0.0 and 1.0
        assertEquals(1.0, color.r(), AbstractVec3.EPSILON);
        assertEquals(0.0, color.g(), AbstractVec3.EPSILON);
        assertEquals(1.0, color.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testAddColors() {
        Color c1 = new Color(0.3, 0.4, 0.5);
        Color c2 = new Color(0.2, 0.3, 0.4);
        Color result = c1.add(c2);

        // Results are clamped if they exceed 1.0
        assertEquals(0.5, result.r(), AbstractVec3.EPSILON);
        assertEquals(0.7, result.g(), AbstractVec3.EPSILON);
        assertEquals(0.9, result.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testAddColorsWithClamping() {
        Color c1 = new Color(0.6, 0.7, 0.8);
        Color c2 = new Color(0.6, 0.7, 0.8);
        Color result = c1.add(c2);

        // Results should be clamped to 1.0
        assertEquals(1.0, result.r(), AbstractVec3.EPSILON);
        assertEquals(1.0, result.g(), AbstractVec3.EPSILON);
        assertEquals(1.0, result.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testMultiplyByScalar() {
        Color color = new Color(0.5, 0.6, 0.7);
        Color result = color.multiply(0.5);

        assertEquals(0.25, result.r(), AbstractVec3.EPSILON);
        assertEquals(0.30, result.g(), AbstractVec3.EPSILON);
        assertEquals(0.35, result.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testSchurProduct() {
        Color c1 = new Color(0.5, 0.6, 0.7);
        Color c2 = new Color(0.3, 0.4, 0.5);
        Color result = c1.schur(c2);

        assertEquals(0.15, result.r(), AbstractVec3.EPSILON);
        assertEquals(0.24, result.g(), AbstractVec3.EPSILON);
        assertEquals(0.35, result.b(), AbstractVec3.EPSILON);
    }

    @Test
    void testEquality() {
        Color c1 = new Color(0.5, 0.6, 0.7);
        Color c2 = new Color(0.5, 0.6, 0.7);
        Color c3 = new Color(0.51, 0.6, 0.7);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(null, c1);
    }
}

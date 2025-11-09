package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void testAddition() {
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(2.0, 3.0, 4.0);
        Vector result = v1.add(v2);

        assertEquals(3.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(5.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(7.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testSubtraction() {
        Vector v1 = new Vector(3.0, 4.0, 5.0);
        Vector v2 = new Vector(1.0, 2.0, 3.0);
        Vector result = v1.subtract(v2);

        assertEquals(2.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(2.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(2.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testMultiplyByScalar() {
        Vector v = new Vector(1.0, 2.0, 3.0);
        Vector result = v.multiply(2.0);

        assertEquals(2.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(4.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(6.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testDotProduct() {
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(2.0, 3.0, 4.0);
        double result = v1.dot(v2);

        // 1*2 + 2*3 + 3*4 = 2 + 6 + 12 = 20
        assertEquals(20.0, result, AbstractVec3.EPSILON);
    }

    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1.0, 0.0, 0.0);  // i vector
        Vector v2 = new Vector(0.0, 1.0, 0.0);  // j vector
        Vector result = v1.cross(v2);

        // i × j = k
        assertEquals(0.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(0.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(1.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testCrossProductAnticommutative() {
        Vector v1 = new Vector(2.0, 3.0, 4.0);
        Vector v2 = new Vector(5.0, 6.0, 7.0);

        Vector result1 = v1.cross(v2);
        Vector result2 = v2.cross(v1);

        // v1 × v2 = -(v2 × v1)
        assertEquals(-result1.getX(), result2.getX(), AbstractVec3.EPSILON);
        assertEquals(-result1.getY(), result2.getY(), AbstractVec3.EPSILON);
        assertEquals(-result1.getZ(), result2.getZ(), AbstractVec3.EPSILON);
    }
}

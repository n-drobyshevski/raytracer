package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testConstructorAndGetters() {
        Point p = new Point(1.0, 2.0, 3.0);
        assertEquals(1.0, p.getX(), AbstractVec3.EPSILON);
        assertEquals(2.0, p.getY(), AbstractVec3.EPSILON);
        assertEquals(3.0, p.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testSubtractPoints() {
        Point p1 = new Point(3.0, 4.0, 5.0);
        Point p2 = new Point(1.0, 1.0, 2.0);
        Vector result = p1.subtract(p2);

        assertEquals(2.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(3.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(3.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testAddVector() {
        Point p = new Point(1.0, 2.0, 3.0);
        Vector v = new Vector(2.0, 3.0, 4.0);
        Point result = p.add(v);

        assertEquals(3.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(5.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(7.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testMultiplyByScalar() {
        Point p = new Point(1.0, 2.0, 3.0);
        Point result = p.multiply(2.0);

        assertEquals(2.0, result.getX(), AbstractVec3.EPSILON);
        assertEquals(4.0, result.getY(), AbstractVec3.EPSILON);
        assertEquals(6.0, result.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testEquality() {
        Point p1 = new Point(1.0, 2.0, 3.0);
        Point p2 = new Point(1.0, 2.0, 3.0);
        Point p3 = new Point(1.1, 2.0, 3.0);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(null, p1);
    }
}

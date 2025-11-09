package math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AbstractVec3Test {

    private static class TestVec3 extends AbstractVec3 {
        public TestVec3(double x, double y, double z) {
            super(x, y, z);
        }
    }

    @Test
    void testGetters() {
        TestVec3 vec = new TestVec3(1.0, 2.0, 3.0);
        assertEquals(1.0, vec.getX(), AbstractVec3.EPSILON);
        assertEquals(2.0, vec.getY(), AbstractVec3.EPSILON);
        assertEquals(3.0, vec.getZ(), AbstractVec3.EPSILON);
    }

    @Test
    void testEquals() {
        TestVec3 vec1 = new TestVec3(1.0, 2.0, 3.0);
        TestVec3 vec2 = new TestVec3(1.0, 2.0, 3.0);
        TestVec3 vec3 = new TestVec3(1.1, 2.0, 3.0);

        assertEquals(vec1, vec2);
        assertNotEquals(vec1, vec3);
        assertNotEquals(null, vec1);
        assertEquals(vec1, vec1);
    }

    @Test
    void testEpsilonEquals() {
        TestVec3 vec1 = new TestVec3(1.0, 2.0, 3.0);
        TestVec3 vec2 = new TestVec3(1.0 + AbstractVec3.EPSILON/2, 2.0, 3.0);
        TestVec3 vec3 = new TestVec3(1.0 + AbstractVec3.EPSILON*2, 2.0, 3.0);

        assertEquals(vec1, vec2);
        assertNotEquals(vec1, vec3);
    }

    @Test
    void testToString() {
        TestVec3 vec = new TestVec3(1.0, 2.0, 3.0);
        String expected = "(1.000000, 2.000000, 3.000000)";
        assertEquals(expected, vec.toString());
    }
}

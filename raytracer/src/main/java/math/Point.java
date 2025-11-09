package math;

/**
 * Représente un point en 3D.
 */
public final class Point extends AbstractVec3 {

    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Soustraction de deux points, résultant en un vecteur.
     */
    public Vector subtract(Point other) {
        return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Addition d'un point et d'un vecteur, résultant en un point.
     */
    public Point add(Vector vector) {
        return new Point(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * Multiplication par un scalaire .
     */
    public Point multiply(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }
}
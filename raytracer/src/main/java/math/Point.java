package math;

/**
 * Représente un point en 3D.
 */
public final class Point extends AbstractVec3 {
    /**
     * Crée un nouveau point 3D.
     *
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Différence entre ce point et un autre.
     * @param other Point à soustraire
     * @return Vecteur allant de other vers this
     */
    public Vector subtract(Point other) {
        return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Translation du point par un vecteur.
     * @param vector Vecteur à ajouter
     * @return Nouveau point translaté
     */
    public Point add(Vector vector) {
        return new Point(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * Multiplication du point par un scalaire.
     * @param scalar Facteur multiplicateur
     * @return Nouveau point mis à l’échelle
     */
    public Point multiply(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }
}

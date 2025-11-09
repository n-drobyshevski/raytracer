package math;

/**
 * Représente un vecteur en 3D (direction ou normale).
 */
public final class Vector extends AbstractVec3 {

    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Addition
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Soustraction
     */
    public Vector subtract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Multiplication par un scalaire
     */
    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Produit scalaire
     */
    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Produit vectoriel
     */
    public Vector cross(Vector other) {
        return new Vector(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    /**
     * Produit de Schur (multiplication composante par composante)
     */
    public Vector schur(Vector other) {
        return new Vector(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    /**
     * Calcule la longueur du vecteur
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Calcule la longueur au carré (plus rapide que length()).
     */
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * Normalise le vecteur (le ramène à une longueur de 1)
     */
    public Vector normalize() {
        double len = length();
        if (len < EPSILON) {
            return new Vector(0, 0, 0);
        }
        return this.multiply(1.0 / len);
    }
}
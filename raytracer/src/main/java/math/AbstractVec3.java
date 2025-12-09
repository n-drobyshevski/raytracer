package math;

public abstract class AbstractVec3 { //

    /**
     * Constructeur vecteur 3D.
     *
     * @param x composante X
     * @param y composante Y
     * @param z composante Z
     */
    public static final double EPSILON = 1e-9;

    /** Retourne x. */
    protected final double x;
    /** Retourne y. */
    protected final double y;
    /** Retourne z. */
    protected final double z;

    protected AbstractVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Compare deux 'double' en utilisant la marge EPSILON.
     */
    protected boolean epsilonEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Méthode equals pour comparer deux objets Vec3
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AbstractVec3 that = (AbstractVec3) obj;

        return epsilonEquals(that.x, x) &&
                epsilonEquals(that.y, y) &&
                epsilonEquals(that.z, z);
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        result = 31 * result + Double.hashCode(z);
        return result;
    }
    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }
}

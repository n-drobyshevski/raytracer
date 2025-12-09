package imaging;

import math.AbstractVec3;

/**
 * Représente une couleur RGB
 * Les composantes (r, g, b) sont stockées en 'double' (via x, y, z)
 * et sont limitées entre 0.0 et 1.0
 */
public final class Color extends AbstractVec3 {

    /**
     * Constructeur par défaut : couleur Noire
     */
    public Color() {
        super(0, 0, 0);
    }

    /**
     * Crée une couleur à partir de ses trois composantes.
     *
     * @param r Rouge (0.0-1.0)
     * @param g Vert (0.0-1.0)
     * @param b Bleu (0.0-1.0)
     */
    public Color(double r, double g, double b) {
        super(clamp(r), clamp(g), clamp(b)); // Clampe les valeurs
    }

    /** Composante rouge. */
    public double r() { return this.x; }
    /** Composante verte. */
    public double g() { return this.y; }
    /** Composante bleue. */
    public double b() { return this.z; }

    /**
     * Addition de couleurs.
     */
    public Color add(Color other) {
        return new Color(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Multiplication par un scalaire (augmente/diminue l'intensité)
     */
    public Color multiply(double scalar) {
        return new Color(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    /**
     * Produit de Schur (mélange de couleurs)
     */
    public Color schur(Color other) {
        return new Color(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    /**
     * Limite une valeur 'double' entre 0.0 et 1.0
     */
    private static double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    /**
     * Convertit la couleur en entier RGB 8 bits.
     *
     * @return Entier au format 0xRRGGBB
     */
    public int toRGB() {
        int red   = (int) Math.round(this.x * 255);
        int green = (int) Math.round(this.y * 255);
        int blue  = (int) Math.round(this.z * 255);

        // Combine les composantes en un seul entier
        return ((red   & 0xff) << 16) +
                ((green & 0xff) << 8)  +
                (blue  & 0xff);
    }
}

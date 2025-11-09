package raytracer;

import imaging.Color;

/**
 * Classe parente abstraite pour les sources de lumière
 */
public abstract class AbstractLight {

    protected final Color color; // Couleur de la lumière

    /**
     * Constructeur pour une lumière.
     * @param color Couleur de la lumière
     */
    protected AbstractLight(Color color) {
        this.color = color;
    }

    public Color getColor() { return color; }
}
package raytracer;

import imaging.Color;
import math.Point;
import math.Vector;

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

    /** Couleur de la lumière. */
    public Color getColor() { return color; }

    /**
     * Calcule le vecteur L (direction VERS la lumière) depuis un point P.
     * @param p Le point de l'espace
     * @return Le vecteur direction normalisé vers la source lumineuse
     */
    public abstract Vector getL(Point p);

    /**
     * Calcule la distance de la lumière au point pour gérer l'ombre.
     * @param p Point d'origine
     * @return Distance à la source
     */
    public abstract double getDistance(Point p);
}

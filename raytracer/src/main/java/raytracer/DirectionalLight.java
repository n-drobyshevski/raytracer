package raytracer;

import imaging.Color;
import math.Vector;

/**
 * Lumière directionnelle globale
 */
public class DirectionalLight extends AbstractLight {

    private final Vector direction; // Direction de la lumière

    /**
     * Crée une lumière directionnelle.
     * @param direction Direction
     * @param color Couleur
     */
    public DirectionalLight(Vector direction, Color color) {
        super(color);
        this.direction = direction;
    }

    public Vector getDirection() { return direction; }
}
package raytracer;

import imaging.Color;
import math.Point;

/**
 * Source de lumière ponctuelle locale
 */
public class PointLight extends AbstractLight {

    private final Point position; // Position de la lumière

    /**
     * Crée une lumière ponctuelle.
     * @param position Position
     * @param color Couleur
     */
    public PointLight(Point position, Color color) {
        super(color);
        this.position = position;
    }

    public Point getPosition() { return position; }
}
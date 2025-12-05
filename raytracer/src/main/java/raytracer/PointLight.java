package raytracer;

import imaging.Color;
import math.Point;
import math.Vector;

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

    @Override
    public Vector getL(Point p) {
        // Le vecteur L est le vecteur allant du point P vers la position de la lumière
        // L = (LightPos - P) normalisé
        return this.position.subtract(p).normalize();
    }

    @Override
    public double getDistance(Point p) {
        // Distance euclidienne entre le point P et la position de la lumière
        Vector v = this.position.subtract(p);

        // On retourne la norme (longueur) du vecteur
        return Math.sqrt(v.dot(v));
    }
}
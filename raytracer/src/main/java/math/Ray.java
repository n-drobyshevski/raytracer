package math;

import math.Point;
import math.Vector;

/**
 * Représente un rayon
 * Un rayon est défini par une origine (Point) et une direction (Vecteur).
 */
public class Ray {

    private final Point origin;
    private final Vector direction;

    /**
     * Construit un nouveau rayon.
     * @param origin Le point d'origine du rayon
     * @param direction Le vecteur direction du rayon (devrait être normalisé)
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Point getOrigin() {
        return origin;
    }

    public Vector getDirection() {
        return direction;
    }

    /**
     * Calcule le point p à une distance t le long du rayon.
     * p = origin + direction * t
     */
    public Point pointAt(double t) {
        return origin.add(direction.multiply(t));
    }
}
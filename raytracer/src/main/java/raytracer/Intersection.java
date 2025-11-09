package raytracer;

import geometry.Shape;

/**
 * Représente les informations d'une intersection
 * Stocke la forme touchée et la distance 't' de l'intersection.
 */
public class Intersection {

    private final double t; // La distance le long du rayon
    private final Shape shape; // L'objet intersecté

    public Intersection(double t, Shape shape) {
        this.t = t;
        this.shape = shape;
    }

    public double getT() {
        return t;
    }

    public Shape getShape() {
        return shape;
    }
}
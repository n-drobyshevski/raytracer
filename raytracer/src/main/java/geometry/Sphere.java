package geometry;

import imaging.Color;
import math.Point;

/**
 * Représente un objet Sphère
 */
public class Sphere extends Shape {

    private final Point center; // Centre de la sphère
    private final double radius; // Rayon de la sphère

    /**
     * Crée une nouvelle sphère.
     * @param center Point central
     * @param radius Rayon
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     */
    public Sphere(Point center, double radius, Color diffuse, Color specular) {
        super(diffuse, specular);
        this.center = center;
        this.radius = radius;
    }

    // Getters
    public Point getCenter() { return center; }
    public double getRadius() { return radius; }
}
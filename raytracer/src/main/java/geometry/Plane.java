package geometry;

import imaging.Color;
import math.Point;
import math.Vector;

/**
 * Représente un plan infini
 */
public class Plane extends Shape {

    private final Point point;   // Un point appartenant au plan
    private final Vector normal; // Le vecteur normal au plan

    /**
     * Crée un nouveau plan.
     * @param point Un point sur le plan
     * @param normal La normale du plan
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     */
    public Plane(Point point, Vector normal, Color diffuse, Color specular) {
        super(diffuse, specular);
        this.point = point;
        double length = Math.sqrt(normal.dot(normal));
        this.normal = new Vector(
            normal.getX() / length,
            normal.getY() / length,
            normal.getZ() / length
        );
    }

    // Getters
    public Point getPoint() { return point; }
    public Vector getNormal() { return normal; }
}
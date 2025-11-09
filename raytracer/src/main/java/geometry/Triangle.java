package geometry;

import imaging.Color;
import math.Point;
import math.Ray;
import raytracer.Intersection;

import java.util.Optional;

/**
 * Représente un triangle, défini par trois sommets
 */
public class Triangle extends Shape {

    // Les trois sommets (vertex) du triangle
    private final Point a;
    private final Point b;
    private final Point c;

    /**
     * Crée un nouveau triangle.
     * @param a Sommet 1
     * @param b Sommet 2
     * @param c Sommet 3
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     */
    public Triangle(Point a, Point b, Point c, Color diffuse, Color specular) {
        super(diffuse, specular);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    // Getters
    public Point getA() { return a; }
    public Point getB() { return b; }
    public Point getC() { return c; }

    @Override
    public Optional<Intersection> intersect(Ray ray) {
        // Ce jalon se limite aux sphères
        return Optional.empty();
    }
}
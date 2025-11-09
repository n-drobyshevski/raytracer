package geometry;

import imaging.Color;
import math.Point;
import math.Ray;
import math.Vector;
import raytracer.Intersection;

import java.util.Optional;

/**
 * Représente un objet Sphère
 */
public class Sphere extends Shape {

    private final Point center; // Centre de la sphère
    private final double radius; // Rayon de la sphère

    /**
     * Crée une nouvelle sphère.
     *
     * @param center   Point central
     * @param radius   Rayon
     * @param diffuse  Couleur diffuse
     * @param specular Couleur spéculaire
     */
    public Sphere(Point center, double radius, Color diffuse, Color specular) {
        super(diffuse, specular);
        this.center = center;
        this.radius = radius;
    }

    // Getters
    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    /**
     * Calcule l'intersection d'un rayon avec la sphère
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        // o = origine du rayon
        // d = direction du rayon
        // c = centre de la sphère
        // r = rayon de la sphère

        // Vecteur (o - c)
        Vector o_minus_c = ray.getOrigin().subtract(this.center);

        // Résolution de a*t^2 + b*t + c = 0

        // a = d . d
        double a = ray.getDirection().dot(ray.getDirection());

        // b = 2 * (o - c) . d
        double b = 2.0 * o_minus_c.dot(ray.getDirection());

        // c = (o - c) . (o - c) - r^2
        double c = o_minus_c.dot(o_minus_c) - (this.radius * this.radius);

        // Calcul du discriminant Delta = b^2 - 4ac
        double delta = b * b - 4 * a * c;

        // Si Delta < 0, pas d'intersection
        if (delta < 0) {
            return Optional.empty();
        }

        double sqrtDelta = Math.sqrt(delta);

        // Calcul des deux solutions t1 et t2
        double t1 = (-b + sqrtDelta) / (2 * a);
        double t2 = (-b - sqrtDelta) / (2 * a);

        // On cherche la plus petite intersection positive
        // t2 est toujours plus petit que t1

        // Si t2 est positif, c'est le plus proche
        if (t2 > 0) {
            return Optional.of(new Intersection(t2, this));
        }

        // Sinon, si t1 est positif, c'est le plus proche
        if (t1 > 0) {
            return Optional.of(new Intersection(t1, this));
        }

        // Sinon, les deux intersections sont négatives (derrière la caméra)
        return Optional.empty();
    }
}
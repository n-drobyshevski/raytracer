package geometry;

import imaging.Color;
import math.Point;
import math.Ray;
import math.Vector;
import raytracer.Intersection;

import java.util.Optional;

/**
 * Classe représentant une sphère dans l'espace 3D.
 * Fournit le calcul d'intersection rayon-sphère pour le raytracing.
 */
public class Sphere extends Shape {

    private final Point center; // Centre de la sphère
    private final double radius; // Rayon de la sphère

    /**
     * Constructeur d'une sphère.
     *
     * @param center Point central
     * @param radius Rayon
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     * @param shininess Exposant de brillance
     */

    public Sphere(Point center, double radius, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.center = center;
        this.radius = radius;
    }

    // Getters
    /** Centre de la sphère. */
    public Point getCenter() {
        return center;
    }

    /** Rayon de la sphère. */
    public double getRadius() {
        return radius;
    }

    /**
     * Calcule l'intersection (point, normale) entre le rayon et la sphère.
     *
     * @param ray Le rayon à tester
     * @return Un Optional contenant l'intersection, vide sinon
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
        double t = -1;
        if (t2 > 0) {
            t = t2;
        } else if (t1 > 0) {
            t = t1;
        } else {
            // Toutes les intersections sont derrière la caméra
            return Optional.empty();
        }

        // --- MISE À JOUR JALON 4 : Calcul des données pour l'éclairage ---

        // 1. Point d'intersection P = Origine + t * Direction
        Point p = ray.pointAt(t);

        // 2. Normale N = (P - Centre) / ||P - Centre||
        // La normale doit toujours être normalisée (longueur 1)
        Vector normal = p.subtract(this.center).normalize();

        // 3. Retourne l'intersection enrichie avec P, la Normale, et la couleur diffuse
        return Optional.of(new Intersection(
                p,
                normal,
                this.getDiffuse(),
                this.getSpecular(),
                this.getShininess(),
                t,
                this
        ));
    }
}

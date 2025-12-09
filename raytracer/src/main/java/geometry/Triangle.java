package geometry;

import imaging.Color;
import math.Point;
import math.Ray;
import math.Vector;
import raytracer.Intersection;

import java.util.Optional;

/**
 * Classe représentant un triangle dans l'espace 3D (trois sommets).
 * Utilise l'algorithme de Möller-Trumbore pour l'intersection rayon-triangle.
 */
public class Triangle extends Shape {

    /**
     * Crée un triangle à partir de 3 sommets.
     *
     * @param a Point A
     * @param b Point B
     * @param c Point C
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     * @param shininess Exposant de brillance
     */
    private final Point a;
    private final Point b;
    private final Point c;
    private final Vector normal; // Normale calculée

    public Triangle(Point a, Point b, Point c, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.a = a;
        this.b = b;
        this.c = c;

        // Calcul de la normale du triangle (produit vectoriel des arêtes)
        Vector edge1 = b.subtract(a);
        Vector edge2 = c.subtract(a);
        this.normal = edge1.cross(edge2).normalize();
    }

    /** Premier sommet. */
    public Point getA() { return a; }
    /** Deuxième sommet. */
    public Point getB() { return b; }
    /** Troisième sommet. */
    public Point getC() { return c; }

    /**
     * Calcule l'intersection entre ce triangle et le rayon.
     *
     * @param ray Le rayon à tester
     * @return Un Optional contenant l'intersection, vide sinon
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        // Algorithme d'intersection de Möller-Trumbore
        Vector edge1 = b.subtract(a);
        Vector edge2 = c.subtract(a);

        Vector pvec = ray.getDirection().cross(edge2);
        double det = edge1.dot(pvec);

        // Si le déterminant est proche de 0, le rayon est parallèle au triangle
        if (Math.abs(det) < 1e-8) {
            return Optional.empty();
        }

        double invDet = 1.0 / det;
        Vector tvec = ray.getOrigin().subtract(a);

        // Calcul de la coordonnée barycentrique u
        double u = tvec.dot(pvec) * invDet;
        if (u < 0.0 || u > 1.0) {
            return Optional.empty();
        }

        Vector qvec = tvec.cross(edge1);

        // Calcul de la coordonnée barycentrique v
        double v = ray.getDirection().dot(qvec) * invDet;
        if (v < 0.0 || u + v > 1.0) {
            return Optional.empty();
        }

        // Calcul de t
        double t = edge2.dot(qvec) * invDet;

        if (t > 1e-8) { // Intersection valide devant la caméra
            Point p = ray.pointAt(t);

            // On retourne l'intersection avec la normale pré-calculée
            return Optional.of(new Intersection(
                    p,
                    this.normal,
                    this.getDiffuse(),
                    this.getSpecular(),
                    this.getShininess(),
                    t,
                    this
            ));
        }

        return Optional.empty();
    }
}

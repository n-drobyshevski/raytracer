package geometry;

import imaging.Color;
import math.Point;
import math.Ray;
import math.Vector;
import raytracer.Intersection;

import java.util.Optional;

/**
 * Représente un plan infini défini par un point et une normale.
 */
public class Plane extends Shape {
    /**
     * Constructeur d'un plan.
     *
     * @param point Un point appartenant au plan
     * @param normal Vecteur normal au plan (sera normalisé)
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     * @param shininess Exposant de brillance
     */
    private final Point point;   // Un point appartenant au plan
    private final Vector normal; // Le vecteur normal au plan

    public Plane(Point point, Vector normal, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.point = point;
        this.normal = normal.normalize(); // On s'assure que la normale est normalisée
    }

    /** Un point du plan. */
    public Point getPoint() { return point; }
    /** Normale du plan. */
    public Vector getNormal() { return normal; }

    /**
     * Calcule l'intersection entre le rayon et ce plan.
     *
     * @param ray Le rayon à tester
     * @return Un Optional contenant l'intersection, vide sinon
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        // Formule d'intersection Rayon-Plan :
        // t = ((point_plan - origine_rayon) . normale_plan) / (direction_rayon . normale_plan)

        double denominator = ray.getDirection().dot(this.normal);

        // Si le dénominateur est proche de 0, le rayon est parallèle au plan -> Pas d'intersection
        if (Math.abs(denominator) < 1e-6) {
            return Optional.empty();
        }

        Vector originToPlane = this.point.subtract(ray.getOrigin());
        double t = originToPlane.dot(this.normal) / denominator;

        // L'intersection doit être devant la caméra (t > 0)
        if (t > 1e-6) {
            Point p = ray.pointAt(t);

            // Pour un plan, la normale est constante
            // (Optionnel : si on veut voir le plan des deux côtés, on peut inverser la normale si denominator > 0)
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

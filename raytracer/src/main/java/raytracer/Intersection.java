package raytracer;

import geometry.Shape;
import imaging.Color;
import math.Point;
import math.Vector;

/**
 * Représente les informations d'une intersection
 * Stocke la forme touchée et la distance 't' de l'intersection.
 */
public class Intersection {


    private final double t;         // Distance
    private final Shape shape;      // Objet touché
    private final Point point;      // Point d'intersection exact (P)
    private final Vector normal;    // Normale à la surface en P (N)
    private final Color diffuse;    // Couleur diffuse de l'objet à cet endroit

    public Intersection(Point point, Vector normal, Color diffuse, double t, Shape shape) {
        this.point = point;
        this.normal = normal;
        this.diffuse = diffuse;
        this.t = t;
        this.shape = shape;
    }
    public double getT() { return t; }
    public Shape getShape() { return shape; }
    public Point getPoint() { return point; }
    public Vector getNormal() { return normal; }
    public Color getDiffuse() { return diffuse; }

    /**
     * Calcule la contribution de couleur d'une source de lumière selon le modèle de Lambert.
     * Formule : ld = max(n . l, 0) * lightColor * diffuseColor
     * @param light La source de lumière
     * @return La couleur résultante pour cette lumière
     */
    public Color calculateColor(AbstractLight light) {
        // 1. Vecteur vers la lumière (L)
        Vector l = light.getL(this.point);

        // 2. Produit scalaire N . L (Lambert)
        double nDotL = this.normal.dot(l);

        // 3. On ne garde que la lumière venant de "devant" la surface (max(0, dot))
        double intensity = Math.max(nDotL, 0.0);

        // 4. Calcul final : Intensité * CouleurLumière * CouleurObjet
        // On utilise schur() pour multiplier les couleurs composante par composante
        Color lightColor = light.getColor();

        return lightColor.multiply(intensity).schur(this.diffuse);
    }
}
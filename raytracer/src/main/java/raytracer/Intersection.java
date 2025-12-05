package raytracer;

import geometry.Shape;
import imaging.Color;
import math.Point;
import math.Vector;

public class Intersection {

    private final Point point;
    private final Vector normal;
    private final Color diffuse;
    private final Color specular;   // Ajout Jalon 5
    private final double shininess; // Ajout Jalon 5
    private final double t;
    private final Shape shape;

    public Intersection(Point point, Vector normal, Color diffuse, Color specular, double shininess, double t, Shape shape) {
        this.point = point;
        this.normal = normal;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.t = t;
        this.shape = shape;
    }

    public Point getPoint() { return point; }
    public double getT() { return t; }
    public Shape getShape() { return shape; }

    /**
     * Calcule la couleur (Lambert + Blinn-Phong).
     * @param light La lumière
     * @param eyeDir Le vecteur direction vers l'œil (nécessaire pour Phong)
     */
    public Color calculateColor(AbstractLight light, Vector eyeDir) {
        Vector l = light.getL(this.point);
        Color lightColor = light.getColor();

        // --- 1. Diffuse (Lambert) ---
        double nDotL = Math.max(this.normal.dot(l), 0.0);
        Color diffuseTerm = lightColor.multiply(nDotL).schur(this.diffuse);

        // --- 2. Specular (Blinn-Phong) ---
        Color specularTerm = new Color(0, 0, 0);

        // On ne calcule le spéculaire que si la lumière éclaire la surface (nDotL > 0)
        if (nDotL > 0) {
            // Vecteur H (Halfway) = (LightDir + EyeDir) normalisé
            Vector h = l.add(eyeDir).normalize();

            double nDotH = Math.max(this.normal.dot(h), 0.0);
            double specularIntensity = Math.pow(nDotH, this.shininess);

            specularTerm = lightColor.multiply(specularIntensity).schur(this.specular);
        }

        return diffuseTerm.add(specularTerm);
    }
}
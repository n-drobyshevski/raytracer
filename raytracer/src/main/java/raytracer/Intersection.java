package raytracer;

import geometry.Shape;
import imaging.Color;
import math.Point;
import math.Vector;

public class Intersection {
    /**
     * Crée une intersection.
     * @param point Point d'impact
     * @param normal Normale au point
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     * @param shininess Expo brillance
     * @param t Distance sur le rayon
     * @param shape Objet touché
     */
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
    public Vector getNormal() { return normal; }
    public double getT() { return t; }
    public Shape getShape() { return shape; }
    public Color getDiffuse() { return diffuse; }

    // --- NOUVEAUX GETTERS (Jalon 6 requis) ---
    public Color getSpecular() { return specular; }
    public double getShininess() { return shininess; }

    /**
     * Calcule la couleur en ce point selon les modèles d’éclairage.
     * @param light Lumière considérée
     * @param eyeDir Direction vers la caméra
     * @return La couleur calculée
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

package geometry;

import imaging.Color;
import math.Ray;
import raytracer.Intersection;

import java.util.Optional;

/**
 * Classe parente abstraite pour toutes les formes géométriques.
 * Stocke les propriétés matérielles de base (couleurs).
 */
public abstract class Shape {

    protected final Color diffuse;   // Couleur diffuse de l'objet
    protected final Color specular;  // Couleur spéculaire de l'objet
    protected final double shininess; // Exposant de brillance

    /**
     * Constructeur pour une forme.
     *
     * @param diffuse  Couleur diffuse
     * @param specular Couleur spéculaire
     */
    protected Shape(Color diffuse, Color specular, double shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    // Getters
    public Color getDiffuse() {
        return diffuse;
    }

    public Color getSpecular() {
        return specular;
    }

    public double getShininess() {
        return shininess;
    }

    /**
     * Calcule l'intersection entre un rayon et la forme.
     * C'est à la forme de connaître sa propre équation
     *
     * @param ray Le rayon à tester
     * @return Un Optional<Intersection>
     */
    public abstract Optional<Intersection> intersect(Ray ray);
}
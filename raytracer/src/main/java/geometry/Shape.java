package geometry;

import imaging.Color;

/**
 * Classe parente abstraite pour toutes les formes géométriques.
 * Stocke les propriétés matérielles de base (couleurs).
 */
public abstract class Shape {

    protected final Color diffuse;   // Couleur diffuse de l'objet
    protected final Color specular;  // Couleur spéculaire de l'objet

    /**
     * Constructeur pour une forme.
     * @param diffuse Couleur diffuse
     * @param specular Couleur spéculaire
     */
    public Shape(Color diffuse, Color specular) {
        this.diffuse = diffuse;
        this.specular = specular;
    }

    // Getters
    public Color getDiffuse() { return diffuse; }
    public Color getSpecular() { return specular; }
}
package raytracer;

import imaging.Color;
import math.Vector;
import math.Point;

/**
 * Lumière directionnelle globale
 */
public class DirectionalLight extends AbstractLight {

    private final Vector direction; // Direction de la lumière

    /**
     * Crée une lumière directionnelle.
     * @param direction Direction
     * @param color Couleur
     */
    public DirectionalLight(Vector direction, Color color) {
        super(color);
        this.direction = direction;
    }

    public Vector getDirection() { return direction; }

    @Override
    public Vector getL(Point p) {
        // Pour une lumière directionnelle, le vecteur L est constant.
        // On suppose que le vecteur fourni dans le fichier est la direction VERS la lumière.
        return this.direction;
    }
}
package raytracer;

import math.Point;
import math.Vector;

/**
 * Représente la caméra et les paramètres de vue
 */
public class Camera {

    private final Point lookFrom; // Position de l'œil (x, y, z)
    private final Point lookAt;   // Point visé (u, v, w)
    private final Vector up;      // Vecteur "haut" (m, n, o)
    private final double fov;     // Angle de vue (f)

    /**
     * Crée une nouvelle caméra.
     * @param lookFrom Position de l'œil
     * @param lookAt Point visé
     * @param up Vecteur "haut"
     * @param fov Angle de vue en degrés
     */
    public Camera(Point lookFrom, Point lookAt, Vector up, double fov) {
        this.lookFrom = lookFrom;
        this.lookAt = lookAt;
        this.up = up;
        this.fov = fov;
    }

    // Getters
    public Point getLookFrom() { return lookFrom; }
    public Point getLookAt() { return lookAt; }
    public Vector getUp() { return up; }
    public double getFov() { return fov; }
}
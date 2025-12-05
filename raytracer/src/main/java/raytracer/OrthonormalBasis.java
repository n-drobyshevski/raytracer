package raytracer;

import raytracer.Camera;
import math.Vector;

/**
 * Représente le repère orthonormé (u, v, w) de la caméra
 * Les calculs sont effectués dans le constructeur
 */
public class OrthonormalBasis {

    private final Vector u, v, w;

    /**
     * Calcule le repère (u, v, w) à partir de la caméra.
     * @param camera L'objet Camera contenant lookFrom, lookAt, et up.
     */
    public OrthonormalBasis(Camera camera) {
        // w = (lookFrom - lookAt) / ||lookFrom - lookAt||
        this.w = camera.getLookFrom().subtract(camera.getLookAt()).normalize();

        // u = (up x w) / ||up x w||
        this.u = camera.getUp().cross(this.w).normalize();

        // v = (w x u) / ||w x u||
        this.v = this.w.cross(this.u).normalize();
    }

    // Getters
    public Vector getU() { return u; }
    public Vector getV() { return v; }
    public Vector getW() { return w; }
}
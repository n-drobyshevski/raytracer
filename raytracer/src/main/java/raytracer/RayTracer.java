package raytracer;

import imaging.Color;
import math.Ray;
import math.Vector;
import math.Point;
import java.util.Optional;
import scene.Scene;

/**
 * Moteur raytracing principal : calcule la couleur d’un pixel par lancer de rayon et récursion.
 */

public class RayTracer {
    /**
     * Instancie le core raytracer.
     * @param scene Scène à lancer
     */
    private final Scene scene;
    private final OrthonormalBasis onb;
    private final double pixelWidth;
    private final double pixelHeight;
    private final int width;
    private final int height;

    public RayTracer(Scene scene) {
        this.scene = scene;
        this.width = scene.getWidth();
        this.height = scene.getHeight();
        this.onb = new OrthonormalBasis(scene.getCamera());

        double fovr = Math.toRadians(scene.getCamera().getFov());
        this.pixelHeight = Math.tan(fovr / 2.0);

        double aspectRatio = (double)this.width / (double)this.height;
        this.pixelWidth = this.pixelHeight * aspectRatio;
    }

    /**
     * Calcule la couleur du pixel (i, j).
     * @param i Abscisse
     * @param j Ordonnée
     * @return Couleur calculée
     */
    public Color getPixelColor(int i, int j) {
        Ray viewRay = calculateRay(i, j);
        return computeColor(viewRay, 1);
    }

    /**
     * Méthode récursive pour calculer la couleur (Directe + Réfléchie).
     * @param ray Le rayon à tracer
     * @param depth La profondeur actuelle de récursion
     * @return La couleur résultante
     */
    private Color computeColor(Ray ray, int depth) {
        // Arrêt de la récursion si on dépasse maxdepth
        if (depth > scene.getMaxDepth()) {
            return new Color(0, 0, 0);
        }

        Optional<Intersection> closestIntersection = scene.findClosestIntersection(ray);

        if (closestIntersection.isPresent()) {
            Intersection intersection = closestIntersection.get();
            Point p = intersection.getPoint();
            Vector n = intersection.getNormal();

            // Vecteur vue (inverse du rayon incident)
            Vector eyeDir = ray.getDirection().multiply(-1).normalize();

            // --- 1. Eclairage Direct (Lambert + Phong) ---
            Color finalColor = scene.getAmbient(); // On part de l'ambiante

            for (AbstractLight light : scene.getLights()) {
                Vector l = light.getL(p);
                double distToLight = light.getDistance(p);

                Ray shadowRay = new Ray(p, l);
                boolean isInShadow = scene.isShadowed(shadowRay, distToLight);

                if (!isInShadow) {
                    Color contribution = intersection.calculateColor(light, eyeDir);
                    finalColor = finalColor.add(contribution);
                }
            }

            // --- 2. Eclairage Indirect (Réflexion - JALON 6) ---
            // On ne calcule la réflexion que si l'objet est spéculaire (brillant)
            // et qu'on n'a pas atteint la limite de profondeur.
            Color specularColor = intersection.getSpecular();

            // Vérifie si l'objet a une composante spéculaire (n'est pas noir)
            boolean isReflective = (specularColor.r() > 0 || specularColor.g() > 0 || specularColor.b() > 0);

            if (isReflective && depth < scene.getMaxDepth()) {
                // Calcul du rayon réfléchi R
                // Formule : r = d + 2 * (n . (-d)) * n
                // Ici ray.getDirection() est 'd'. eyeDir est '-d'.
                double nDotV = n.dot(eyeDir);
                Vector rDir = ray.getDirection().add(n.multiply(2 * nDotV)).normalize();

                // Créer le rayon réfléchi (partant de P avec un léger décalage pour éviter l'auto-intersection)
                Ray reflectedRay = new Ray(p, rDir);

                // Appel récursif
                Color reflectedColor = computeColor(reflectedRay, depth + 1);

                // Mélange : CouleurFinale += Specular * ReflectedColor
                finalColor = finalColor.add(specularColor.schur(reflectedColor));
            }

            return finalColor;
        } else {
            return new Color(0, 0, 0); // Fond noir
        }
    }

    private Ray calculateRay(int i, int j) {
        double a = (pixelWidth * (i - (width / 2.0) + 0.5)) / (width / 2.0);
        double b = (pixelHeight * ((height / 2.0) - j + 0.5)) / (height / 2.0);

        Vector d = onb.getU().multiply(a)
                .add(onb.getV().multiply(b))
                .subtract(onb.getW())
                .normalize();

        Point origin = scene.getCamera().getLookFrom();
        return new Ray(origin, d);
    }
}

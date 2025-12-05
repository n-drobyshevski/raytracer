package raytracer;

import imaging.Color;
import math.Ray;
import math.Vector;
import math.Point;
import raytracer.OrthonormalBasis;
import scene.Scene;

import java.util.Optional;

/**
 * Classe principale du traceur de rayons
 * Calcule la couleur pour chaque pixel.
 */
public class RayTracer {

    private final Scene scene;
    private final OrthonormalBasis onb;
    private final double pixelWidth;
    private final double pixelHeight;
    private final int width;
    private final int height;

    /**
     * Construit un RayTracer pour une scène donnée.
     * Pré-calcule les dimensions du plan de vue.
     */
    public RayTracer(Scene scene) {
        this.scene = scene;
        this.width = scene.getWidth();
        this.height = scene.getHeight();
        this.onb = new OrthonormalBasis(scene.getCamera());

        // Calcul des dimensions des pixels dans la scène

        // fovr = (fov * PI) / 180
        double fovr = Math.toRadians(scene.getCamera().getFov());

        // pixelheight = tan(fovr / 2)
        this.pixelHeight = Math.tan(fovr / 2.0);

        // pixelwidth = pixelheight * (imgwidth / imgheight)
        double aspectRatio = (double)this.width / (double)this.height;
        this.pixelWidth = this.pixelHeight * aspectRatio;
    }

    /**
     * Calcule la couleur pour un pixel spécifique (i, j)
     * @param i Coordonnée X du pixel
     * @param j Coordonnée Y du pixel
     * @return La couleur du pixel
     */
    public Color getPixelColor(int i, int j) {

        // 1. Calculer le rayon (View Ray)
        Ray viewRay = calculateRay(i, j);

        // 2. Rechercher l'intersection la plus proche
        Optional<Intersection> closestIntersection = scene.findClosestIntersection(viewRay);

        // 3. Calculer la couleur
        if (closestIntersection.isPresent()) {
            Intersection intersection = closestIntersection.get();
            Point p = intersection.getPoint();

            // --- JALON 5 : Calculs Phong et Ombres ---

            // 1. Calculer EyeDir (Vecteur du point vers la caméra)
            // C'est l'inverse de la direction du rayon de vue
            Vector eyeDir = viewRay.getDirection().multiply(-1).normalize();

            Color finalColor = scene.getAmbient();

            for (AbstractLight light : scene.getLights()) {
                Vector l = light.getL(p);
                double distToLight = light.getDistance(p);

                // 2. Gestion des ombres (Shadow Ray)
                Ray shadowRay = new Ray(p, l);
                boolean isInShadow = scene.isShadowed(shadowRay, distToLight);

                if (!isInShadow) {
                    // 3. Appel corrigé avec eyeDir
                    Color contribution = intersection.calculateColor(light, eyeDir);
                    finalColor = finalColor.add(contribution);
                }
            }

            return finalColor;
        } else {
            // Sinon, utiliser du noir
            return new Color(0, 0, 0); // Noir
        }
    }

    /**
     * Calcule le vecteur direction pour le pixel (i,j)
     */
    private Ray calculateRay(int i, int j) {
        // Formules de calcul de la direction d
        double a = (pixelWidth * (i - (width / 2.0) + 0.5)) / (width / 2.0);

        // Note: L'axe Y est inversé entre Java (j=0 en haut) et la scène (+v en haut)
        // On inverse le 'j' pour compenser
        double b = (pixelHeight * ((height / 2.0) - j + 0.5)) / (height / 2.0);

        // d = (u*a + v*b - w) / ||...||
        Vector d = onb.getU().multiply(a)
                .add(onb.getV().multiply(b))
                .subtract(onb.getW())
                .normalize();

        Point origin = scene.getCamera().getLookFrom();
        return new Ray(origin, d);
    }
}
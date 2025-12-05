package scene;

import geometry.Shape;
import imaging.Color;
import math.Ray;
import raytracer.AbstractLight;
import raytracer.Camera;
import raytracer.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe conteneur pour tous les éléments de la scène
 */
public class Scene {

    private int width;
    private int height;
    private Camera camera;
    private String output = "output.png";
    private Color ambient = new Color(0, 0, 0);
    private final List<AbstractLight> lights = new ArrayList<>();
    private final List<Shape> shapes = new ArrayList<>();
    private int maxDepth = 1;
    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Camera getCamera() { return camera; }
    public String getOutput() { return output; }
    public Color getAmbient() { return ambient; }
    public List<AbstractLight> getLights() { return lights; }
    public List<Shape> getShapes() { return shapes; }

    public int getMaxDepth() { return maxDepth; }
    public void setMaxDepth(int maxDepth) { this.maxDepth = maxDepth; }
    // Setters
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setCamera(Camera camera) { this.camera = camera; }
    public void setOutput(String output) { this.output = output; }
    public void setAmbient(Color ambient) { this.ambient = ambient; }

    // Méthodes pour ajouter aux listes
    public void addLight(AbstractLight light) {
        this.lights.add(light);
    }
    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    /**
     * Calcule l'intersection la plus proche d'un rayon avec les objets de la scène
     * @param ray Le rayon à tester
     * @return Un Optional<Intersection> contenant l'intersection la plus proche,
     * ou vide s'il n'y a pas d'intersection.
     */
    public Optional<Intersection> findClosestIntersection(Ray ray) {
        Optional<Intersection> closest = Optional.empty();
        double minT = Double.MAX_VALUE;

        for (Shape shape : shapes) {
            Optional<Intersection> hit = shape.intersect(ray);
            if (hit.isPresent()) {
                double t = hit.get().getT();
                if (t > 1e-4 && t < minT) { // epsilon pour éviter l'auto-intersection
                    minT = t;
                    closest = hit;
                }
            }
        }
        return closest;
    }
    /**
     * Vérifie si un point est à l'ombre pour une lumière donnée.
     * @param shadowRay Le rayon qui part du point vers la lumière
     * @param lightDistance La distance jusqu'à la lumière (pour ne pas tester au-delà)
     * @return true si un objet bloque la lumière
     */
    public boolean isShadowed(Ray shadowRay, double lightDistance) {
        for (Shape shape : shapes) {
            Optional<Intersection> hit = shape.intersect(shadowRay);
            if (hit.isPresent()) {
                double t = hit.get().getT();
                if (t > 1e-4 && t < lightDistance) {
                    return true;
                }
            }
        }
        return false;
    }
}
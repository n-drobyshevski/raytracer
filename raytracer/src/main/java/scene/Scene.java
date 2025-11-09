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

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Camera getCamera() { return camera; }
    public String getOutput() { return output; }
    public Color getAmbient() { return ambient; }
    public List<AbstractLight> getLights() { return lights; }
    public List<Shape> getShapes() { return shapes; }

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
        Optional<Intersection> closestIntersection = Optional.empty();
        double minT = Double.MAX_VALUE;

        // On calcule les intersections possibles avec tous les objets
        for (Shape shape : shapes) {
            Optional<Intersection> currentIntersection = shape.intersect(ray);

            if (currentIntersection.isPresent()) {
                Intersection intersection = currentIntersection.get();
                double t = intersection.getT();

                // On garde la plus petite distance 't' positive
                if (t > 0 && t < minT) {
                    minT = t;
                    closestIntersection = currentIntersection;
                }
            }
        }
        // On retourne la plus petite trouvée
        return closestIntersection;
    }
}
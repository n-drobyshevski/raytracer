package scene;

import geometry.Shape;
import imaging.Color;
import raytracer.AbstractLight;
import raytracer.Camera;

import java.util.ArrayList;
import java.util.List;

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
}
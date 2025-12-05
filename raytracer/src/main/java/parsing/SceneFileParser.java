package parsing;

import geometry.*;
import raytracer.*;
import imaging.Color;
import math.Point;
import math.Vector;
import scene.Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SceneFileParser {

    private Color currentDiffuse = new Color(0, 0, 0);
    private Color currentSpecular = new Color(0, 0, 0);
    private double currentShininess = 0.0; // Shininess par défaut

    private int maxVerts = 0;
    private List<Point> vertices = new ArrayList<>();
    private Color totalLightColor = new Color(0, 0, 0);

    private boolean sizeSet = false;
    private boolean cameraSet = false;

    public Scene parse(String filePath) throws IOException, ParsingException {
        Scene scene = new Scene();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] tokens = line.split("\\s+");
                String command = tokens[0];

                switch (command) {
                    case "size": parseSize(tokens, scene); break;
                    case "output": parseOutput(tokens, scene); break;
                    case "camera": parseCamera(tokens, scene); break;
                    case "ambient": parseAmbient(tokens, scene); break;
                    case "diffuse": parseDiffuse(tokens, scene); break;
                    case "specular": parseSpecular(tokens); break;
                    case "shininess": parseShininess(tokens); break; // Ajout Jalon 5
                    case "directional": parseDirectional(tokens, scene); break;
                    case "point": parsePoint(tokens, scene); break;
                    case "maxverts": parseMaxVerts(tokens); break;
                    case "vertex": parseVertex(tokens); break;
                    case "sphere": parseSphere(tokens, scene); break;
                    case "tri": parseTri(tokens, scene); break;
                    case "plane": parsePlane(tokens, scene); break;
                    default: break;
                }
            }
        }
        if (!sizeSet) throw new ParsingException("Erreur: 'size' non défini.");
        if (!cameraSet) throw new ParsingException("Erreur: 'camera' non défini.");
        return scene;
    }

    private void parseSize(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 3) throw new ParsingException("Erreur 'size'");
        scene.setWidth(Integer.parseInt(tokens[1]));
        scene.setHeight(Integer.parseInt(tokens[2]));
        sizeSet = true;
    }

    private void parseOutput(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 2) throw new ParsingException("Erreur 'output'");
        scene.setOutput(tokens[1]);
    }

    private void parseCamera(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 11) throw new ParsingException("Erreur 'camera'");
        scene.setCamera(new Camera(parsePoint(tokens, 1), parsePoint(tokens, 4), parseVector(tokens, 7), Double.parseDouble(tokens[10])));
        cameraSet = true;
    }

    private void parseAmbient(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'ambient'");
        scene.setAmbient(parseColor(tokens, 1));
        checkDiffuseSum(scene.getAmbient(), currentDiffuse);
    }

    private void parseDiffuse(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'diffuse'");
        currentDiffuse = parseColor(tokens, 1);
        checkDiffuseSum(scene.getAmbient(), currentDiffuse);
    }

    private void parseSpecular(String[] tokens) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'specular'");
        currentSpecular = parseColor(tokens, 1);
    }

    private void parseShininess(String[] tokens) throws ParsingException {
        if (tokens.length != 2) throw new ParsingException("Erreur 'shininess'");
        try {
            currentShininess = Double.parseDouble(tokens[1]);
        } catch (NumberFormatException e) {
            throw new ParsingException("Erreur 'shininess': invalide.");
        }
    }

    private void parseDirectional(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 7) throw new ParsingException("Erreur 'directional'");
        Vector dir = parseVector(tokens, 1);
        Color col = parseColor(tokens, 4);
        validateLightIntensity(col);
        scene.addLight(new DirectionalLight(dir, col));
    }

    private void parsePoint(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 7) throw new ParsingException("Erreur 'point'");
        Point pos = parsePoint(tokens, 1);
        Color col = parseColor(tokens, 4);
        validateLightIntensity(col);
        scene.addLight(new PointLight(pos, col));
    }

    private void parseMaxVerts(String[] tokens) throws ParsingException {
        if (tokens.length != 2) throw new ParsingException("Erreur 'maxverts'");
        maxVerts = Integer.parseInt(tokens[1]);
        vertices = new ArrayList<>(maxVerts);
    }

    private void parseVertex(String[] tokens) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'vertex'");
        if (vertices.size() >= maxVerts) throw new ParsingException("Erreur: trop de vertex.");
        vertices.add(parsePoint(tokens, 1));
    }

    private void parseSphere(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 5) throw new ParsingException("Erreur 'sphere'");
        Point c = parsePoint(tokens, 1);
        double r = Double.parseDouble(tokens[4]);
        scene.addShape(new Sphere(c, r, currentDiffuse, currentSpecular, currentShininess));
    }

    private void parseTri(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'tri'");
        int iA = Integer.parseInt(tokens[1]);
        int iB = Integer.parseInt(tokens[2]);
        int iC = Integer.parseInt(tokens[3]);

        if (iA >= maxVerts || iB >= maxVerts || iC >= maxVerts || iA < 0 || iB < 0 || iC < 0)
            throw new ParsingException("Erreur 'tri': indices invalides.");

        Point a = vertices.get(iA);
        Point b = vertices.get(iB);
        Point c = vertices.get(iC);

        // CORRECTION JALON 5 : Ajout de currentShininess
        scene.addShape(new Triangle(a, b, c, currentDiffuse, currentSpecular, currentShininess));
    }

    private void parsePlane(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 7) throw new ParsingException("Erreur 'plane'");
        Point p = parsePoint(tokens, 1);
        Vector n = parseVector(tokens, 4);

        // CORRECTION JALON 5 : Ajout de currentShininess (assumant que Plane a aussi été mis à jour)
        scene.addShape(new Plane(p, n, currentDiffuse, currentSpecular, currentShininess));
    }

    // --- Helpers ---
    private Point parsePoint(String[] tokens, int idx) {
        return new Point(Double.parseDouble(tokens[idx]), Double.parseDouble(tokens[idx+1]), Double.parseDouble(tokens[idx+2]));
    }
    private Vector parseVector(String[] tokens, int idx) {
        return new Vector(Double.parseDouble(tokens[idx]), Double.parseDouble(tokens[idx+1]), Double.parseDouble(tokens[idx+2]));
    }
    private Color parseColor(String[] tokens, int idx) {
        return new Color(Double.parseDouble(tokens[idx]), Double.parseDouble(tokens[idx+1]), Double.parseDouble(tokens[idx+2]));
    }
    private void checkDiffuseSum(Color ambient, Color diffuse) throws ParsingException {
        if (ambient.r() + diffuse.r() > 1.0 || ambient.g() + diffuse.g() > 1.0 || ambient.b() + diffuse.b() > 1.0)
            throw new ParsingException("Validation échouée: ambient + diffuse > 1.");
    }
    private void validateLightIntensity(Color newColor) throws ParsingException {
        double r = totalLightColor.r() + newColor.r();
        double g = totalLightColor.g() + newColor.g();
        double b = totalLightColor.b() + newColor.b();
        if (r > 1.0 || g > 1.0 || b > 1.0) throw new ParsingException("Intensité lumière totale > 1.");
        totalLightColor = new Color(r, g, b);
    }
}
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

/**
 * Analyseur (Parser) pour les fichiers de description de scène
 * Crée et remplit un objet Scene à partir d'un fichier
 */
public class SceneFileParser {

    // État courant du parser
    private Color currentDiffuse = new Color(0, 0, 0);
    private Color currentSpecular = new Color(0, 0, 0);
    private int maxVerts = 0;
    private List<Point> vertices = new ArrayList<>();
    private Color totalLightColor = new Color(0, 0, 0);

    // Références obligatoires
    private boolean sizeSet = false;
    private boolean cameraSet = false;

    /**
     * Méthode principale pour parser le fichier de scène.
     * @param filePath Chemin vers le fichier .txt de la scène
     * @return Un objet Scene complet
     * @throws IOException Si le fichier ne peut être lu
     * @throws ParsingException Si le contenu du fichier est invalide
     */
    public Scene parse(String filePath) throws IOException, ParsingException {
        Scene scene = new Scene();

        // Parcours ligne par ligne
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Nettoyer la ligne

                // Ignorer les commentaires et lignes vides
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Séparer la commande et les arguments
                String[] tokens = line.split("\\s+");
                String command = tokens[0];

                // Gérer les différents cas
                switch (command) {
                    // Commandes principales
                    case "size":
                        parseSize(tokens, scene);
                        break;
                    case "output":
                        parseOutput(tokens, scene);
                        break;
                    case "camera":
                        parseCamera(tokens, scene);
                        break;

                    // Commandes de matériaux (couleurs)
                    case "ambient":
                        parseAmbient(tokens, scene);
                        break;
                    case "diffuse":
                        parseDiffuse(tokens, scene);
                        break;
                    case "specular":
                        parseSpecular(tokens);
                        break;

                    // Commandes de lumières
                    case "directional":
                        parseDirectional(tokens, scene);
                        break;
                    case "point":
                        parsePoint(tokens, scene);
                        break;

                    // Commandes de géométrie (Triangles)
                    case "maxverts":
                        parseMaxVerts(tokens);
                        break;
                    case "vertex":
                        parseVertex(tokens);
                        break;

                    // Commandes d'objets (Formes)
                    case "sphere":
                        parseSphere(tokens, scene);
                        break;
                    case "tri":
                        parseTri(tokens, scene);
                        break;
                    case "plane":
                        parsePlane(tokens, scene);
                        break;

                    default:
                        // Ignorer les commandes inconnues
                        break;
                }
            }
        }

        // Vérifications finales
        if (!sizeSet) throw new ParsingException("Erreur: 'size' (taille) n'a pas été défini.");
        if (!cameraSet) throw new ParsingException("Erreur: 'camera' n'a pas été défini.");

        return scene;
    }

    /**
     * Analyse la commande 'size'
     */
    private void parseSize(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 3) throw new ParsingException("Erreur 'size': 2 arguments attendus.");
        try {
            scene.setWidth(Integer.parseInt(tokens[1]));
            scene.setHeight(Integer.parseInt(tokens[2]));
            sizeSet = true;
        } catch (NumberFormatException e) {
            throw new ParsingException("Erreur 'size': arguments non valides.");
        }
    }

    /**
     * Analyse la commande 'output'
     */
    private void parseOutput(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 2) throw new ParsingException("Erreur 'output': 1 argument attendu.");
        scene.setOutput(tokens[1]);
    }

    /**
     * Analyse la commande 'camera'
     */
    private void parseCamera(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 11) throw new ParsingException("Erreur 'camera': 10 arguments attendus.");
        try {
            Point lookFrom = parsePoint(tokens, 1);
            Point lookAt = parsePoint(tokens, 4);
            Vector up = parseVector(tokens, 7);
            double fov = Double.parseDouble(tokens[10]);

            scene.setCamera(new Camera(lookFrom, lookAt, up, fov));
            cameraSet = true;
        } catch (NumberFormatException e) {
            throw new ParsingException("Erreur 'camera': arguments non valides.");
        }
    }

    /**
     * Analyse la commande 'ambient'
     */
    private void parseAmbient(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'ambient': 3 arguments attendus.");
        Color ambient = parseColor(tokens, 1);
        scene.setAmbient(ambient);
        // Re-valider la couleur diffuse si elle a été définie avant
        checkDiffuseSum(scene.getAmbient(), this.currentDiffuse);
    }

    /**
     * Analyse la commande 'diffuse'
     */
    private void parseDiffuse(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'diffuse': 3 arguments attendus.");
        this.currentDiffuse = parseColor(tokens, 1);
        // Valider la somme ambient + diffuse
        checkDiffuseSum(scene.getAmbient(), this.currentDiffuse);
    }

    /**
     * Analyse la commande 'specular'
     */
    private void parseSpecular(String[] tokens) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'specular': 3 arguments attendus.");
        this.currentSpecular = parseColor(tokens, 1);
    }

    /**
     * Analyse la commande 'directional'
     */
    private void parseDirectional(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 7) throw new ParsingException("Erreur 'directional': 6 arguments attendus.");
        Vector direction = parseVector(tokens, 1);
        Color color = parseColor(tokens, 4);
        checkLightSum(color);
        scene.addLight(new DirectionalLight(direction, color));
    }

    /**
     * Analyse la commande 'point' (lumière)
     */
    private void parsePoint(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 7) throw new ParsingException("Erreur 'point': 6 arguments attendus.");
        Point position = parsePoint(tokens, 1);
        Color color = parseColor(tokens, 4);
        checkLightSum(color);
        scene.addLight(new PointLight(position, color));
    }

    /**
     * Analyse la commande 'maxverts'
     */
    private void parseMaxVerts(String[] tokens) throws ParsingException {
        if (tokens.length != 2) throw new ParsingException("Erreur 'maxverts': 1 argument attendu.");
        try {
            this.maxVerts = Integer.parseInt(tokens[1]);
            this.vertices = new ArrayList<>(this.maxVerts);
        } catch (NumberFormatException e) {
            throw new ParsingException("Erreur 'maxverts': argument non valide.");
        }
    }

    /**
     * Analyse la commande 'vertex'
     */
    private void parseVertex(String[] tokens) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'vertex': 3 arguments attendus.");
        if (this.vertices.size() >= this.maxVerts) {
            throw new ParsingException("Erreur: Plus de 'vertex' déclarés que 'maxverts'.");
        }
        this.vertices.add(parsePoint(tokens, 1));
    }

    /**
     * Analyse la commande 'sphere'
     */
    private void parseSphere(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 5) throw new ParsingException("Erreur 'sphere': 4 arguments attendus.");
        try {
            Point center = parsePoint(tokens, 1);
            double radius = Double.parseDouble(tokens[4]);
            // Applique les couleurs courantes
            scene.addShape(new Sphere(center, radius, this.currentDiffuse, this.currentSpecular));
        } catch (NumberFormatException e) {
            throw new ParsingException("Erreur 'sphere': arguments non valides.");
        }
    }

    /**
     * Analyse la commande 'tri' (triangle)
     */
    private void parseTri(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 4) throw new ParsingException("Erreur 'tri': 3 arguments attendus.");
        try {
            int iA = Integer.parseInt(tokens[1]);
            int iB = Integer.parseInt(tokens[2]);
            int iC = Integer.parseInt(tokens[3]);

            // Valider les indices
            if (iA >= maxVerts || iB >= maxVerts || iC >= maxVerts ||
                    iA < 0 || iB < 0 || iC < 0) {
                throw new ParsingException("Erreur 'tri': indice de vertex invalide (dépasse maxverts).");
            }

            // Récupérer les points
            Point a = this.vertices.get(iA);
            Point b = this.vertices.get(iB);
            Point c = this.vertices.get(iC);

            scene.addShape(new Triangle(a, b, c, this.currentDiffuse, this.currentSpecular));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new ParsingException("Erreur 'tri': arguments non valides ou 'vertex' non défini.");
        }
    }

    /**
     * Analyse la commande 'plane'
     */
    private void parsePlane(String[] tokens, Scene scene) throws ParsingException {
        if (tokens.length != 7) throw new ParsingException("Erreur 'plane': 6 arguments attendus.");
        Point point = parsePoint(tokens, 1);
        Vector normal = parseVector(tokens, 4);
        scene.addShape(new Plane(point, normal, this.currentDiffuse, this.currentSpecular));
    }

    // --- Méthodes utilitaires de parsing ---

    private Point parsePoint(String[] tokens, int startIndex) throws NumberFormatException {
        double x = Double.parseDouble(tokens[startIndex]);
        double y = Double.parseDouble(tokens[startIndex + 1]);
        double z = Double.parseDouble(tokens[startIndex + 2]);
        return new Point(x, y, z);
    }

    private Vector parseVector(String[] tokens, int startIndex) throws NumberFormatException {
        double x = Double.parseDouble(tokens[startIndex]);
        double y = Double.parseDouble(tokens[startIndex + 1]);
        double z = Double.parseDouble(tokens[startIndex + 2]);
        return new Vector(x, y, z);
    }

    private Color parseColor(String[] tokens, int startIndex) throws NumberFormatException {
        double r = Double.parseDouble(tokens[startIndex]);
        double g = Double.parseDouble(tokens[startIndex + 1]);
        double b = Double.parseDouble(tokens[startIndex + 2]);
        return new Color(r, g, b);
    }

    // --- Méthodes de validation ---

    /**
     * Vérifie que (ambient + diffuse) ← 1 pour chaque composante
     */
    private void checkDiffuseSum(Color ambient, Color diffuse) throws ParsingException {
        if (ambient.r() + diffuse.r() > 1.0 ||
                ambient.g() + diffuse.g() > 1.0 ||
                ambient.b() + diffuse.b() > 1.0) {
            throw new ParsingException("Validation échouée: (ambient + diffuse) dépasse 1.0.");
        }
    }

    /**
     * Vérifie que la somme totale des lumières ← 1 pour chaque composante
     */
    private void checkLightSum(Color newLightColor) throws ParsingException {
        this.totalLightColor = this.totalLightColor.add(newLightColor);
        if (this.totalLightColor.r() > 1.0 ||
                this.totalLightColor.g() > 1.0 ||
                this.totalLightColor.b() > 1.0) {
            throw new ParsingException("Validation échouée: La somme des couleurs de lumière dépasse 1.0.");
        }
    }
}
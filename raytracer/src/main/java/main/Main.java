package main;

import parsing.SceneFileParser;
import parsing.ParsingException;
import scene.Scene;
import imaging.Renderer;
import imaging.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Programme principal du lanceur de rayons.
 */
public class Main {
    /**
     * Programme principal du Raytracer.
     * @param args Chemin du fichier de scène
     */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Default scene path relative to 'src/main/resources'
    private static final String DEFAULT_SCENE_RESOURCE = "scenes/final_avec_bonus.scene";

    public static void main(String[] args) {
        String sceneFilePath;

        // 1. Determine the scene file path
        if (args.length > 0) {
            // Case 1: Path provided as argument
            sceneFilePath = args[0];
        } else {
            // Case 2: No argument, use default resource
            logger.info("Aucun argument fourni. Tentative de chargement de la ressource par défaut : {}", DEFAULT_SCENE_RESOURCE);

            // Try to locate the file in the classpath resources
            URL resourceUrl = Main.class.getClassLoader().getResource(DEFAULT_SCENE_RESOURCE);

            if (resourceUrl != null) {
                // If found in resources, convert URL to file path
                try {
                    // toURI() handles spaces and special characters correctly
                    sceneFilePath = Paths.get(resourceUrl.toURI()).toString();
                } catch (Exception e) {
                    logger.error("Erreur lors de la conversion de l'URL de ressource en chemin fichier.", e);
                    return;
                }
            } else {
                // Fallback: Check if it exists as a relative file path (e.g., src/main/resources/...)
                // This is useful if the resources are not yet compiled/copied to target
                String relativePath = "src/main/resources/" + DEFAULT_SCENE_RESOURCE;
                File file = new File(relativePath);
                if (file.exists()) {
                    sceneFilePath = relativePath;
                } else {
                    logger.error("Impossible de trouver le fichier de scène par défaut : {}", DEFAULT_SCENE_RESOURCE);
                    logger.error("Veuillez fournir un chemin valide en argument.");
                    return;
                }
            }
        }

        try {
            // 2. Load the scene file
            logger.info("Chargement de la scène : {}", sceneFilePath);
            SceneFileParser parser = new SceneFileParser();
            Scene scene = parser.parse(sceneFilePath);

            // 3. Create the Renderer
            Renderer renderer = new Renderer();

            // 4. Render the image
            logger.info("Rendu de l'image ( {} x {})..." , scene.getWidth(), scene.getHeight());
            BufferedImage renderedImage = renderer.render(scene);

            // 5. Save the image
            ImageWriter writer = new ImageWriter();
            String outputFileName = scene.getOutput();
            writer.saveImage(renderedImage, outputFileName);

            logger.info("Image sauvegardée avec succès : {}" , outputFileName);

        } catch (ParsingException e) {
            logger.error("Erreur lors de l'analyse du fichier scène :");
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error("Erreur d'entrée/sortie (IO) :");
            logger.error(e.getMessage());
        }
    }
}

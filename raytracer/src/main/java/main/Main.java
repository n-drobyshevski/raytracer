package main;

import parsing.SceneFileParser;
import parsing.ParsingException;
import scene.Scene;
import imaging.Renderer;
import imaging.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Programme principal du lanceur de rayons.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 1. Vérifier l'argument (fichier de scène)
        if (args.length < 1) {
            logger.error("Erreur : Vous devez fournir un fichier de scène en argument.");
            logger.error("Usage : java main.Main <scene.txt>");
            return;
        }

        String sceneFilePath = args[0];

        try {
            // 2. Charger le fichier de scène
            logger.info("Chargement de la scène : {}", sceneFilePath);
            SceneFileParser parser = new SceneFileParser();
            Scene scene = parser.parse(sceneFilePath);

            // 3. Créer le Renderer
            Renderer renderer = new Renderer();

            // 4. Lancer le rendu
            logger.info("Rendu de l'image ( {} x {})..." , scene.getWidth(), scene.getHeight());
            BufferedImage renderedImage = renderer.render(scene);

            // 5. Sauvegarder l'image
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
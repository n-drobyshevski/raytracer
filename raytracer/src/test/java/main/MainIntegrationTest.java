package main;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test d'intégration de bout en bout.
 * Exécute la méthode main.Main.main() et vérifie que le fichier
 * image de sortie est créé.
 */
public class MainIntegrationTest {

    private final String OUTPUT_FILENAME = "test-output.png";
    private String sceneFilePath;

    @BeforeEach
    void setUp() throws Exception {
        // Récupère le chemin complet vers le fichier de scène de test
        URL sceneUrl = getClass().getClassLoader().getResource("test0.scene");
        assertNotNull(sceneUrl, "Impossible de trouver test0.scene");

        sceneFilePath = Paths.get(sceneUrl.toURI()).toString();

        // S'assurer que l'ancien fichier de sortie n'existe pas
        Files.deleteIfExists(Paths.get(OUTPUT_FILENAME));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Nettoyer le fichier de sortie après le test
        Files.deleteIfExists(Paths.get(OUTPUT_FILENAME));
    }

    @Test
    void testMain_ApplicationRuns_CreatesOutputFile() {
        // Arrange
        String[] args = { sceneFilePath };
        Path outputPath = Paths.get(OUTPUT_FILENAME);

        // Act
        // Exécute la méthode principale de votre application
        assertDoesNotThrow(() -> Main.main(args), "L'exécution de main.Main.main() a levé une exception.");

        // Assert
        // Vérifie que le fichier 'test-output.png' (défini dans la scène) a été créé
        assertTrue(Files.exists(outputPath), "Le fichier de sortie 'test-output.png' n'a pas été créé.");
        assertTrue(Files.isRegularFile(outputPath), "La sortie n'est pas un fichier valide.");
    }
}
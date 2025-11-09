package parsing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import scene.Scene;
import geometry.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class SceneFileParserTest {

    private SceneFileParser parser;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new SceneFileParser();
    }

    @Test
    void testParseBasicValidScene() throws IOException, ParsingException {
        String sceneContent = String.join("\n",
            "size 800 600",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        Scene scene = parser.parse(scenePath.toString());

        assertEquals(800, scene.getWidth());
        assertEquals(600, scene.getHeight());
        assertEquals("test.png", scene.getOutput());
    }

    @Test
    void testParseWithComments() throws IOException, ParsingException {
        String sceneContent = String.join("\n",
            "# This is a comment",
            "size 800 600",
            "# Another comment",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        Scene scene = parser.parse(scenePath.toString());

        assertEquals(800, scene.getWidth());
        assertEquals(600, scene.getHeight());
    }

    @Test
    void testParseSphereAndLights() throws IOException, ParsingException {
        String sceneContent = String.join("\n",
            "size 800 600",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "ambient 0.1 0.1 0.1",
            "diffuse 0.5 0.5 0.5",
            "specular 0.3 0.3 0.3",
            "sphere 0 0 0 1.0",
            "point 2 3 4 0.3 0.3 0.3",
            "directional 1 1 1 0.2 0.2 0.2",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        Scene scene = parser.parse(scenePath.toString());

        assertNotNull(scene);
        assertEquals(1, scene.getShapes().size());
        assertInstanceOf(Sphere.class, scene.getShapes().get(0));
        assertEquals(2, scene.getLights().size());
    }

    @Test
    void testParseTriangleWithVertices() throws IOException, ParsingException {
        String sceneContent = String.join("\n",
            "size 800 600",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "maxverts 3",
            "vertex 0 0 0",
            "vertex 1 0 0",
            "vertex 0 1 0",
            "diffuse 0.5 0.5 0.5",
            "tri 0 1 2",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        Scene scene = parser.parse(scenePath.toString());

        assertNotNull(scene);
        assertEquals(1, scene.getShapes().size());
        assertInstanceOf(Triangle.class, scene.getShapes().get(0));
    }

    @Test
    void testParsePlane() throws IOException, ParsingException {
        String sceneContent = String.join("\n",
            "size 800 600",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "diffuse 0.5 0.5 0.5",
            "plane 0 0 0 0 1 0",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        Scene scene = parser.parse(scenePath.toString());

        assertNotNull(scene);
        assertEquals(1, scene.getShapes().size());
        assertInstanceOf(Plane.class, scene.getShapes().get(0));
    }

    @Test
    void testMissingSizeCommand() {
        String sceneContent = String.join("\n",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        assertThrows(ParsingException.class, () -> parser.parse(scenePath.toString()));
    }

    @Test
    void testMissingCameraCommand() {
        String sceneContent = String.join("\n",
            "size 800 600",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        assertThrows(ParsingException.class, () -> parser.parse(scenePath.toString()));
    }

    @Test
    void testInvalidLightSum() {
        String sceneContent = String.join("\n",
            "size 800 600",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "point 0 0 0 0.6 0.6 0.6",
            "point 0 0 0 0.6 0.6 0.6",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        assertThrows(ParsingException.class, () -> parser.parse(scenePath.toString()));
    }

    @Test
    void testInvalidDiffuseAmbientSum() {
        String sceneContent = String.join("\n",
            "size 800 600",
            "camera 0 0 5 0 0 0 0 1 0 45",
            "ambient 0.6 0.6 0.6",
            "diffuse 0.6 0.6 0.6",
            "output test.png"
        );

        Path scenePath = createTempSceneFile(sceneContent);
        assertThrows(ParsingException.class, () -> parser.parse(scenePath.toString()));
    }

    private Path createTempSceneFile(String content) {
        try {
            Path filePath = tempDir.resolve("scene.txt");
            Files.writeString(filePath, content);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp scene file", e);
        }
    }
}



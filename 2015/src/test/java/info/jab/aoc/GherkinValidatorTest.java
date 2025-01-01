package info.jab.aoc;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.GherkinDocument;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GherkinValidatorTest {

    @Disabled
    @Test
    void validateAllGherkinFiles() throws IOException {
        // Encuentra todos los archivos .feature recursivamente
        try (Stream<Path> paths = Files.walk(Paths.get("src/main/gherkin"))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".feature"))
                 .forEach(this::validateGherkinFile);
        }
    }

    private void validateGherkinFile(Path path) {
        try {
            GherkinParser parser = GherkinParser.builder().build();
            
            // Validar el documento - si no hay excepciones, el archivo es válido
            Optional<GherkinDocument> doc = parser.parse(path)
                .filter(envelope -> envelope.getGherkinDocument() != null)
                .map(Envelope::getGherkinDocument)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró GherkinDocument en " + path));
            
            // Si llegamos aquí, el documento es válido
            System.out.println("Archivo válido: " + path);
            
        } catch (Exception e) {
            throw new AssertionError(
                "Error validando archivo: " + path + "\n" + e.getMessage(), e);
        }
    }
}

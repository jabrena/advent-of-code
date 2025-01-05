package info.jab.aoc;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.Source;
import io.cucumber.messages.types.SourceMediaType;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GherkinValidatorTest {

    private static final String GHERKIN_DIR = "src/test/gherkin";

    @Test
    void validateAllGherkinFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(GHERKIN_DIR))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".feature"))
                 .map(Path::toString)
                 .peek(System.out::println)
                 .forEach(GherkinUtils::getGherkinDocument);
        }
    }
}

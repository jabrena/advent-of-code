package info.jab.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.Source;
import io.cucumber.messages.types.SourceMediaType;

public class GherkinUtils {
    
    public static GherkinDocument getGherkinDocument(String fileName) {
        try {
            var feature = Files.readString(Paths.get(fileName));
            var envelope = Envelope.of(new Source("minimal.feature", feature, SourceMediaType.TEXT_X_CUCUMBER_GHERKIN_PLAIN));
            
            return GherkinParser.builder()
                .includeSource(false)
                .includePickles(false)
                .build()
                .parse(envelope)
                .findFirst().get()
                .getGherkinDocument().get();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

}

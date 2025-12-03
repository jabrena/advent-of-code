# AOC 2025

```bash
# Compile first
./mvnw clean test -pl 2025

# JavaFX requires module path configuration - use Maven exec plugin:
# Day 1
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day1.Day1Visualization" -Dexec.classpathScope=test

# Day 3
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day3.Day3Visualization" -Dexec.classpathScope=test
```

# Advent of code collection

Advent of Code is an annual set of Christmas-themed computer programming challenges that follow an Advent calendar.

[![Java CI](https://github.com/jabrena/advent-of-code-collection/actions/workflows/maven.yml/badge.svg)](https://github.com/jabrena/advent-of-code-collection/actions/workflows/maven.yml)

## Cloud IDEs

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/jabrena/advent-of-code-collection)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/jabrena/advent-of-code-collection)

## Summary

- [2024](./2024/README.md)

## How to build in local?

```bash
java -version
./mvnw clean verify
./mvnw -pl 2024 clean compile -am
./mvnw -pl 2024 clean verify -am
./mvnw -pl 2024 clean test -Dtest=Day11Test
./mvnw -pl 2024 clean test -Dtest=AlternativesBenchmarkTest
./mvnw -pl 2024 clean dependency:tree
./mvnw -pl 2024 clean verify surefire-report:report -DshowSuccess=false
jwebserver -p 9000 -d "$(pwd)/2024/target/reports"

./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates
```

## References

### Libraries

- https://github.com/z669016/aoc
- https://github.com/z669016/algorithms
- https://javadoc.io/doc/com.google.guava/guava/latest/index.html
- https://github.com/dpaukov/combinatoricslib3
- https://github.com/sim642/adventofcode/tree/master/src/main/scala/eu/sim642/adventofcodelib
- https://github.com/bertjan/advent-of-code-2024/blob/main/src/main/java/utils/Matrix.java
- https://github.com/p-kovacs/advent-of-code-2024/tree/master/src/main/java/com/github/pkovacs/util
- https://github.com/hlipka/AdventOfCode/tree/main/java/src/util/de/hendriklipka/aoc
- https://github.com/SimonBaars/AdventOfCode-Java/blob/master/src/main/java/com/sbaars/adventofcode/common/Direction.java
- https://github.com/SimonBaars/AdventOfCode-Java/blob/master/src/main/java/com/sbaars/adventofcode/common/location/Loc.java
- https://github.com/SimonBaars/AdventOfCode-Java/blob/master/src/main/java/com/sbaars/adventofcode/util/AoCUtils.java
- https://github.com/zodac/advent-of-code/tree/master/common-utils

### Examples

- https://github.com/z669016/adventofcode-2022 (Java)
- https://github.com/forax/advent-of-code-2023 (Java)
- https://github.com/forax/advent-of-code-2024 (Java)
- https://github.com/nipafx/advent-of-code-2023 (Java)
- https://github.com/bertjan/advent-of-code-2024 (Java)
- https://github.com/nilederg/AOC-2024 (Java)
- https://github.com/SimonBaars/AdventOfCode-Java (Java)
- https://github.com/p-kovacs/advent-of-code-2024 (Java)
- https://github.com/michaelmountain-8451/advent-of-code (Java)
- https://github.com/juan-medina/adventofcode2024 (C#)
- https://github.com/TheJare/aoc2024 (Ruby)
- https://github.com/jmgimeno/aoc2024/tree/master (Scala)
- https://github.com/neutropolis/aoc (APL)

### Searches

- https://github.com/search?q=advent+of+code++language%3AJava&type=repositories&s=updated&o=desc
- https://github.com/search?q=aoc+2024+language%3AJava&type=repositories&s=updated&o=desc
- https://github.com/search?q=advent+of+code+2024++language%3AJava&type=repositories&l=Java&s=updated&o=desc

### Others


- https://adventofcode.com/
- https://github.com/jasonmuzzy/aoc-copilot
- https://www.reddit.com/r/adventofcode/
- https://www.reddit.com/r/adventofcode/?f=flair_name%3A%22Funny%22
- https://www.reddit.com/r/adventofcode/search/?q=flair_name%3A%22SOLUTION%20MEGATHREAD%22&restrict_sr=1
- https://openjdk.org/projects/code-tools/jol/
- https://jmh.morethan.io/

## Acknowledgements

A heartfelt thanks to ChatGPT and Gemini for the insightful early morning (6:00 AM)
discussions on design thinking and code refactoring, with a focus on functional approaches.
I'm also deeply grateful to my friend [Juan Antonio Medina](https://www.github.com/juan-medina)
for the continuous and inspiring conversations throughout the day,
and to [Rene van Putten](https://github.com/z669016/) for their invaluable inspiration.

**Powered by Java 24 EA**

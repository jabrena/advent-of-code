# Advent of code collection

Advent of Code is an annual set of Christmas-themed computer programming challenges that follow an Advent calendar.

## How to build in local?

```bash
./mvnw clean verify
./mvnw clean verify -pl 2023 -am
./mvnw clean verify -pl 2024 -am
./mvnw dependency:tree -pl 2024 -am
./mvnw verify -pl 2024 -am surefire-report:report -DshowSuccess=false
jwebserver -p 9000 -d "$(pwd)/2024/target/reports"

./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates
```

## References

- https://adventofcode.com/
- https://adventofcode.com/2024
- https://adventofcode.com/2023
- https://adventofcode.com/2022
- https://adventofcode.com/2021
- https://adventofcode.com/2020
- https://adventofcode.com/2019
- https://adventofcode.com/2018
- https://adventofcode.com/2017
- https://adventofcode.com/2016
- https://adventofcode.com/2015
- https://www.reddit.com/r/adventofcode/
- https://www.reddit.com/r/adventofcode/?f=flair_name%3A%22Funny%22
- https://www.reddit.com/r/adventofcode/search/?q=flair_name%3A%22SOLUTION%20MEGATHREAD%22&restrict_sr=1
- https://github.com/z669016/aoc
- https://github.com/z669016/algorithms
- https://github.com/sim642/adventofcode/tree/master/src/main/scala/eu/sim642/adventofcodelib
- https://github.com/bertjan/advent-of-code-2024/blob/main/src/main/java/utils/Matrix.java
- https://github.com/p-kovacs/advent-of-code-2024/tree/master/src/main/java/com/github/pkovacs/util
- https://github.com/dpaukov/combinatoricslib3
- https://github.com/z669016/adventofcode-2022
- https://github.com/forax/advent-of-code-2023
- https://github.com/nipafx/advent-of-code-2023

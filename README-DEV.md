# Essential Maven Goals:

```bash
# Analyze dependencies
./mvnw dependency:tree
./mvnw dependency:analyze
./mvnw dependency:resolve

./mvnw clean validate -U
./mvnw buildplan:list-plugin
./mvnw buildplan:list-phase
./mvnw help:all-profiles
./mvnw help:active-profiles
./mvnw license:third-party-report

# Clean the project
./mvnw clean

# Clean and package in one command
./mvnw clean package

./mvnw clean test

# Run integration tests
./mvnw clean verify

# Check for dependency updates
./mvnw versions:display-property-updates
./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates

# Generate project reports
# Generate aggregated surefire report (requires tests to be run first)
./mvnw clean test surefire-report:report-only
# Or generate full site with aggregated reports
./mvnw clean test site

# Generate Allure reports
# Run tests first (this generates both Surefire XML and Allure results)
./mvnw clean test
# Generate Allure report from test results (use full plugin coordinates)
./mvnw io.qameta.allure:allure-maven:report
# Or combine both commands
./mvnw clean test io.qameta.allure:allure-maven:report
# Serve Allure report locally (opens browser automatically)
./mvnw io.qameta.allure:allure-maven:serve
# Allure report location: target/allure-report/allure-maven.html
# Allure results location: target/allure-results/

# Generate both Surefire and Allure reports
./mvnw clean test surefire-report:report-only io.qameta.allure:allure-maven:report

# View reports
jwebserver -p 8005 -d "$(pwd)/target/"
# Surefire report: http://127.0.0.1:8005/site/surefire-report.html
# Allure report: http://127.0.0.1:8005/allure-report/allure-maven.html

```

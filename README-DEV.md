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

# Remove unused imports
./mvnw clean rewrite:run

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
# View reports
jwebserver -p 8005 -d "$(pwd)/target/site"
# Aggregated report will be at: http://127.0.0.1:8005/target/reports/surefire-report.html

# Generate aggregated Allure report
# 1. Run tests (automatically aggregates results)
./mvnw clean test

# 2. Generate the report
./mvnw allure:report

# Serve Allure report
jwebserver -p 8005 -d "$(pwd)/target/site/allure-maven-plugin"
# Aggregated report will be at: http://127.0.0.1:8005/index.html
```


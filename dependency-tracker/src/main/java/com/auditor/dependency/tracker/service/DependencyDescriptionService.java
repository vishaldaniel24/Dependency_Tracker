package com.auditor.dependency.tracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DependencyDescriptionService {

    private static final Map<String, String> DESCRIPTIONS = new HashMap<>();

    static {

        // ── Spring Boot Core ────────────────────────────────
        DESCRIPTIONS.put("spring-boot",
            "Core Spring Boot framework that auto-configures your application and manages the application lifecycle.");
        DESCRIPTIONS.put("spring-boot-starter",
            "Base Spring Boot starter that includes auto-configuration support, logging, and YAML support.");
        DESCRIPTIONS.put("spring-boot-autoconfigure",
            "Provides Spring Boot's auto-configuration mechanism that wires beans automatically based on classpath contents.");
        DESCRIPTIONS.put("spring-boot-starter-parent",
            "Parent POM that provides dependency management and plugin configuration for Spring Boot projects.");

        // ── Spring Web ──────────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-web",
            "Starter for building web applications using Spring MVC. Includes Tomcat as the default embedded server.");
        DESCRIPTIONS.put("spring-boot-starter-webmvc",
            "Starter for building Spring MVC web applications with an embedded servlet container.");
        DESCRIPTIONS.put("spring-webmvc",
            "Spring's Model-View-Controller framework for building flexible and loosely coupled web applications.");
        DESCRIPTIONS.put("spring-web",
            "Core Spring web module providing HTTP integration, web utilities, and REST client support.");

        // ── Spring Data & JPA ───────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-data-jpa",
            "Starter for using Spring Data JPA with Hibernate to persist data in relational databases.");
        DESCRIPTIONS.put("spring-data-jpa",
            "Spring Data module that simplifies JPA-based database access using repository interfaces.");
        DESCRIPTIONS.put("spring-data-commons",
            "Shared infrastructure for Spring Data modules including repository abstraction and query support.");
        DESCRIPTIONS.put("hibernate-core",
            "The core Hibernate ORM framework for mapping Java objects to database tables.");
        DESCRIPTIONS.put("hibernate-commons-annotations",
            "Common annotations used by Hibernate ORM for entity mapping and configuration.");
        DESCRIPTIONS.put("jakarta-persistence-api",
            "Jakarta Persistence API (JPA) specification interfaces for ORM-based data access.");

        // ── Thymeleaf ───────────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-thymeleaf",
            "Starter for building MVC web applications using Thymeleaf as the server-side HTML template engine.");
        DESCRIPTIONS.put("thymeleaf",
            "A modern server-side Java template engine for HTML, XML, JavaScript, CSS and plain text.");
        DESCRIPTIONS.put("thymeleaf-spring6",
            "Thymeleaf integration module for Spring Framework 6, enabling Spring MVC dialect and expression support.");

        // ── Database ────────────────────────────────────────
        DESCRIPTIONS.put("h2",
            "A fast in-memory relational database written in Java. Ideal for development and testing environments.");
        DESCRIPTIONS.put("mysql-connector-j",
            "Official MySQL JDBC driver that allows Java applications to connect to MySQL databases.");
        DESCRIPTIONS.put("postgresql",
            "Official PostgreSQL JDBC driver for connecting Java applications to PostgreSQL databases.");
        DESCRIPTIONS.put("HikariCP",
            "A high-performance JDBC connection pool. Spring Boot uses it as the default connection pool.");

        // ── Lombok ──────────────────────────────────────────
        DESCRIPTIONS.put("lombok",
            "Reduces Java boilerplate by auto-generating getters, setters, constructors, and builders via annotations.");

        // ── Spring Security ─────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-security",
            "Starter for adding Spring Security authentication and authorization to your application.");
        DESCRIPTIONS.put("spring-security-core",
            "Core Spring Security module providing authentication, authorization, and access-control features.");
        DESCRIPTIONS.put("spring-security-web",
            "Spring Security module for web-based security including request filtering and session management.");
        DESCRIPTIONS.put("spring-security-config",
            "Provides Spring Security's XML and Java configuration support for defining security rules.");

        // ── Jackson (JSON) ──────────────────────────────────
        DESCRIPTIONS.put("jackson-databind",
            "Core Jackson library for serializing Java objects to JSON and deserializing JSON back to Java objects.");
        DESCRIPTIONS.put("jackson-core",
            "Low-level JSON streaming API used by Jackson for reading and writing JSON efficiently.");
        DESCRIPTIONS.put("jackson-annotations",
            "Jackson annotations like @JsonProperty and @JsonIgnore for controlling JSON serialization behavior.");
        DESCRIPTIONS.put("jackson-datatype-jsr310",
            "Jackson module that adds support for Java 8 Date and Time API (java.time) serialization.");

        // ── Spring Core ─────────────────────────────────────
        DESCRIPTIONS.put("spring-core",
            "Fundamental Spring Framework module providing core utilities, IoC container, and resource abstraction.");
        DESCRIPTIONS.put("spring-context",
            "Spring's application context module that provides the IoC container and dependency injection support.");
        DESCRIPTIONS.put("spring-beans",
            "Spring module responsible for bean creation, wiring, and lifecycle management within the IoC container.");
        DESCRIPTIONS.put("spring-aop",
            "Spring's Aspect-Oriented Programming module for defining cross-cutting concerns like logging and transactions.");
        DESCRIPTIONS.put("spring-expression",
            "Spring Expression Language (SpEL) module for querying and manipulating objects at runtime.");
        DESCRIPTIONS.put("spring-tx",
            "Spring transaction management module supporting both programmatic and declarative transactions.");
        DESCRIPTIONS.put("spring-orm",
            "Spring integration layer for JPA, Hibernate and other ORM frameworks.");
        DESCRIPTIONS.put("spring-jdbc",
            "Spring's JDBC abstraction layer that simplifies database interaction and error handling.");

        // ── Logging ─────────────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-logging",
            "Default logging starter for Spring Boot using Logback as the logging framework.");
        DESCRIPTIONS.put("logback-classic",
            "The classic Logback logging module, the default logging implementation used by Spring Boot.");
        DESCRIPTIONS.put("logback-core",
            "Core Logback logging engine that provides the foundation for logback-classic and logback-access.");
        DESCRIPTIONS.put("slf4j-api",
            "Simple Logging Facade for Java — a common logging API that decouples code from specific logging frameworks.");
        DESCRIPTIONS.put("log4j-to-slf4j",
            "Bridges Log4j 2 API calls to SLF4J so libraries using Log4j still work in a Logback environment.");
        DESCRIPTIONS.put("jul-to-slf4j",
            "Bridges Java Util Logging (JUL) to SLF4J so all logging flows through one unified pipeline.");

        // ── Validation ──────────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-validation",
            "Starter for using Java Bean Validation with Hibernate Validator as the validation engine.");
        DESCRIPTIONS.put("hibernate-validator",
            "Reference implementation of Jakarta Bean Validation used to validate Java objects via annotations.");
        DESCRIPTIONS.put("jakarta-validation-api",
            "Jakarta Bean Validation API specification for declaring and executing validation constraints.");

        // ── Testing ─────────────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-test",
            "Starter for testing Spring Boot applications with JUnit 5, Mockito, AssertJ, and MockMvc.");
        DESCRIPTIONS.put("junit-jupiter",
            "JUnit 5 Jupiter API for writing modern unit tests in Java with annotations like @Test and @BeforeEach.");
        DESCRIPTIONS.put("junit-jupiter-api",
            "JUnit 5 API module for writing tests and extensions.");
        DESCRIPTIONS.put("mockito-core",
            "Mocking framework for Java that lets you create mock objects and define their behavior in tests.");
        DESCRIPTIONS.put("mockito-junit-jupiter",
            "Mockito integration for JUnit 5 enabling annotation-based mock injection in test classes.");
        DESCRIPTIONS.put("assertj-core",
            "Fluent assertion library for Java tests providing readable and expressive assertions.");

        // ── Embedded Server ─────────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-tomcat",
            "Starter that includes embedded Apache Tomcat as the default servlet container for Spring Boot apps.");
        DESCRIPTIONS.put("tomcat-embed-core",
            "Core embedded Apache Tomcat library that runs a web server inside your Spring Boot application.");
        DESCRIPTIONS.put("tomcat-embed-websocket",
            "Embedded Tomcat module adding WebSocket support to Spring Boot applications.");

        // ── Actuator & DevTools ─────────────────────────────
        DESCRIPTIONS.put("spring-boot-starter-actuator",
            "Provides production-ready monitoring endpoints like /health, /metrics, and /info for your application.");
        DESCRIPTIONS.put("spring-boot-devtools",
            "Developer tools for Spring Boot that enable automatic restarts and live reloading during development.");
        DESCRIPTIONS.put("spring-boot-actuator",
            "Spring Boot Actuator module providing built-in endpoints to monitor and manage your application.");

        // ── Apache Commons ──────────────────────────────────
        DESCRIPTIONS.put("commons-lang3",
            "Apache Commons utility library providing helper methods for strings, numbers, reflection, and more.");
        DESCRIPTIONS.put("commons-io",
            "Apache Commons library with utilities for file and stream I/O operations.");
        DESCRIPTIONS.put("commons-collections4",
            "Apache Commons library extending Java's standard collection types with additional data structures.");

        // ── Misc ────────────────────────────────────────────
        DESCRIPTIONS.put("snakeyaml",
            "YAML parser and emitter for Java, used by Spring Boot to read application.yml config files.");
        DESCRIPTIONS.put("byte-buddy",
            "Runtime code generation library used by Mockito and Hibernate to create dynamic proxy classes.");
        DESCRIPTIONS.put("jakarta-annotation-api",
            "Jakarta Annotations API providing common annotations like @PostConstruct and @PreDestroy.");
        DESCRIPTIONS.put("aspectjweaver",
            "AspectJ weaver that enables Spring AOP's proxy-based aspect weaving at runtime.");
        DESCRIPTIONS.put("micrometer-core",
            "Application metrics library used by Spring Boot Actuator to expose metrics in various formats.");
    }

    /**
     * Looks up a description for each artifactId.
     * If not found in the map, generates a readable fallback automatically.
     *
     * @param artifactIds - list of Maven artifactIds to describe
     * @return Map of artifactId → description
     */
    public Map<String, String> getDescriptions(List<String> artifactIds) {

        Map<String, String> result = new HashMap<>();

        for (String artifactId : artifactIds) {

            if (DESCRIPTIONS.containsKey(artifactId)) {
                // Known library — return pre-written description
                result.put(artifactId, DESCRIPTIONS.get(artifactId));
            } else {
                // Unknown library — generate a readable fallback from the name
                String readable = artifactId
                    .replace("-", " ")
                    .replace("_", " ");

                // Capitalize the first letter
                readable = Character.toUpperCase(readable.charAt(0))
                         + readable.substring(1);

                result.put(artifactId,
                    readable + " — a library included in this project. " +
                    "No built-in description available.");
            }
        }

        return result;
    }
}
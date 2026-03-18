package com.auditor.dependency.tracker.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

// Marks this class as a Spring-managed service bean
@Service
public class JarParserService {

    // -------------------------------------------------------
    // Spring Boot fat JARs bundle dependencies in BOOT-INF/lib/
    // Traditional WARs bundle them in WEB-INF/lib/
    // We check both to support either project type.
    // -------------------------------------------------------
    private static final String BOOT_INF_LIB = "BOOT-INF/lib/";
    private static final String WEB_INF_LIB  = "WEB-INF/lib/";

    // -------------------------------------------------------
    // Regex pattern to detect where the version number starts.
    // JAR filenames follow this convention:
    //   artifactId - version . jar
    //   spring-core-6.0.0.jar
    //   jackson-databind-2.15.2.jar
    //
    // The pattern matches a hyphen followed immediately by a digit,
    // which is where the version begins. Everything from that
    // hyphen to the end (.jar) will be stripped away.
    // -------------------------------------------------------
    private static final Pattern VERSION_PATTERN =
            Pattern.compile("-\\d+.*\\.jar$");

    /**
     * Parses an uploaded executable .jar file and extracts the
     * artifactIds of all physically bundled transitive dependencies.
     *
     * @param inputStream - the raw byte stream of the uploaded .jar file
     * @return a List of artifactId strings found inside BOOT-INF/lib/
     */
    public List<String> extractDependencies(InputStream inputStream) {

        // This list will collect every dependency artifactId we find
        List<String> dependencies = new ArrayList<>();

        try {
            // -------------------------------------------------------
            // STEP 1: Wrap the InputStream in a JarInputStream.
            // JarInputStream lets us walk through the JAR entry by
            // entry — just like iterating files in a ZIP archive.
            // -------------------------------------------------------
            JarInputStream jarInputStream = new JarInputStream(inputStream);

            // -------------------------------------------------------
            // STEP 2: Declare a JarEntry to hold the current entry
            // as we walk through the JAR.
            // A JarEntry represents one file/folder inside the JAR.
            // -------------------------------------------------------
            JarEntry entry;

            // -------------------------------------------------------
            // STEP 3: Loop through every entry inside the JAR.
            // getNextJarEntry() moves to the next file and returns
            // null when there are no more entries left.
            // -------------------------------------------------------
            while ((entry = jarInputStream.getNextJarEntry()) != null) {

                // Get the full internal path of this entry
                // e.g. "BOOT-INF/lib/spring-core-6.0.0.jar"
                String entryName = entry.getName();

                // -------------------------------------------------------
                // STEP 4: Filter — only process entries that are:
                //   1. Inside BOOT-INF/lib/ or WEB-INF/lib/
                //   2. Are actual .jar files (not folders)
                // This skips all compiled classes, resources, etc.
                // -------------------------------------------------------
                boolean isInLibFolder = entryName.startsWith(BOOT_INF_LIB)
                                     || entryName.startsWith(WEB_INF_LIB);

                boolean isJarFile = entryName.endsWith(".jar");

                if (isInLibFolder && isJarFile) {

                    // -------------------------------------------------------
                    // STEP 5: Extract just the filename from the full path.
                    // "BOOT-INF/lib/spring-core-6.0.0.jar"
                    //                 ↓
                    // "spring-core-6.0.0.jar"
                    // -------------------------------------------------------
                    String fileName = entryName.substring(
                                        entryName.lastIndexOf("/") + 1
                                      );

                    // -------------------------------------------------------
                    // STEP 6: Strip the version and .jar extension
                    // to get the clean artifactId.
                    // "spring-core-6.0.0.jar" → "spring-core"
                    // -------------------------------------------------------
                    String artifactId = stripVersion(fileName);

                    // Safety check: only add non-empty results
                    if (artifactId != null && !artifactId.isEmpty()) {
                        dependencies.add(artifactId);
                    }
                }
            }

        } catch (Exception e) {
            // If anything goes wrong (corrupt JAR, closed stream, etc.)
            // log a clear error and return an empty list safely.
            System.err.println("❌ Failed to parse JAR file: " + e.getMessage());
            return dependencies;
        }

        // Return the fully populated list of physical dependency artifactIds
        return dependencies;
    }

    /**
     * Strips the version number and .jar extension from a JAR filename
     * to produce a clean artifactId.
     *
     * Examples:
     *   "spring-core-6.0.0.jar"       → "spring-core"
     *   "jackson-databind-2.15.2.jar" → "jackson-databind"
     *   "lombok-1.18.30.jar"          → "lombok"
     *
     * @param fileName - the raw JAR filename
     * @return the artifactId without version or extension
     */
    private String stripVersion(String fileName) {

        // Try to find where the version number starts using our regex
        Matcher matcher = VERSION_PATTERN.matcher(fileName);

        if (matcher.find()) {
            // Return only the part before the version hyphen
            // "spring-core-6.0.0.jar" → "spring-core"
            return fileName.substring(0, matcher.start());
        }

        // Fallback: if no version pattern is found, just remove .jar
        // This handles edge cases like "somelib.jar" → "somelib"
        return fileName.replace(".jar", "");
    }
}
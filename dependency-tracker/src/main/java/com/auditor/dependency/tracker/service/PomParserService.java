package com.auditor.dependency.tracker.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Marks this class as a Spring-managed service bean
// Spring will automatically detect and register it for dependency injection
@Service
public class PomParserService {

    /**
     * Parses an uploaded pom.xml file and extracts all declared dependency artifactIds.
     *
     * @param inputStream - the raw byte stream of the uploaded pom.xml file
     * @return a List of artifactId strings found inside <dependency> tags
     */
    public List<String> extractDependencies(InputStream inputStream) {

        // This list will collect every artifactId we find in the pom.xml
        List<String> dependencies = new ArrayList<>();

        try {
            // -------------------------------------------------------
            // STEP 1: Create a DocumentBuilderFactory
            // This is the configuration hub for the XML parser.
            // You can enable features like namespace-awareness here.
            // -------------------------------------------------------
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // -------------------------------------------------------
            // STEP 2: Create a DocumentBuilder from the factory
            // This is the actual parser object that will read the XML.
            // -------------------------------------------------------
            DocumentBuilder builder = factory.newDocumentBuilder();

            // -------------------------------------------------------
            // STEP 3: Parse the InputStream into a Document object
            // The entire pom.xml is now loaded into memory as a
            // navigable DOM tree of nodes and elements.
            // -------------------------------------------------------
            Document document = builder.parse(inputStream);

            // -------------------------------------------------------
            // STEP 4: Normalize the DOM tree
            // This merges any fragmented text nodes caused by
            // whitespace or formatting in the XML file, ensuring
            // clean and consistent data extraction.
            // -------------------------------------------------------
            document.getDocumentElement().normalize();

            // -------------------------------------------------------
            // STEP 5: Get all <dependency> tags in the document
            // getElementsByTagName() searches the entire tree and
            // returns every matching tag as a NodeList.
            // -------------------------------------------------------
            NodeList dependencyNodes = document.getElementsByTagName("dependency");

            // -------------------------------------------------------
            // STEP 6: Loop through each <dependency> node
            // Each item in the NodeList is one <dependency> block
            // from your pom.xml, e.g.:
            //   <dependency>
            //       <groupId>org.springframework</groupId>
            //       <artifactId>spring-core</artifactId>
            //   </dependency>
            // -------------------------------------------------------
            for (int i = 0; i < dependencyNodes.getLength(); i++) {

                // Cast the raw Node to an Element so we can use
                // tag-specific methods like getElementsByTagName()
                Element dependencyElement = (Element) dependencyNodes.item(i);

                // -------------------------------------------------------
                // STEP 7: Inside this <dependency>, find the <artifactId>
                // getElementsByTagName() returns a NodeList even for a
                // single tag, so we use .item(0) to grab the first result.
                // -------------------------------------------------------
                NodeList artifactIdNodes = dependencyElement
                                            .getElementsByTagName("artifactId");

                // Safety check: only proceed if an <artifactId> tag exists
                if (artifactIdNodes.getLength() > 0) {

                    // -------------------------------------------------------
                    // STEP 8: Extract the text content of the <artifactId> tag
                    // .getTextContent() returns the raw string inside the tag,
                    // .trim() removes any accidental leading/trailing whitespace.
                    // -------------------------------------------------------
                    String artifactId = artifactIdNodes
                                            .item(0)
                                            .getTextContent()
                                            .trim();

                    // Add the clean artifactId string to our result list
                    dependencies.add(artifactId);
                }
            }

        } catch (Exception e) {
            // If anything goes wrong during parsing (malformed XML,
            // closed stream, unsupported encoding, etc.), we log a
            // clear error message and return an empty list safely.
            System.err.println("❌ Failed to parse pom.xml: " + e.getMessage());
            return dependencies; // returns empty list — never crashes the app
        }

        // Return the fully populated list of dependency artifactIds
        return dependencies;
    }
}

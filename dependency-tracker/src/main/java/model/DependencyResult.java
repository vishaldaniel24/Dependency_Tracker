package com.auditor.dependency.tracker.model;

// No Lombok needed — constructor, getters and setters written manually
public class DependencyResult {

    private String artifactId;
    private String description;
    private boolean usedInJar;

    // Constructor — this is what line 66 in the controller calls
    public DependencyResult(String artifactId, String description, boolean usedInJar) {
        this.artifactId = artifactId;
        this.description = description;
        this.usedInJar = usedInJar;
    }

    // Empty constructor
    public DependencyResult() {}

    // Getters
    public String getArtifactId()  { return artifactId; }
    public String getDescription() { return description; }
    public boolean isUsedInJar()   { return usedInJar; }

    // Setters
    public void setArtifactId(String artifactId)   { this.artifactId = artifactId; }
    public void setDescription(String description) { this.description = description; }
    public void setUsedInJar(boolean usedInJar)    { this.usedInJar = usedInJar; }
}
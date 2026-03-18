package com.auditor.dependency.tracker.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.auditor.dependency.tracker.model.DependencyResult;
import com.auditor.dependency.tracker.service.DependencyDescriptionService;
import com.auditor.dependency.tracker.service.JarParserService;
import com.auditor.dependency.tracker.service.PomParserService;

@Controller
public class DependencyController {

    // Manual constructor injection — no Lombok needed
    private final PomParserService pomParserService;
    private final JarParserService jarParserService;
    private final DependencyDescriptionService descriptionService;

    // ✅ Spring will automatically call this constructor to inject all 3 services
    public DependencyController(PomParserService pomParserService,
                                 JarParserService jarParserService,
                                 DependencyDescriptionService descriptionService) {
        this.pomParserService = pomParserService;
        this.jarParserService = jarParserService;
        this.descriptionService = descriptionService;
    }

    @GetMapping("/")
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/analyze")
    public String analyzeDependencies(
            @RequestParam("pomFile") MultipartFile pomFile,
            @RequestParam("jarFile") MultipartFile jarFile,
            Model model) {

        try {
            // STEP 1: Validate files
            if (pomFile.isEmpty() || jarFile.isEmpty()) {
                model.addAttribute("error", "Please upload both a pom.xml and a .jar file.");
                return "upload";
            }

            // STEP 2: Parse pom.xml → declared dependencies
            List<String> pomDependencies = pomParserService
                    .extractDependencies(pomFile.getInputStream());

            // STEP 3: Parse .jar → physically bundled libraries
            List<String> jarDependencies = jarParserService
                    .extractDependencies(jarFile.getInputStream());

            // STEP 4: Put jar entries in a Set for fast lookup
            Set<String> jarSet = new HashSet<>(jarDependencies);

            // STEP 5: Collect ALL unique artifact names
            Set<String> allArtifactIds = new LinkedHashSet<>();
            allArtifactIds.addAll(pomDependencies);
            allArtifactIds.addAll(jarDependencies);

            // STEP 6: Get descriptions — instant, no API needed
            Map<String, String> descriptions = descriptionService
                    .getDescriptions(new ArrayList<>(allArtifactIds));

            // STEP 7: Build result list for pom-declared dependencies
            List<DependencyResult> pomResults = pomDependencies.stream()
                .map(artifactId -> new DependencyResult(
                    artifactId,
                    descriptions.getOrDefault(artifactId, "No description available."),
                    jarSet.contains(artifactId)
                ))
                .collect(Collectors.toList());

            // STEP 8: Find transitive dependencies (in JAR but not in pom)
            Set<String> pomSet = new HashSet<>(pomDependencies);

            List<DependencyResult> transitiveResults = jarDependencies.stream()
                .filter(artifactId -> !pomSet.contains(artifactId))
                .map(artifactId -> new DependencyResult(
                    artifactId,
                    descriptions.getOrDefault(artifactId, "No description available."),
                    true
                ))
                .collect(Collectors.toList());

            // STEP 9: Separate used vs unused
            List<DependencyResult> usedDeps = pomResults.stream()
                    .filter(DependencyResult::isUsedInJar)
                    .collect(Collectors.toList());

            List<DependencyResult> unusedDeps = pomResults.stream()
                    .filter(dep -> !dep.isUsedInJar())
                    .collect(Collectors.toList());

            // STEP 10: Pass everything to Thymeleaf
            model.addAttribute("usedDeps",        usedDeps);
            model.addAttribute("unusedDeps",       unusedDeps);
            model.addAttribute("transitiveDeps",   transitiveResults);
            model.addAttribute("pomFile",          pomFile.getOriginalFilename());
            model.addAttribute("jarFile",          jarFile.getOriginalFilename());
            model.addAttribute("totalPom",         pomDependencies.size());
            model.addAttribute("totalJar",         jarDependencies.size());
            model.addAttribute("totalUsed",        usedDeps.size());
            model.addAttribute("totalUnused",      unusedDeps.size());
            model.addAttribute("totalTransitive",  transitiveResults.size());

            return "result";

        } catch (Exception e) {
            System.err.println("❌ Analysis failed: " + e.getMessage());
            model.addAttribute("error", "Analysis failed: " + e.getMessage());
            return "upload";
        }
    }
}
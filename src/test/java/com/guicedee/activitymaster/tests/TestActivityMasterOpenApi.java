package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.LogUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates the merged OpenAPI 3.1 document produced by the GuicedEE OpenAPI scanner for the
 * ActivityMaster FSDM REST surface.
 *
 * <p>The single application-level {@link io.swagger.v3.oas.annotations.OpenAPIDefinition} on
 * {@code ActivityMasterOpenApiConfiguration} declares the global metadata and the canonical tag
 * catalogue. Each individual JAX-RS resource ({@code RulesRestService},
 * {@code RulesTypeRestService}, {@code ClassificationRestService}, etc.) contributes its own paths
 * and is annotated with a matching {@link io.swagger.v3.oas.annotations.tags.Tag}. This test asserts
 * that the scanner merges all of those contributions into one coherent document: the global info and
 * tag catalogue survive, and every domain — including the newly added Rules / Rule Types /
 * Classifications resources — appears with correctly tagged find/create/update operations.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterOpenApi
{
    private OpenAPI openAPI;

    @BeforeAll
    public void setup()
    {
        LogUtils.addConsoleLogger(Level.INFO);
        ActivityMasterConfiguration.get()
                .setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();

        openAPI = IGuiceContext.get(OpenAPI.class);
        assertNotNull(openAPI, "The merged OpenAPI model should be provided by the GuicedEE OpenAPI module");
    }

    @Test
    public void globalInfoFromOpenApiDefinitionIsPresent()
    {
        assertNotNull(openAPI.getInfo(), "Merged document must retain the global @OpenAPIDefinition info block");
        org.junit.jupiter.api.Assertions.assertEquals("ActivityMaster FSDM API", openAPI.getInfo().getTitle(),
                "Global title should come from ActivityMasterOpenApiConfiguration");
        org.junit.jupiter.api.Assertions.assertEquals("3.0.0", openAPI.getInfo().getVersion(),
                "Global version should come from ActivityMasterOpenApiConfiguration");
    }

    @Test
    public void allCanonicalTagsAreMerged()
    {
        assertNotNull(openAPI.getTags(), "The merged document should carry the canonical tag catalogue");
        Set<String> tagNames = openAPI.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        // The full catalogue declared on the application-level @OpenAPIDefinition.
        Set<String> expected = Set.of(
                "Events", "Arrangements", "Parties", "Resource Items", "Products",
                "Classifications", "Classification Data Concepts", "Rules", "Rule Types", "Geography");

        assertTrue(tagNames.containsAll(expected),
                "All canonical FSDM tags should be merged into the document, but was: " + tagNames);
    }

    @Test
    public void newDomainPathsAreScannedAndMerged()
    {
        assertNotNull(openAPI.getPaths(), "The merged document should expose scanned JAX-RS paths");
        Set<String> paths = openAPI.getPaths().keySet();

        // Newly added resources.
        assertPathPresent(paths, "/rules/", "find");
        assertPathPresent(paths, "/rules/", "create");
        assertPathPresent(paths, "/rules/", "update");

        assertPathPresent(paths, "/rules-type/", "find");
        assertPathPresent(paths, "/rules-type/", "create");
        assertPathPresent(paths, "/rules-type/", "update");

        assertPathPresent(paths, "/classification/", "find");
        assertPathPresent(paths, "/classification/", "create");
        assertPathPresent(paths, "/classification/", "update");

        assertPathPresent(paths, "/classification-data-concept/", "find");
        assertPathPresent(paths, "/classification-data-concept/", "create");
        assertPathPresent(paths, "/classification-data-concept/", "update");
    }

    @Test
    public void existingDomainPathsRemainAfterMerge()
    {
        Set<String> paths = openAPI.getPaths().keySet();

        // Pre-existing resources must still be present alongside the newly merged ones.
        assertPathPresent(paths, "/product/", "find");
        assertPathPresent(paths, "/event/", "find");
        assertPathPresent(paths, "/party/", "find");
        assertPathPresent(paths, "/resource-item/", "find");
        assertPathPresent(paths, "/arrangement/", "find");
    }

    @Test
    public void enterpriseAndSystemScopingTemplatesArePreserved()
    {
        Set<String> paths = openAPI.getPaths().keySet();

        // Every FSDM path is scoped by enterprise and requesting system: /{enterprise}/<domain>/{requestingSystemName}/<op>
        boolean allScoped = paths.stream()
                .filter(p -> p.contains("/rules") || p.contains("/classification"))
                .allMatch(p -> p.contains("{enterprise}") && p.contains("{requestingSystemName}"));

        assertTrue(allScoped,
                "New rules/classification paths must keep the {enterprise} and {requestingSystemName} scoping templates: " + paths);
    }

    @Test
    public void rulesOperationsAreTaggedWithRules()
    {
        assertOperationsTagged("/rules/", "Rules");
        assertOperationsTagged("/rules-type/", "Rule Types");
        assertOperationsTagged("/classification/", "Classifications");
        assertOperationsTagged("/classification-data-concept/", "Classification Data Concepts");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────

    private void assertPathPresent(Set<String> paths, String domainSegment, String operation)
    {
        boolean present = paths.stream()
                .anyMatch(p -> p.contains(domainSegment) && p.endsWith("/" + operation));
        assertTrue(present,
                "Expected a merged path containing '" + domainSegment + "' ending in '/" + operation + "', but was: " + paths);
    }

    private void assertOperationsTagged(String domainSegment, String expectedTag)
    {
        List<Operation> operations = openAPI.getPaths()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().contains(domainSegment))
                .map(java.util.Map.Entry::getValue)
                .flatMap(item -> item.readOperations().stream())
                .toList();

        assertFalse(operations.isEmpty(),
                "Expected at least one operation for paths containing '" + domainSegment + "'");

        boolean allTagged = operations.stream()
                .allMatch(op -> op.getTags() != null && op.getTags().contains(expectedTag));
        assertTrue(allTagged,
                "All operations under '" + domainSegment + "' should be tagged '" + expectedTag + "'");
    }
}



package com.guicedee.activitymaster.fsdm.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Application-level OpenAPI 3.1 document configuration for the ActivityMaster
 * Functional Service Data Model (FSDM) REST surface.
 *
 * <p>This class carries no behaviour — it exists solely to host the global
 * {@link OpenAPIDefinition} annotation that the GuicedEE OpenAPI scanner reads at
 * startup. The generated specification is served at {@code /openapi.json} and
 * {@code /openapi.yaml}, and rendered through the Swagger UI at {@code /swagger/}.</p>
 *
 * <p>Every FSDM domain resource is grouped under a {@link Tag} declared below so the
 * generated documentation presents a consistent, navigable structure.</p>
 */
@OpenAPIDefinition(
        info = @Info(
                title = "ActivityMaster FSDM API",
                version = "3.0.0",
                description = """
                        Canonical Functional Service Data Model (FSDM) REST API for ActivityMaster.

                        All endpoints are scoped by enterprise and requesting system using the
                        path template `/{enterprise}/{domain}/{requestingSystemName}/{operation}`.
                        Create and update operations follow a fire-and-forget relationship
                        persistence model: the primary entity is returned immediately while
                        relationship links are persisted asynchronously.
                        """,
                contact = @Contact(name = "ActivityMaster", url = "https://github.com/Activity-Master"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = {
                @Server(
                        description = "Enterprise-scoped server",
                        url = "{scheme}://{host}:{port}",
                        variables = {
                                @ServerVariable(name = "scheme", defaultValue = "http", allowableValues = {"http", "https"}),
                                @ServerVariable(name = "host", defaultValue = "localhost"),
                                @ServerVariable(name = "port", defaultValue = "8080")
                        }
                )
        },
        tags = {
                @Tag(name = "Events", description = "Event and activity tracking — create, find, update and relationship management."),
                @Tag(name = "Arrangements", description = "Resource arrangements and bookings, including the optimised pivot read."),
                @Tag(name = "Parties", description = "Involved party (person/organisation) lifecycle and classification/identification search."),
                @Tag(name = "Resource Items", description = "Physical and virtual resource catalogue, including binary data management."),
                @Tag(name = "Products", description = "Product catalogue lifecycle — create, find, update and relationship management (types, classifications, resources)."),
                @Tag(name = "Classifications", description = "Classification value lifecycle — create, find and update classification values and their hierarchy."),
                @Tag(name = "Classification Data Concepts", description = "Classification data concept (bucket/scheme) lifecycle — create, find and update concepts, their values and attached resources."),
                @Tag(name = "Rules", description = "Business rule lifecycle — create, find and update rules, their classifications, products, rule types and composition hierarchy."),
                @Tag(name = "Rule Types", description = "Rule type lifecycle — create, find and update rule types, their classifications and supporting resources."),
                @Tag(name = "Geography", description = "On-demand GeoNames geographic data — countries, provinces, districts, postal codes, timezones and languages.")
        }
)
public final class ActivityMasterOpenApiConfiguration
{
    private ActivityMasterOpenApiConfiguration()
    {
        // Annotation holder only — never instantiated.
    }
}


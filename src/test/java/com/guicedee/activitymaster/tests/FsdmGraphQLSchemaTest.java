package com.guicedee.activitymaster.tests;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.WarehouseQueryFilter;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.WarehouseQuerySpec;
import com.guicedee.activitymaster.fsdm.graphql.FsdmGraphQLSchemaProvider;
import com.guicedee.activitymaster.fsdm.graphql.WarehouseEntitySerializer;
import com.guicedee.activitymaster.fsdm.graphql.WarehouseJsonScalar;
import com.guicedee.vertx.graphql.services.IGraphQLSchemaProvider;
import graphql.language.ArrayValue;
import graphql.language.BooleanValue;
import graphql.language.IntValue;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLTypeUtil;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Standalone (no database required) tests for the FSDM GraphQL layer.
 *
 * <p>Validates that the {@link FsdmGraphQLSchemaProvider} is correctly registered as an SPI,
 * that its SDL assembles into a valid executable schema with all seven domain queries, that the
 * permissive {@link WarehouseJsonScalar} coerces literals correctly, that the
 * {@link WarehouseEntitySerializer} flattens only scalar columns, and that the transport-neutral
 * {@link WarehouseQuerySpec} / {@link WarehouseQueryFilter} model behaves as expected.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FsdmGraphQLSchemaTest
{
    private static final Set<String> EXPECTED_QUERIES = Set.of(
            "involvedParties", "arrangements", "events", "products",
            "resourceItems", "classifications", "rules");

    private GraphQLSchema buildSchema()
    {
        FsdmGraphQLSchemaProvider provider = new FsdmGraphQLSchemaProvider();
        TypeDefinitionRegistry registry = provider.getTypeDefinitions();
        RuntimeWiring wiring = provider.configureWiring(RuntimeWiring.newRuntimeWiring()).build();
        return new SchemaGenerator().makeExecutableSchema(registry, wiring);
    }

    @Test
    public void providerRegisteredViaServiceLoader()
    {
        boolean found = ServiceLoader.load(IGraphQLSchemaProvider.class)
                .stream()
                .anyMatch(p -> p.type().equals(FsdmGraphQLSchemaProvider.class));
        assertTrue(found, "FsdmGraphQLSchemaProvider should be registered as an IGraphQLSchemaProvider service");
    }

    @Test
    public void schemaAssemblesWithAllSevenDomains()
    {
        GraphQLSchema schema = buildSchema();
        GraphQLObjectType query = schema.getQueryType();
        assertNotNull(query, "Query type should be present");

        Set<String> fields = query.getFieldDefinitions()
                .stream()
                .map(GraphQLFieldDefinition::getName)
                .collect(Collectors.toSet());
        assertEquals(EXPECTED_QUERIES, fields, "All seven FSDM domains should be exposed as queries");

        for (String name : EXPECTED_QUERIES)
        {
            GraphQLFieldDefinition field = query.getFieldDefinition(name);
            assertNotNull(field, "Query field should exist: " + name);
            assertEquals("[JSON!]!", GraphQLTypeUtil.simplePrint(field.getType()),
                    "Domain query '" + name + "' should return a non-null list of non-null JSON rows");
            assertNotNull(field.getArgument("query"), "Domain query '" + name + "' should accept a 'query' argument");
        }
    }

    @Test
    public void filterOperandEnumMatchesEntityAssist()
    {
        GraphQLSchema schema = buildSchema();
        GraphQLEnumType operand = (GraphQLEnumType) schema.getType("FilterOperand");
        assertNotNull(operand, "FilterOperand enum should be present");

        Set<String> sdlValues = operand.getValues()
                .stream()
                .map(v -> v.getName())
                .collect(Collectors.toSet());
        Set<String> entityAssistValues = java.util.Arrays.stream(Operand.values())
                .map(Enum::name)
                .collect(Collectors.toSet());
        assertEquals(entityAssistValues, sdlValues,
                "GraphQL FilterOperand should mirror the EntityAssist Operand enum exactly");
    }

    @Test
    public void queryInputExposesExpectedFields()
    {
        GraphQLSchema schema = buildSchema();
        GraphQLInputObjectType input = (GraphQLInputObjectType) schema.getType("QueryInput");
        assertNotNull(input, "QueryInput type should be present");

        Set<String> fields = input.getFieldDefinitions()
                .stream()
                .map(f -> f.getName())
                .collect(Collectors.toSet());
        assertTrue(fields.containsAll(Set.of(
                        "enterprise", "system", "filters", "orderBy", "descending",
                        "first", "max", "activeOnly", "inDateRange")),
                "QueryInput should expose the full dynamic query surface, but was: " + fields);
        assertEquals("String!", GraphQLTypeUtil.simplePrint(input.getField("enterprise").getType()),
                "enterprise is a required security scope");
        assertEquals("String!", GraphQLTypeUtil.simplePrint(input.getField("system").getType()),
                "system is a required security scope");
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
    public void jsonScalarCoercesLiteralsAndPassesThroughResults()
    {
        Coercing coercing = WarehouseJsonScalar.create().getCoercing();

        // serialize() is a pass-through of the already-shaped data fetcher result
        Map<String, Object> row = Map.of("name", "Alpha", "count", 3);
        assertSame(row, coercing.serialize(row), "JSON scalar serialize should pass the value straight through");

        // parseLiteral() understands the GraphQL literal AST
        assertEquals("hello", coercing.parseLiteral(new StringValue("hello")));
        assertEquals(true, coercing.parseLiteral(new BooleanValue(true)));
        assertEquals(BigInteger.valueOf(7), coercing.parseLiteral(new IntValue(BigInteger.valueOf(7))));

        ArrayValue array = ArrayValue.newArrayValue()
                .value(new StringValue("a"))
                .value(new StringValue("b"))
                .build();
        assertEquals(List.of("a", "b"), coercing.parseLiteral(array));

        ObjectValue object = ObjectValue.newObjectValue()
                .objectField(ObjectField.newObjectField().name("k").value(new StringValue("v")).build())
                .build();
        assertEquals(Map.of("k", "v"), coercing.parseLiteral(object));
    }

    @Test
    public void entitySerializerFlattensOnlyScalarColumns()
    {
        UUID ref = UUID.fromString("00000000-0000-0000-0000-0000000000aa");
        OffsetDateTime when = OffsetDateTime.of(2026, 1, 2, 3, 4, 5, 0, ZoneOffset.UTC);

        SampleEntity entity = new SampleEntity();
        entity.name = "Alpha";
        entity.order = 3;
        entity.active = Boolean.TRUE;
        entity.ref = ref;
        entity.when = when;
        entity.tags = List.of("x", "y");
        entity.meta = Map.of("k", "v");
        entity.empty = null;
        entity.code = "C1";

        Map<String, Object> result = WarehouseEntitySerializer.toMap(entity);

        // scalar columns are flattened
        assertEquals("Alpha", result.get("name"));
        assertEquals(3, result.get("order"));
        assertEquals(Boolean.TRUE, result.get("active"));
        // UUIDs and temporals are normalised to their string form
        assertEquals(ref.toString(), result.get("ref"));
        assertEquals(when.toString(), result.get("when"));
        // inherited scalar columns are also walked
        assertEquals("C1", result.get("code"));

        // relationship-like / non-scalar columns are skipped even when populated
        assertFalse(result.containsKey("tags"), "collections must not be serialised");
        assertFalse(result.containsKey("meta"), "maps must not be serialised");
        // null values are omitted
        assertFalse(result.containsKey("empty"), "null columns must be omitted");
    }

    @Test
    public void querySpecAndFilterDefaultsAndChaining()
    {
        WarehouseQuerySpec spec = new WarehouseQuerySpec();
        // sensible secure defaults
        assertTrue(spec.isActiveOnly(), "activeOnly should default to true");
        assertTrue(spec.isInDateRange(), "inDateRange should default to true");
        assertFalse(spec.isDescending(), "descending should default to false");
        assertNotNull(spec.getFilters(), "filters should be initialised");
        assertTrue(spec.getFilters().isEmpty());

        WarehouseQueryFilter eq = new WarehouseQueryFilter("name", Operand.Equals, "Alpha");
        WarehouseQueryFilter in = new WarehouseQueryFilter()
                .setPath("status")
                .setOperand(Operand.InList)
                .setValues(List.of("A", "B"));

        WarehouseQuerySpec returned = spec.addFilter(eq).addFilter(in);
        assertSame(spec, returned, "addFilter should return the spec for chaining");
        assertEquals(2, spec.getFilters().size());

        assertEquals("name", eq.getPath());
        assertEquals(Operand.Equals, eq.getOperand());
        assertEquals("Alpha", eq.getValue());

        assertEquals("status", in.getPath());
        assertEquals(Operand.InList, in.getOperand());
        assertEquals(List.of("A", "B"), in.getValues());

        // default operand on the no-arg constructor
        assertEquals(Operand.Equals, new WarehouseQueryFilter().getOperand());
    }

    /** Base type to exercise the serializer's superclass walk. */
    public static class SampleBase
    {
        public String code;
    }

    /** A stand-in entity carrying a mix of scalar and non-scalar columns. */
    public static class SampleEntity extends SampleBase
    {
        public String name;
        public int order;
        public Boolean active;
        public UUID ref;
        public OffsetDateTime when;
        public List<String> tags;
        public Map<String, String> meta;
        public String empty;
    }
}


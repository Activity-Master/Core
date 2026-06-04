package com.guicedee.activitymaster.fsdm.graphql;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.WarehouseQueryFilter;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.WarehouseQuerySpec;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import com.guicedee.vertx.graphql.services.IGraphQLSchemaProvider;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Exposes each of the seven FSDM domain separations as a GraphQL query.
 *
 * <p>Each query accepts a {@code QueryInput} which is translated into a transport-neutral
 * {@link WarehouseQuerySpec} and applied to the relevant EntityAssist {@link QueryBuilderSCD}
 * via {@code applyQuerySpec(...)} — dynamically building the criteria query. Execution runs inside
 * the canonical {@link SessionUtils#withActivityMaster} security/session context, and the resulting
 * Mutiny {@link Uni} is bridged to a Vert.x {@link Future} so the auto-installed
 * {@code VertxFutureAdapter} can resolve it.</p>
 */
public class FsdmGraphQLSchemaProvider implements IGraphQLSchemaProvider<FsdmGraphQLSchemaProvider>
{
    private static final Logger log = Logger.getLogger(FsdmGraphQLSchemaProvider.class.getName());

    private static final String SDL = """
            scalar JSON

            enum FilterOperand {
                Equals
                NotEquals
                Like
                NotLike
                LessThan
                LessThanEqualTo
                GreaterThan
                GreaterThanEqualTo
                Null
                NotNull
                InList
                NotInList
            }

            input FilterInput {
                path: String!
                operand: FilterOperand = Equals
                value: String
                values: [String!]
            }

            "Dynamic query definition shared by every FSDM domain."
            input QueryInput {
                "FSDM enterprise name (security scope)."
                enterprise: String!
                "FSDM system name performing the request (security scope)."
                system: String!
                filters: [FilterInput!]
                orderBy: String
                descending: Boolean = false
                first: Int
                max: Int
                "Restrict to the active-flag range (default true)."
                activeOnly: Boolean = true
                "Restrict to the effective date range (default true)."
                inDateRange: Boolean = true
            }

            type Query {
                involvedParties(query: QueryInput!): [JSON!]!
                arrangements(query: QueryInput!): [JSON!]!
                events(query: QueryInput!): [JSON!]!
                products(query: QueryInput!): [JSON!]!
                resourceItems(query: QueryInput!): [JSON!]!
                classifications(query: QueryInput!): [JSON!]!
                rules(query: QueryInput!): [JSON!]!
            }
            """;

    @Override
    public TypeDefinitionRegistry getTypeDefinitions()
    {
        return new SchemaParser().parse(SDL);
    }

    @Override
    public RuntimeWiring.Builder configureWiring(RuntimeWiring.Builder builder)
    {
        return builder
                .scalar(WarehouseJsonScalar.create())
                .type("Query", q -> q
                        .dataFetcher("involvedParties", domain(session -> new InvolvedParty().builder(session)))
                        .dataFetcher("arrangements", domain(session -> new Arrangement().builder(session)))
                        .dataFetcher("events", domain(session -> new Event().builder(session)))
                        .dataFetcher("products", domain(session -> new Product().builder(session)))
                        .dataFetcher("resourceItems", domain(session -> new ResourceItem().builder(session)))
                        .dataFetcher("classifications", domain(session -> new Classification().builder(session)))
                        .dataFetcher("rules", domain(session -> new Rules().builder(session)))
                );
    }

    /**
     * Builds a data fetcher for a domain root by supplying its session-bound query builder.
     *
     * @param builderFn function that produces a fresh {@link QueryBuilderSCD} bound to the session
     * @return a data fetcher returning a Vert.x {@link Future} of serialised rows
     */
    @SuppressWarnings("rawtypes")
    private DataFetcher<Future<List<Map<String, Object>>>> domain(Function<Mutiny.Session, QueryBuilderSCD> builderFn)
    {
        return env -> {
            Map<String, Object> input = env.getArgument("query");
            String enterprise = (String) input.get("enterprise");
            String system = (String) input.get("system");

            Uni<List<Map<String, Object>>> uni = SessionUtils.withActivityMaster(enterprise, system, tuple -> {
                WarehouseQuerySpec spec = toSpec(input).setEnterprise(tuple.getItem2());
                QueryBuilderSCD queryBuilder = builderFn.apply(tuple.getItem1()).applyQuerySpec(spec);
                @SuppressWarnings("unchecked")
                Uni<List<Object>> results = queryBuilder.getAll();
                return results.map(FsdmGraphQLSchemaProvider::serialize);
            });

            return Future.fromCompletionStage(uni.subscribeAsCompletionStage());
        };
    }

    private static List<Map<String, Object>> serialize(List<Object> rows)
    {
        List<Map<String, Object>> out = new ArrayList<>();
        if (rows != null)
        {
            for (Object row : rows)
            {
                out.add(WarehouseEntitySerializer.toMap(row));
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private static WarehouseQuerySpec toSpec(Map<String, Object> input)
    {
        WarehouseQuerySpec spec = new WarehouseQuerySpec();
        spec.setOrderBy((String) input.get("orderBy"));
        spec.setDescending(Boolean.TRUE.equals(input.get("descending")));
        if (input.get("activeOnly") != null)
        {
            spec.setActiveOnly(Boolean.TRUE.equals(input.get("activeOnly")));
        }
        if (input.get("inDateRange") != null)
        {
            spec.setInDateRange(Boolean.TRUE.equals(input.get("inDateRange")));
        }
        spec.setFirst((Integer) input.get("first"));
        spec.setMax((Integer) input.get("max"));

        Object rawFilters = input.get("filters");
        if (rawFilters instanceof List<?> filters)
        {
            for (Object raw : filters)
            {
                if (raw instanceof Map<?, ?> filterMap)
                {
                    WarehouseQueryFilter filter = new WarehouseQueryFilter();
                    filter.setPath((String) filterMap.get("path"));
                    filter.setOperand(toOperand((String) filterMap.get("operand")));
                    filter.setValue((String) filterMap.get("value"));
                    Object values = filterMap.get("values");
                    if (values instanceof List<?>)
                    {
                        filter.setValues((List<String>) values);
                    }
                    spec.addFilter(filter);
                }
            }
        }
        return spec;
    }

    private static Operand toOperand(String name)
    {
        if (name == null || name.isBlank())
        {
            return Operand.Equals;
        }
        try
        {
            return Operand.valueOf(name);
        }
        catch (IllegalArgumentException e)
        {
            log.warning("Unknown filter operand '" + name + "', defaulting to Equals");
            return Operand.Equals;
        }
    }
}



package com.guicedee.activitymaster.fsdm.graphql;

import graphql.language.ArrayValue;
import graphql.language.BooleanValue;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.NullValue;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A permissive {@code JSON} scalar used to return dynamically-shaped warehouse rows.
 *
 * <p>Serialisation is pass-through: the data fetchers already produce plain {@link Map} /
 * {@link List} / scalar structures (see {@link WarehouseEntitySerializer}), which the GraphQL
 * HTTP handler then encodes as JSON.</p>
 */
public final class WarehouseJsonScalar
{
    private WarehouseJsonScalar()
    {
    }

    public static GraphQLScalarType create()
    {
        return GraphQLScalarType.newScalar()
                .name("JSON")
                .description("Arbitrary JSON value representing a warehouse row")
                .coercing(new Coercing<Object, Object>()
                {
                    @Override
                    public Object serialize(Object dataFetcherResult)
                    {
                        return dataFetcherResult;
                    }

                    @Override
                    public Object parseValue(Object input)
                    {
                        return input;
                    }

                    @Override
                    public Object parseLiteral(Object input)
                    {
                        return parseLiteralValue(input);
                    }
                })
                .build();
    }

    private static Object parseLiteralValue(Object input)
    {
        switch (input)
        {
            case StringValue v -> { return v.getValue(); }
            case BooleanValue v -> { return v.isValue(); }
            case IntValue v -> { return v.getValue(); }
            case FloatValue v -> { return v.getValue(); }
            case NullValue ignored -> { return null; }
            case ArrayValue array ->
            {
                List<Object> values = new ArrayList<>();
                for (Value<?> value : array.getValues())
                {
                    values.add(parseLiteralValue(value));
                }
                return values;
            }
            case ObjectValue object ->
            {
                Map<String, Object> values = new LinkedHashMap<>();
                for (ObjectField field : object.getObjectFields())
                {
                    values.put(field.getName(), parseLiteralValue(field.getValue()));
                }
                return values;
            }
            default -> { return null; }
        }
    }
}


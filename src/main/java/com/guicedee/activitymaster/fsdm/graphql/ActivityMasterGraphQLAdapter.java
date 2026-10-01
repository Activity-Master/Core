package com.guicedee.activitymaster.fsdm.graphql;

import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScopeProperties;
import com.guicedee.client.scopes.CallScopeSource;
import com.guicedee.client.scopes.CallScoper;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Bridges typed domain APIs into GraphQL while capturing the verified HTTP call scope. */
public final class ActivityMasterGraphQLAdapter {
    private static final Logger LOG = Logger.getLogger(ActivityMasterGraphQLAdapter.class.getName());
    private ActivityMasterGraphQLAdapter() { }

    public static UUID id(DataFetchingEnvironment environment, String argument) {
        String value = environment.getArgument(argument);
        return value == null ? null : UUID.fromString(value);
    }

    public static List<UUID> ids(List<String> values) {
        return values == null ? List.of() : values.stream().map(UUID::fromString).toList();
    }

    public static <T> DataFetcher<Object> fetch(String domain, Function<DataFetchingEnvironment, Uni<T>> operation) {
        return environment -> {
            CallScoper scoper = null;
            boolean entered = false;
            try {
                // RoutingContext is supplied by the server, never by GraphQL variables.
                RoutingContext context = environment.getGraphQlContext().get(RoutingContext.class);
                if (context != null) {
                    scoper = IGuiceContext.get(CallScoper.class);
                    scoper.enter();
                    entered = true;
                    CallScopeProperties properties = IGuiceContext.get(CallScopeProperties.class);
                    if (properties.getSource() == CallScopeSource.Unknown) properties.setSource(CallScopeSource.Http);
                    properties.getProperties().put("RoutingContext", context);
                    properties.getProperties().put("HttpServerRequest", context.request());
                    properties.getProperties().put("HttpServerResponse", context.response());
                }
                // Subscribe within the scope: APIs capture host identity before opening a transaction.
                return operation.apply(environment).subscribeAsCompletionStage().handle((result, failure) -> failure == null
                        ? DataFetcherResult.<T>newResult().data(result).build() : error(environment, domain, failure));
            } catch (Exception failure) {
                return error(environment, domain, failure);
            } finally {
                if (entered) scoper.exit();
            }
        };
    }

    private static DataFetcherResult<Object> error(DataFetchingEnvironment environment, String domain, Throwable failure) {
        while (failure instanceof CompletionException && failure.getCause() != null) failure = failure.getCause();
        String code, message;
        if (failure instanceof SecurityException) {
            code = "FORBIDDEN"; message = domain + " access denied";
        } else if (failure instanceof NotFoundException) {
            code = "NOT_FOUND"; message = domain + " target unavailable";
        } else if (failure instanceof IllegalArgumentException || failure instanceof BadRequestException) {
            code = "BAD_USER_INPUT"; message = "Invalid " + domain.toLowerCase(java.util.Locale.ROOT) + " input";
        } else {
            LOG.log(Level.SEVERE, domain + " GraphQL operation failed", failure);
            code = "INTERNAL_SERVER_ERROR"; message = domain + " operation failed";
        }
        return DataFetcherResult.newResult().error(GraphqlErrorBuilder.newError(environment).message(message)
                .extensions(Map.of("code", code)).build()).build();
    }
}

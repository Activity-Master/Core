package com.guicedee.activitymaster.fsdm.implementations;

import com.guicedee.client.services.lifecycle.IGuicePostStartup;
import com.guicedee.vertx.spi.VertXPreStartup;
import com.guicedee.vertx.web.spi.VertxRouterConfigurator;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.instrumentation.JsonObjectAdapter;

import java.util.List;

public class GraphQLPostStartup implements IGuicePostStartup<GraphQLPostStartup>, VertxRouterConfigurator<GraphQLPostStartup> {
    @Override
    public List<Future<Boolean>> postLoad() {
        return List.of();
    }


    @Override
    public Router builder(Router router) {

        GraphiQLHandlerOptions options = new GraphiQLHandlerOptions()
                .setEnabled(true);
        GraphiQLHandler handler = GraphiQLHandler.create(VertXPreStartup.getVertx(), options);
        router.route("/graphiql*").subRouter(handler.router());
        //graph ql needs json adapter
     //   graphQLBuilder.instrumentation(new JsonObjectAdapter());
      //  router.route("/graphql").handler(GraphQLHandler.create(graphQL));
        return router;
    }
}

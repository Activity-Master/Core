package com.guicedee.activitymaster.fsdm.plugins;

import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

/** Forward conversion of built-in extension identities, after plugin taxonomy and before domain taxonomy. */
@SortedUpdate(sortOrder = 1020, taskCount = 1)
public final class BuiltInPluginsInstall implements ISystemUpdate {
    @Override public Uni<Boolean> update(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        ISystemsService<?> systems = IGuiceContext.get(ISystemsService.class);
        return systems.getActivityMaster(session, enterprise).chain(core ->
                systems.getSecurityIdentityToken(session, core).chain(token -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    // Register newly added capabilities first, so dependency declarations do not depend on SPI order.
                    for (IMasterPlugin<?> plugin : IMasterPlugin.allPlugins())
                            chain = chain.chain(() -> systems.doesSystemExist(session, enterprise, plugin.getSystemName(), token)
                                    .chain(exists -> exists ? Uni.createFrom().voidItem()
                                            : plugin.registerSystem(session, enterprise).replaceWithVoid()));
                    for (IMasterPlugin<?> plugin : IMasterPlugin.allPlugins())
                            chain = chain.chain(() -> IGuiceContext.get(PluginService.class).registerBuiltIn(session, core, token, plugin));
                    return chain;
                })).replaceWith(true);
    }
}

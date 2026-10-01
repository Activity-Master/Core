package com.guicedee.activitymaster.fsdm.plugins;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

/** Forward update for enterprises which already recorded the original built-in plugin update. */
@SortedUpdate(sortOrder = 1030, taskCount = 1)
public final class PluginArchitectureInstall implements ISystemUpdate {
    @Override public Uni<Boolean> update(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        return new BuiltInPluginsInstall().update(session, enterprise);
    }
}

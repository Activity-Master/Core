package com.guicedee.activitymaster.fsdm.plugins;

import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.ISystemUpdate;
import com.guicedee.activitymaster.fsdm.client.services.systems.SortedUpdate;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

/** Forward taxonomy update adding the catalogue age rating; existing plugins without a rating are "All". */
@SortedUpdate(sortOrder = 1040, taskCount = 1)
public final class PluginAgeRatingInstall implements ISystemUpdate {
    @Override public Uni<Boolean> update(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        ISystemsService<?> systems = IGuiceContext.get(ISystemsService.class);
        return systems.getActivityMaster(session, enterprise)
                .chain(core -> systems.getSecurityIdentityToken(session, core)
                        .chain(token -> IGuiceContext.get(PluginService.class).installAgeRatingTaxonomy(session, core, token)))
                .replaceWith(true);
    }
}

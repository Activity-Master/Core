package com.guicedee.activitymaster;

import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.plugins.PluginModels;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import java.util.UUID;

/** Explicit verified organic User fixture, rather than a legacy administrator/group/system credential. */
public final class BuiltInPluginFixture {
    private BuiltInPluginFixture() { }
    public static Uni<PluginModels.Identity> administrator(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        ISystemsService<?> systems = IGuiceContext.get(ISystemsService.class);
        IInvolvedPartyService<?> parties = IGuiceContext.get(IInvolvedPartyService.class);
        ISecurityTokenService<?> security = IGuiceContext.get(ISecurityTokenService.class);
        IClassificationService<?> classes = IGuiceContext.get(IClassificationService.class);
        return systems.getActivityMaster(session, enterprise).chain(core -> systems.getSecurityIdentityToken(session, core)
                .chain(bootstrap -> parties.createIdentificationType(session, core, "PluginFixture", "Plugin fixture", bootstrap)
                        .chain(() -> parties.create(session, core, new Pair<>("PluginFixture", UUID.randomUUID().toString()), true, bootstrap))
                        .chain(party -> security.create(session, "User", "PluginFixture-" + UUID.randomUUID(), "Verified fixture user", core)
                                .chain(user -> security.getAdministratorsFolder(session, core, bootstrap)
                                        .chain(folder -> classes.find(session, "User", core, bootstrap)
                                                .chain(type -> security.link(session, folder, user, type)))
                                        .replaceWith(new PluginModels.Identity(party.getId(), enterprise.getId(),
                                                UUID.fromString(user.getSecurityToken())))))));
    }
}

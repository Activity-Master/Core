package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

/** Vocabulary only. Installation and actor grants are never seeded automatically. */
public final class FsdmBehaviorTaxonomy {
    private FsdmBehaviorTaxonomy() { }

    public static Uni<Void> ensure(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID systemCredential) {
        IEventService<?> events = IGuiceContext.get(IEventService.class);
        IClassificationService<?> classifications = IGuiceContext.get(IClassificationService.class);
        Uni<Void> chain = events.createEventType(session, FsdmBehaviorAuthority.type(system.getId(),FsdmBehaviorAuthority.INSTALLATION), system, systemCredential)
                .chain(() -> events.createEventType(session, FsdmBehaviorAuthority.type(system.getId(),FsdmBehaviorAuthority.GRANT), system, systemCredential))
                .replaceWithVoid();
        for (String name : new String[]{"ScopedProvider", "ScopedRealm", "ScopedOwner", "ScopedBehavior"}) {
            chain = chain.chain(() -> classifications.create(session, FsdmBehaviorAuthority.role(system.getId(),name), name + " scoped behavior metadata",
                    EnterpriseClassificationDataConcepts.EventXClassification, system, systemCredential).replaceWithVoid());
        }
        chain = chain.chain(() -> classifications.create(session, FsdmBehaviorAuthority.role(system.getId(),"ScopedActor"), "Actor receiving a scoped behavior",
                EnterpriseClassificationDataConcepts.EventXInvolvedParty, system, systemCredential).replaceWithVoid());
        return chain.chain(() -> classifications.create(session, FsdmBehaviorAuthority.role(system.getId(),"ScopedEventType"), "Scoped authorization event type",
                EnterpriseClassificationDataConcepts.EventXEventType, system, systemCredential).replaceWithVoid());
    }
}

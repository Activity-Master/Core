package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

@Log4j2
@Singleton
public class SystemsProvider implements Provider<ISystems<Systems, SystemsQueryBuilder>>
{
    @Inject
    private Provider<IEnterprise<?, ?>> enterprise;
    @Inject
    private Provider<ISystemsService<?>> systemsService;

    private String systemName;

    public SystemsProvider()
    {
    }

    public SystemsProvider(String systemName)
    {
        this.systemName = systemName;
    }

    @Override
    public ISystems<Systems, SystemsQueryBuilder> get()
    {
        if (EnterpriseProvider.loadedEnterprise == null || EnterpriseProvider.loadedEnterprise.isFake())
        {
            return new Systems();
        }

        var factory = IGuiceContext.get(Mutiny.SessionFactory.class);

        log.info("🔍 Starting enterprise fetch...");

        Uni<ISystems<Systems, SystemsQueryBuilder>> result = (Uni) factory.withSession(session -> {
            log.info("📦 Enterprise Session opened");
            return session.withTransaction(tx -> {
                log.info("🔁 Transaction started");
                return systemsService.get()
                               .findSystem(session, enterprise.get(),
                                       systemName, null);
            });
        });

        log.info("⏳ Awaiting enterprise result...");
        return result.await()
                       .atMost(Duration.ofSeconds(50));

    }

}

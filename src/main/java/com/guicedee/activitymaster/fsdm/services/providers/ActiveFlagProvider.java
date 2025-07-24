package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

@Log4j2
@Singleton
public class ActiveFlagProvider implements Provider<IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>>
{
    @Inject
    private Provider<IEnterprise<?, ?>> enterprise;

    @Inject
    private Provider<IActiveFlagService<?>> activeFlagService;

    private final com.entityassist.enumerations.ActiveFlag flag;

    public ActiveFlagProvider(com.entityassist.enumerations.ActiveFlag flag)
    {
        this.flag = flag;
    }

    @Override
    public IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder> get()
    {
        if (enterprise.get()
                    .isFake())
        {
            return new ActiveFlag();
        }
        var factory = IGuiceContext.get(Mutiny.SessionFactory.class);

        log.info("🔍 Starting enterprise fetch...");

        Uni<IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>> result = (Uni) factory.withSession(session -> {
            log.info("📦 Enterprise Session opened");
            return session.withTransaction(tx -> {
                log.info("🔁 Transaction started");
                return activeFlagService.get()
                               .findFlagByName(session, flag, enterprise.get());
            });
        });

        log.info("⏳ Awaiting enterprise result...");

        return result.await()
                       .atMost(Duration.ofSeconds(50));
    }
}

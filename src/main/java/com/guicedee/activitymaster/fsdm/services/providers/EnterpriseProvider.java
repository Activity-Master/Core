package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
//import jakarta.transaction.Transactional;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.java.Log;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

@Log
@Singleton
public class EnterpriseProvider implements Provider<IEnterprise<Enterprise, EnterpriseQueryBuilder>>
{
    @Inject
    private Provider<ActivityMasterConfiguration> configuration;

    @Inject
    private Provider<IEnterpriseService<?>> enterpriseService;

    public static IEnterprise<Enterprise, EnterpriseQueryBuilder> loadedEnterprise = null;

    @SuppressWarnings("unchecked")
    @Override
    //@Transactional
    public IEnterprise<Enterprise, EnterpriseQueryBuilder> get()
    {
        var factory = IGuiceContext.get(Mutiny.SessionFactory.class);

        log.info("🔍 Starting enterprise fetch...");

        Uni<IEnterprise<Enterprise, EnterpriseQueryBuilder>> result = (Uni)factory.withSession(session -> {
                                                                                  log.info("📦 Enterprise Session opened");
                                                                                  return session.withTransaction(tx -> {
                                                                                      log.info("🔁 Transaction started");
                                                                                      return enterpriseService.get()
                                                                                                     .isEnterpriseReady(session)
                                                                                                     .invoke(ent -> log.info("✅ Enterprise is ready: " + ent.getName()));
                                                                                  });
                                                                              });

        log.info("⏳ Awaiting enterprise result...");

        return result.await()
                       .atMost(Duration.ofSeconds(50));
    }
}

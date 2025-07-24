package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Log4j2
public class SystemsTokenProvider implements Provider<UUID>
{
	@Inject
	private Provider<IEnterprise<?, ?>> enterprise;
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	private String systemName;
	
	public SystemsTokenProvider()
	{
	}
	
	public SystemsTokenProvider(String systemName)
	{
		this.systemName = systemName;
	}
	
	@Override
	public UUID get()
	{
		if (enterprise.get()
		              .isFake())
		{
			return UUID.randomUUID();
		}

		  var factory = IGuiceContext.get(Mutiny.SessionFactory.class);

        log.info("🔍 Starting " + systemName +" fetch...");

        Uni<UUID> result = factory.withSession(session -> {
            log.info("📦 " + systemName +" Session opened");
            return session.withTransaction(tx -> {
                log.info("🔁 Transaction started");
                return systemsService.get()
				.findSystem(session, enterprise.get(), systemName)
				.chain(system -> {
					return systemsService.get()
							.getSecurityIdentityToken(session, system);
				});
            });
        });

        log.info("⏳ Awaiting " + systemName + " tokens result...");
		return result
				.await()
				.atMost(Duration.of(50L, ChronoUnit.SECONDS))
		;
	}
}

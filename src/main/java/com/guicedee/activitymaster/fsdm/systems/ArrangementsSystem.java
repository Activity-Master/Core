package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IArrangementsService.ArrangementSystemName;


@Log4j2
public class ArrangementsSystem
        extends ActivityMasterDefaultSystem<ArrangementsSystem>
        implements IActivityMasterSystem<ArrangementsSystem>
{
    @Inject
    private ISystemsService<?> systemsService;

    @Override
    public ISystems<?, ?> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        ISystems<?, ?> iSystems = systemsService
                                          .create(session, enterprise, getSystemName(), getSystemDescription())
                                          .await()
                                          .atMost(Duration.ofMinutes(1))
                ;
        getSystem(session, enterprise).chain(system -> {
                    return systemsService
                                   .registerNewSystem(session, enterprise, system);
                })
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        return iSystems;
    }

    @Override
    public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("Starting reactive postStartup for Arrangements System");
        log.info("Starting reactive postStartup for Active Flag System");

        // Create a reactive chain for the postStartup operations
        // Get the system
        return systemsService.findSystem(session, enterprise, getSystemName())
                       .onItem()
                       .ifNull()
                       .failWith(() -> new RuntimeException("System not found: " + getSystemName()))
                       .chain(system -> {
                           log.debug("Found system: {}", system.getName());
                           // Get the security token
                           return systemsService.getSecurityIdentityToken(session, system)
                                          .onItem()
                                          .ifNull()
                                          .failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
                                          .map(token -> {
                                              log.debug("Found security token for system: {}", system.getName());
                                              return null; // Return Void
                                          });
                       })
                       .replaceWith(Uni.createFrom()
                                            .voidItem());
    }

    @Override
    public int totalTasks()
    {
        return 4;
    }

    @Override
    public Integer sortOrder()
    {
        return Integer.MIN_VALUE + 8;
    }

    @Override
    public String getSystemName()
    {
        return ArrangementSystemName;
    }

    @Override
    public String getSystemDescription()
    {
        return "The system for the arrangement management";
    }

}

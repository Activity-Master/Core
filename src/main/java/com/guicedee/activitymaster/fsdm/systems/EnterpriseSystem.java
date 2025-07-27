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

import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;

@Log4j2
public class EnterpriseSystem
    extends ActivityMasterDefaultSystem<EnterpriseSystem>
    implements IActivityMasterSystem<EnterpriseSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public ISystems<?, ?> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    return null;
  }


  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Enterprise System", "Starting Enterprise Checks");
    log.info("Creating enterprise defaults in a new session and transaction");
    // No actual operations needed, just return a void item
    return Uni.createFrom()
               .voidItem();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("Starting reactive postStartup for Enterprise System");

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
    return 0;
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE;
  }

  @Override
  public String getSystemName()
  {
    return EnterpriseSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for handling enterprises";
  }
}

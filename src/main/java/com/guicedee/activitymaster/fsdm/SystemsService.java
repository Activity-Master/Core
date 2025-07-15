package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SystemsException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassification;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.*;

public class SystemsService
        implements ISystemsService<SystemsService>
{
    private static final Logger log = Logger.getLogger(SystemsService.class.getName());

    @Inject
    private IEnterprise<?, ?> enterprise;

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private ISecurityTokenService<?> securityTokenService;

    public ISystems<?, ?> get()
    {
        return new Systems();
    }

    @Override
    //@CacheResult(cacheName = "GetActivityMaster")
    public Uni<ISystems<?, ?>> getActivityMaster(@CacheKey ISystems<?, ?> requestingSystem, @CacheKey java.util.UUID... identityToken)
    {
        return findSystem(requestingSystem, ActivityMasterSystemName, identityToken);
    }

    @Override
    //@CacheResult(cacheName = "GetActivityMasterEnterprise")
    public Uni<ISystems<?, ?>> getActivityMaster(@CacheKey IEnterprise<?, ?> requestingSystem, java.util.UUID... identityToken)
    {
        return findSystem(requestingSystem, ActivityMasterSystemName, identityToken);
    }

    //@Transactional()
    @Override
    public Uni<Boolean> doesSystemExist(IEnterprise<?, ?> enterprise, String systemName, java.util.UUID... identityToken)
    {
        return new Systems().builder()
                       .withName(systemName)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .getCount()
                       .map(count -> count > 0);
    }

    //@Transactional()
    //@CacheResult(cacheName = "FindSystemEnterpriseLevel")
    @Override
    public Uni<ISystems<?, ?>> findSystem(@CacheKey IEnterprise<?, ?> enterprise, @CacheKey String systemName, java.util.UUID... identityToken)
    {
        Systems search = new Systems();
        return search.builder()
                       .withName(systemName)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       //.canRead(enterprise, identityToken)
                       .get()
                       .chain(result -> {
                           if (result != null)
                           {
                               return Uni.createFrom()
                                              .item(result);
                           }
                           else
                           {
                               return Uni.createFrom()
                                              .failure(new SystemsException("Cannot find a system named - " + systemName + " - in enterprise - " + enterprise));
                           }
                       });
    }

    //@Transactional()
    //@CacheResult(cacheName = "FindSystemByIdentityClassification")
    @Override
    public Uni<ISystems<?, ?>> findSystem(@CacheKey ISystems<?, ?> requestingSystem, @CacheKey String parentSystem, java.util.UUID... identityToken)
    {
        SystemsXClassification systemClassifications = new SystemsXClassification();

        // Get identity classification - wrap non-reactive result in Uni
        IClassification<?, ?> identifyClassification = classificationService.getIdentityType(requestingSystem, identityToken);

        // Use the classification to build the query
        return systemClassifications.builder()
                       .findLink(null, (Classification) identifyClassification, parentSystem)
                       .inDateRange()
                       .withEnterprise(enterprise)
                       .canRead(requestingSystem, identityToken)
                       .get()
                       .chain(result -> {
                           if (result == null)
                           {
                               return Uni.createFrom()
                                              .failure(new SystemsException("Cannot find a child system for - " + requestingSystem + " - in enterprise - " + enterprise));
                           }
                           else
                           {
                               // Get the system from the relationship and return it as ISystems<?,?>
                               return Uni.createFrom()
                                              .item(result.getSystemID());
                           }
                       });
    }

    @Override
    //@Transactional()
    public Uni<String> registerNewSystem(IEnterprise<?, ?> enterprise, ISystems<?, ?> newSystem)
    {
        // Get the activity master system and token
        Uni<ISystems<?, ?>> activityMasterSystemUni = getISystemReactive(ActivityMasterSystemName);
        Uni<UUID> activityMasterSystemUUIDUni = getISystemTokenReactive(ActivityMasterSystemName);

        // Chain the operations
        return Uni.combine()
                       .all()
                       .unis(activityMasterSystemUni, activityMasterSystemUUIDUni)
                       .asTuple()
                       .chain(tuple -> {
                           ISystems<?, ?> activityMasterSystem = tuple.getItem1();
                           UUID activityMasterSystemUUID = tuple.getItem2();

                           // Create security tokens using non-reactive service
                           return Uni.createFrom()
                                          .emitter(emitter -> {
                                              try
                                              {
                                                  // Create first security token
                                                  ISecurityToken<?, ?> newSystemsSecurityToken = securityTokenService.create(
                                                          UserGroupSecurityTokenClassifications.System.toString(),
                                                          newSystem.getName(),
                                                          newSystem.getDescription(),
                                                          activityMasterSystem
                                                  );

                                                  // Create second security token
                                                  ISecurityToken<?, ?> systemsToken = securityTokenService.create(
                                                          UserGroupSecurityTokenClassifications.System.toString(),
                                                          UserGroupSecurityTokenClassifications.System.toString(),
                                                          UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                          activityMasterSystem
                                                  );

                                                  // Find classification
                                                  IClassification<?, ?> classification = classificationService.find(
                                                          UserGroupSecurityTokenClassifications.System,
                                                          activityMasterSystem,
                                                          activityMasterSystemUUID
                                                  );

                                                  // Link tokens
                                                  securityTokenService.link(
                                                          systemsToken,
                                                          newSystemsSecurityToken,
                                                          classification
                                                  );

                                                  // Add classification to new system
                                                  newSystem.addOrReuseClassification(
                                                          SystemsClassifications.SystemIdentity,
                                                          ((SecurityToken) newSystemsSecurityToken).getSecurityToken(),
                                                          newSystem,
                                                          activityMasterSystemUUID
                                                  );

                                                  // Get security identity token
                                                  UUID newSystemUUID = getSecurityIdentityToken(newSystem, activityMasterSystemUUID)
                                                                               .await()
                                                                               .indefinitely()
                                                          ;

                                                  // Create default security
                                                  ((SecurityToken) newSystemsSecurityToken).createDefaultSecurity(
                                                          activityMasterSystem,
                                                          activityMasterSystemUUID
                                                  );

                                                  ((SecurityToken) systemsToken).createDefaultSecurity(
                                                          activityMasterSystem,
                                                          activityMasterSystemUUID
                                                  );

                                                  // Create involved party
                                                  SystemsSystem systemsSystem = IGuiceContext.get(SystemsSystem.class);
                                                  systemsSystem.createInvolvedPartyForNewSystem(newSystem);

                                                  // Complete with the new system UUID
                                                  emitter.complete(newSystemUUID.toString());
                                              }
                                              catch (Exception e)
                                              {
                                                  emitter.fail(e);
                                              }
                                          });
                       });
    }

    // Helper methods for reactive operations
    private Uni<ISystems<?, ?>> getISystemReactive(String systemName)
    {
        return Uni.createFrom()
                       .item(() -> {
                           ISystems<?, ?> system = IActivityMasterService.getISystem(systemName);
                           if (system == null)
                           {
                               throw new NoSuchElementException("System not found: " + systemName);
                           }
                           return system;
                       });
    }

    private Uni<UUID> getISystemTokenReactive(String systemName)
    {
        return Uni.createFrom()
                       .item(() -> {
                           UUID token = IActivityMasterService.getISystemToken(systemName);
                           if (token == null)
                           {
                               throw new NoSuchElementException("System token not found: " + systemName);
                           }
                           return token;
                       });
    }

    @Override
    public Uni<ISystems<?, ?>> create(IEnterprise<?, ?> enterprise, String systemName, String systemDesc, java.util.UUID... identityToken)
    {
        return create(enterprise, systemName, systemDesc, systemName, identityToken);
    }

    @Override
    //@Transactional()
    public Uni<ISystems<?, ?>> create(IEnterprise<?, ?> enterprise, String systemName, String systemDesc, String historyName, java.util.UUID... identityToken)
    {
        Systems newSystem = new Systems();

        // Check if system exists
        return newSystem.builder()
                       .withEnterprise(enterprise)
                       .withName(systemName)
                       .get()
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // System doesn't exist, create it
                               return Uni.createFrom()
                                              .emitter(emitter -> {
                                                  try
                                                  {
                                                      // Set up the new system
                                                      newSystem.setName(systemName);
                                                      newSystem.setDescription(systemDesc);
                                                      newSystem.setSystemHistoryName(historyName);
                                                      newSystem.setEnterpriseID(enterprise);

                                                      // Get active flag service
                                                      IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

                                                      // Get active flag (non-reactive)
                                                      IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
                                                      newSystem.setActiveFlagID(activeFlag);

                                                      // Persist the new system
                                                      newSystem.persist();

                                                      // Get activity master
                                                      ISystems<?, ?> activityMaster = getActivityMaster(enterprise).await()
                                                                                              .indefinitely()
                                                              ;

                                                      // Create default security (non-reactive)
                                                      newSystem.createDefaultSecurity(activityMaster, identityToken);

                                                      // Complete with the new system
                                                      emitter.complete(newSystem);
                                                  }
                                                  catch (Exception e)
                                                  {
                                                      emitter.fail(e);
                                                  }
                                              });
                           }
                           else
                           {
                               // System already exists, find it
                               return findSystem(enterprise, systemName, identityToken);
                           }
                       });
    }

    //@Transactional()
    //@CacheResult(cacheName = "SystemGetSecurityToken")
    public Uni<ISecurityToken<?, ?>> getSecurityToken(@CacheKey String uuidIdentity, @CacheKey ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return new SecurityToken().builder()
                       .findBySecurityToken(uuidIdentity.toString(), enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .withEnterprise(enterprise)
                       //      .canRead(system, identityToken)
                       .get()
                       .map(securityToken -> securityToken);
    }

    //@CacheResult(cacheName = "SystemSetSecurityTokenUUID")
    @Override
    public Uni<UUID> getSecurityIdentityToken(@CacheKey ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return system.findClassification(SystemIdentity, system, identityToken)
                       .map(systemToken -> systemToken != null ? systemToken.getValueAsUUID() : null);
    }
}

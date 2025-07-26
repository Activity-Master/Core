package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.EnterpriseXClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItemType;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassification;
import com.guicedee.client.IGuiceContext;
//import jakarta.transaction.Transactional;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class SecurityTokenSystem
        extends ActivityMasterDefaultSystem<SecurityTokenSystem>
        implements IActivityMasterSystem<SecurityTokenSystem>
{

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private ISecurityTokenService<?> securityTokenService;

    @Inject
    private ISystemsService<?> systemsService;

    @Override
    public ISystems<?, ?> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        // Note: Using reactive programming internally but maintaining synchronous interface
        log.info("Registering security token system for enterprise: {}", enterprise.getName());

        // Create the system
        ISystems<?, ?> iSystems = systemsService
                                          .create(session, enterprise, getSystemName(), getSystemDescription())
                                          .onItem()
                                          .invoke(system -> log.debug("Created system: {}", system.getName()))
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating system: {}", error.getMessage(), error))
                                          .await()
                                          .atMost(Duration.ofMinutes(1))
                ;

        // Register the system
        getSystem(session, enterprise).chain(system -> {
                    return systemsService
                                   .registerNewSystem(session, enterprise, system);
                })
                .onItem()
                .invoke(() -> log.debug("Registered system: {}", getSystemName()))
                .onFailure()
                .invoke(error -> log.error("Error registering system: {}", error.getMessage(), error))
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        log.info("Successfully registered security token system for enterprise: {}", enterprise.getName());
        return iSystems;
    }

    @Override
    public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Security Token Service", "Starting Security Structure Checks/Install");

        // Get the ActivityMaster system
        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
            .invoke(activityMasterSystem -> {
                SecurityTokenSystem instance = IGuiceContext.get(SecurityTokenSystem.class);

                // Call defaultsCreation which handles the core security setup
                // Note: This is still synchronous but will be converted in a future update
                instance.defaultsCreation(session, enterprise);

                logProgress("Security Management", "Setting Security Configurator to Activity Master");
            })
            .chain(activityMasterSystem -> {
                // Get all the systems that are created before this one
                EnterpriseSystem enterpriseSystem = com.guicedee.client.IGuiceContext.get(EnterpriseSystem.class);
                ActiveFlagSystem afs = com.guicedee.client.IGuiceContext.get(ActiveFlagSystem.class);
                ClassificationsSystem cfs = com.guicedee.client.IGuiceContext.get(ClassificationsSystem.class);
                ClassificationsDataConceptSystem cdcs = com.guicedee.client.IGuiceContext.get(ClassificationsDataConceptSystem.class);
                SystemsSystem ss = com.guicedee.client.IGuiceContext.get(SystemsSystem.class);
                InvolvedPartySystem ips = com.guicedee.client.IGuiceContext.get(InvolvedPartySystem.class);

                // For now, we'll still use the blocking approach for getting systems
                // This will be converted to fully reactive in a future update
                ISystems<?, ?> enterpriseSystemObj = enterpriseSystem.getSystem(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));
                ISystems<?, ?> afsSystemObj = afs.getSystem(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));
                ISystems<?, ?> ssSystemObj = ss.getSystem(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));
                ISystems<?, ?> cdcsSystemObj = cdcs.getSystem(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));
                ISystems<?, ?> cfsSystemObj = cfs.getSystem(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));
                ISystems<?, ?> ipsSystemObj = ips.getSystem(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));

                // Create a list of operations to run in parallel
                List<Uni<?>> registerOperations = new ArrayList<>();

                // Add all system registration operations to the list
                registerOperations.add(systemsService.registerNewSystem(session, enterprise, enterpriseSystemObj));
                registerOperations.add(systemsService.registerNewSystem(session, enterprise, afsSystemObj));
                registerOperations.add(systemsService.registerNewSystem(session, enterprise, ssSystemObj));
                registerOperations.add(systemsService.registerNewSystem(session, enterprise, cdcsSystemObj));
                registerOperations.add(systemsService.registerNewSystem(session, enterprise, cfsSystemObj));
                registerOperations.add(systemsService.registerNewSystem(session, enterprise, ipsSystemObj));

                log.info("Running {} system registration operations in parallel", registerOperations.size());

                // Execute all operations in parallel
                return Uni.combine()
                        .all()
                        .unis(registerOperations)
                        .discardItems()
                        .onFailure()
                        .invoke(error -> log.error("Error registering systems: {}", error.getMessage(), error))
                        .invoke(v -> {
                            logProgress("Security Token Service", "Enabling Security System");
                            System.out.println("Enabling Authentication Modules");
                            //todo enable security
                            //	com.guicedee.client.IGuiceContext.get(ActivityMasterConfiguration.class)
                            //            .setSecurityEnabled(true);
                        });
            })
            .replaceWith(Uni.createFrom().voidItem());
    }

    //@Transactional
    void defaultsCreation(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        // Note: Using reactive programming internally but maintaining synchronous interface
        ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                                      .await()
                                                      .atMost(Duration.ofMinutes(1))
                ;

        log.info("Starting security defaults creation for enterprise: {}", enterprise.getName());

        // Step 1: Create security defaults
        createSecurityDefaults(session, enterprise.getName(), activityMasterSystem);

        // Step 2: Create security tokens
        SecurityToken rootToken = createSecurityTokens(session, enterprise.getName(), enterprise);

        // Step 3: Create groups and folders
        createGroupsAndFolders(session, enterprise, rootToken);

        // Step 4: Apply defaults to new enterprise
        applyDefaultsToNewEnterprise(session, enterprise);

        // Step 5: Create ActivityMaster involved party
        createActivityMasterInvolvedParty(session, enterprise);

        // Step 6: Apply defaults after ActivityMaster
        applyDefaultsToNewEnterpriseAfterActivityMaster(session, enterprise);

        log.info("Completed security defaults creation for enterprise: {}", enterprise.getName());
    }

    //@Transactional
    void registerNewSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> creatingSystem)
    {
        // Note: Using reactive programming internally but maintaining synchronous interface
        systemsService.registerNewSystem(session, enterprise, creatingSystem)
                .onFailure()
                .invoke(error -> log.error("Error registering system {}: {}",
                        creatingSystem.getName(), error.getMessage(), error))
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        log.debug("Successfully registered system: {}", creatingSystem.getName());
    }

    void createSecurityDefaults(Mutiny.Session session, String enterpriseName, ISystems<?, ?> system, UUID... identityToken)
    {
        log.info("Creating security defaults for enterprise: {}", enterpriseName);

        // Create the enterprise classification
        IClassification<?, ?> entClassification = classificationService.create(session, enterpriseName, system, identityToken)
                                                          .await()
                                                          .atMost(Duration.ofMinutes(1))
                ;

        log.debug("Created enterprise classification: {}", entClassification.getName());

        // Create security-related classifications in parallel
        List<Uni<?>> securityClassifications = new ArrayList<>();

        // Add all classification creation operations to the list
        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.UserGroup.toString(),
                SecurityTokenClassifications.UserGroup.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 1, entClassification, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.User.toString(),
                SecurityTokenClassifications.User.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.Guests.toString(),
                SecurityTokenClassifications.Guests.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.Visitors.toString(),
                SecurityTokenClassifications.Visitors.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.Registered.toString(),
                SecurityTokenClassifications.Registered.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 2, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.Application.toString(),
                SecurityTokenClassifications.Application.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 3, identityToken));

        securityClassifications.add(classificationService.create(session,
                UserGroupSecurityTokenClassifications.System.toString(),
                UserGroupSecurityTokenClassifications.System.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 4, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.Plugin.toString(),
                SecurityTokenClassifications.Plugin.toString(), EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, 5, identityToken));

        securityClassifications.add(classificationService.create(session,
                SecurityTokenClassifications.Identity.toString(),
                "A security token identity", EnterpriseClassificationDataConcepts.SecurityTokenXClassification, system, 1, entClassification, identityToken));

        log.info("Running {} security classification creation operations in parallel", securityClassifications.size());

        // Execute all operations in parallel
        Uni.combine()
                .all()
                .unis(securityClassifications)
                .discardItems()
                .onFailure()
                .invoke(error -> log.error("Error creating security classifications: {}", error.getMessage(), error))
                .await()
                .atMost(Duration.ofMinutes(2))
        ;

        logProgress("Security Token Service", "Security Classifications Installed", 11);
    }

    //@Transactional
    SecurityToken createSecurityTokens(Mutiny.Session session, String enterpriseName, IEnterprise<?, ?> enterprise)
    {
        log.info("Creating security tokens for enterprise: {}", enterpriseName);

        // Note: Using reactive programming internally but maintaining synchronous interface
        UUID uuid = getSystemToken(session, enterprise).await().atMost(java.time.Duration.ofMinutes(1));
        ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                                      .await()
                                                      .atMost(Duration.ofMinutes(1))
                ;

        // Prepare the description
        String description = enterprise.getDescription()
                                     .isEmpty()
                                     ? "An enterprise-wide project"
                                     : enterprise.getDescription();

        // Create the root token
        SecurityToken rootToken = (SecurityToken) securityTokenService.create(session, enterpriseName,
                        enterprise.getName(), description, activityMasterSystem)
                                                          .onItem()
                                                          .invoke(token -> log.debug("Created root token: {}", token.getName()))
                                                          .onFailure()
                                                          .invoke(error -> log.error("Error creating root token: {}", error.getMessage(), error))
                                                          .await()
                                                          .atMost(Duration.ofMinutes(1));

        // Grant access to the token
        securityTokenService.grantAccessToToken(session, rootToken, rootToken, false, false, false, false, activityMasterSystem)
                .onFailure()
                .invoke(error -> log.error("Error granting access to token: {}", error.getMessage(), error))
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        // Add classification to the enterprise
        try
        {
            enterprise.addOrUpdateClassification(session, EnterpriseClassifications.EnterpriseIdentity,
                    null,
                    rootToken.getSecurityToken(),
                    activityMasterSystem,
                    uuid);
            log.debug("Added classification to enterprise: {}", EnterpriseClassifications.EnterpriseIdentity);
        }
        catch (Exception e)
        {
            log.error("Error adding classification to enterprise: {}", e.getMessage(), e);
        }

        logProgress("Security Token Service", "Enterprise Security Validated", 3);
        log.info("Successfully created security tokens for enterprise: {}", enterpriseName);

        return rootToken;
    }

    @SuppressWarnings("Duplicates")
    void createGroupsAndFolders(Mutiny.Session session, IEnterprise<?, ?> enterprise, SecurityToken rootToken)
    {
        log.info("Creating groups and folders for enterprise: {}", enterprise.getName());

        ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                                      .await()
                                                      .atMost(Duration.ofMinutes(1))
                ;
        // Create the everyone token
        SecurityToken everyoneToken = (SecurityToken) securityTokenService.create(
                        session, SecurityTokenClassifications.UserGroup.toString(),
                        UserGroupSecurityTokenClassifications.Everyone.toString(),
                        UserGroupSecurityTokenClassifications.Everyone.classificationDescription(),
                        activityMasterSystem)
                                                              .await()
                                                              .atMost(Duration.ofMinutes(1));
        SecurityToken everywhereToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.UserGroup.toString(),
                UserGroupSecurityTokenClassifications.Everywhere.toString(),
                UserGroupSecurityTokenClassifications.Everywhere.classificationDescription(),
                activityMasterSystem);
        SecurityToken administratorsToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.UserGroup.toString(),
                UserGroupSecurityTokenClassifications.Administrators.toString(),
                UserGroupSecurityTokenClassifications.Administrators.classificationDescription(),
                activityMasterSystem);
        SecurityToken usersGuestsToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.UserGroup.toString(),
                SecurityTokenClassifications.Guests.toString(),
                SecurityTokenClassifications.Guests.classificationDescription(),
                activityMasterSystem);
        SecurityToken usersGuestsVisitorsToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.UserGroup.toString(),
                SecurityTokenClassifications.Visitors.toString(),
                SecurityTokenClassifications.Visitors.classificationDescription(),
                activityMasterSystem);
        SecurityToken usersGuestsRegisteredToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.UserGroup.toString(),
                SecurityTokenClassifications.Registered.toString(),
                SecurityTokenClassifications.Registered.classificationDescription(),
                activityMasterSystem);

        SecurityToken applicationToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.Application.toString(),
                UserGroupSecurityTokenClassifications.Applications.toString(),
                UserGroupSecurityTokenClassifications.Applications.classificationDescription(),
                activityMasterSystem);
        SecurityToken systemsToken = (SecurityToken) securityTokenService.create(
                session, UserGroupSecurityTokenClassifications.System.toString(),
                UserGroupSecurityTokenClassifications.System.toString(),
                UserGroupSecurityTokenClassifications.System.classificationDescription(),
                activityMasterSystem);

        SecurityToken pluginToken = (SecurityToken) securityTokenService.create(
                session, SecurityTokenClassifications.Plugin.toString(),
                UserGroupSecurityTokenClassifications.Plugins.toString(),
                UserGroupSecurityTokenClassifications.Plugins.classificationDescription(),
                activityMasterSystem);

        SecurityToken activityMasterToken = (SecurityToken) securityTokenService.create(
                session, UserGroupSecurityTokenClassifications.System.toString(),
                "Activity Master System", "Defines the activity master as a system", activityMasterSystem);

        logProgress("Security Token Service", "Base Security Tokens", 11);

        activityMasterSystem.addOrReuseClassification(session, SystemsClassifications.SystemIdentity,
                        activityMasterToken.getSecurityToken(),
                        activityMasterSystem)
                .onItem()
                .invoke(result -> log.debug("Added classification to activity master system: {}", SystemsClassifications.SystemIdentity))
                .onFailure()
                .invoke(error -> log.error("Error adding classification to activity master system: {}", error.getMessage(), error))
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        // Get the UserGroup classification
        IClassification<?, ?> userGroupClassification = classificationService.find(session, SecurityTokenClassifications.UserGroup, activityMasterSystem)
                                                                .await()
                                                                .atMost(Duration.ofMinutes(1))
                ;

        // Link tokens with classifications
        securityTokenService.link(session, rootToken, everyoneToken, userGroupClassification)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        securityTokenService.link(session, rootToken, everywhereToken, userGroupClassification)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        securityTokenService.link(session, everyoneToken, administratorsToken, userGroupClassification)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
	/*	link(everyoneToken, usersToken,
		     classificationService.find(UserGroup, SecurityTokenXSecurityToken.class, activityMasterSystem));
*/
        // Get classifications for linking tokens
        IClassification<?, ?> userGroupClass = classificationService.find(session, SecurityTokenClassifications.UserGroup, activityMasterSystem)
                                                       .await()
                                                       .atMost(Duration.ofMinutes(1))
                ;
        IClassification<?, ?> applicationClass = classificationService.find(session, SecurityTokenClassifications.Application, activityMasterSystem)
                                                         .await()
                                                         .atMost(Duration.ofMinutes(1))
                ;
        IClassification<?, ?> systemClass = classificationService.find(session, UserGroupSecurityTokenClassifications.System, activityMasterSystem)
                                                    .await()
                                                    .atMost(Duration.ofMinutes(1))
                ;
        IClassification<?, ?> pluginClass = classificationService.find(session, SecurityTokenClassifications.Plugin, activityMasterSystem)
                                                    .await()
                                                    .atMost(Duration.ofMinutes(1))
                ;

        // Link tokens with their classifications
        securityTokenService.link(session, everyoneToken, usersGuestsToken, userGroupClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        securityTokenService.link(session, usersGuestsToken, usersGuestsRegisteredToken, userGroupClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        securityTokenService.link(session, usersGuestsToken, usersGuestsVisitorsToken, userGroupClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        securityTokenService.link(session, rootToken, applicationToken, applicationClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        securityTokenService.link(session, rootToken, systemsToken, systemClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        securityTokenService.link(session, rootToken, pluginToken, pluginClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        securityTokenService.link(session, systemsToken, activityMasterToken, systemClass)
                .await()
                .atMost(Duration.ofMinutes(1))
        ;

        logProgress("Security Token Service", "Security Hierarchy Confirmed", 11);

        // Process all grantAccessToToken operations in parallel
        log.info("Starting parallel processing of security token access grants");

        // Create lists to hold all the operations for each token type
        List<Uni<?>> administratorsOperations = new ArrayList<>();
        List<Uni<?>> usersGuestsOperations = new ArrayList<>();
        List<Uni<?>> usersGuestsRegisteredOperations = new ArrayList<>();
        List<Uni<?>> usersGuestsVisitorsOperations = new ArrayList<>();
        List<Uni<?>> everyoneOperations = new ArrayList<>();
        List<Uni<?>> everywhereOperations = new ArrayList<>();
        List<Uni<?>> applicationOperations = new ArrayList<>();
        List<Uni<?>> systemsOperations = new ArrayList<>();
        List<Uni<?>> pluginOperations = new ArrayList<>();

        // Add operations for administratorsToken
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, rootToken, true, true, true, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, everyoneToken, true, true, false, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, administratorsToken, true, true, false, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, applicationToken, true, true, false, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, everywhereToken, true, true, true, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, systemsToken, true, true, false, true, activityMasterSystem));
        administratorsOperations.add(securityTokenService.grantAccessToToken(session, administratorsToken, pluginToken, true, true, false, true, activityMasterSystem));

        // Add operations for usersGuestsToken
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, rootToken, false, false, false, false, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, everyoneToken, false, false, false, false, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, administratorsToken, false, false, false, false, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, applicationToken, false, false, false, false, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, everywhereToken, false, false, false, true, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, usersGuestsToken, false, false, false, true, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, systemsToken, false, false, false, false, activityMasterSystem));
        usersGuestsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsToken, pluginToken, false, false, false, false, activityMasterSystem));

        // Add operations for usersGuestsRegisteredToken
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, rootToken, false, false, false, false, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, everyoneToken, false, false, false, false, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, administratorsToken, false, false, false, false, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, applicationToken, false, false, false, false, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, everywhereToken, false, false, false, true, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, usersGuestsToken, false, false, false, true, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, systemsToken, false, false, false, false, activityMasterSystem));
        usersGuestsRegisteredOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, pluginToken, false, false, false, false, activityMasterSystem));

        // Add operations for usersGuestsVisitorsToken
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, rootToken, false, false, false, false, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, everyoneToken, false, false, false, false, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, administratorsToken, false, false, false, false, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, applicationToken, false, false, false, false, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, everywhereToken, false, false, false, true, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, usersGuestsToken, false, false, false, true, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, systemsToken, false, false, false, false, activityMasterSystem));
        usersGuestsVisitorsOperations.add(securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, pluginToken, false, false, false, false, activityMasterSystem));

        // Add operations for everyoneToken
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, rootToken, false, false, false, true, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, everyoneToken, false, false, false, false, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, administratorsToken, false, false, false, false, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, applicationToken, false, false, false, true, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, everywhereToken, true, true, false, true, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, systemsToken, false, false, false, true, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, pluginToken, false, false, false, true, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, usersGuestsToken, false, false, false, false, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem));
        everyoneOperations.add(securityTokenService.grantAccessToToken(session, everyoneToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem));

        // Add operations for everywhereToken
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, rootToken, false, false, false, true, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, everyoneToken, false, false, false, true, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, administratorsToken, false, false, false, false, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, applicationToken, false, false, false, false, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, systemsToken, false, false, false, false, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, everywhereToken, false, false, false, true, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, pluginToken, false, false, false, false, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, usersGuestsToken, false, false, false, false, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, usersGuestsRegisteredToken, false, false, false, false, activityMasterSystem));
        everywhereOperations.add(securityTokenService.grantAccessToToken(session, everywhereToken, usersGuestsVisitorsToken, false, false, false, false, activityMasterSystem));

        // Add operations for applicationToken
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, rootToken, false, false, false, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, everyoneToken, true, true, false, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, administratorsToken, true, false, false, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, applicationToken, true, true, false, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, everywhereToken, true, true, false, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, systemsToken, true, true, true, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, pluginToken, true, true, true, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem));
        applicationOperations.add(securityTokenService.grantAccessToToken(session, applicationToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem));

        // Add operations for systemsToken
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, rootToken, false, false, false, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, everyoneToken, true, true, false, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, administratorsToken, true, false, false, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, applicationToken, true, true, false, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, everywhereToken, true, true, false, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, systemsToken, true, true, true, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, pluginToken, true, true, true, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem));
        systemsOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem));

        // Add operations for pluginToken
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, rootToken, false, false, false, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, everyoneToken, true, true, false, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, administratorsToken, true, false, false, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, applicationToken, true, true, false, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, systemsToken, everywhereToken, true, true, false, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, systemsToken, true, true, true, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, pluginToken, true, true, true, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, usersGuestsToken, true, true, true, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, usersGuestsRegisteredToken, true, true, true, true, activityMasterSystem));
        pluginOperations.add(securityTokenService.grantAccessToToken(session, pluginToken, usersGuestsVisitorsToken, true, true, true, true, activityMasterSystem));

        // Combine all operations into a single list
        List<Uni<?>> allOperations = new ArrayList<>();
        allOperations.addAll(administratorsOperations);
        allOperations.addAll(usersGuestsOperations);
        allOperations.addAll(usersGuestsRegisteredOperations);
        allOperations.addAll(usersGuestsVisitorsOperations);
        allOperations.addAll(everyoneOperations);
        allOperations.addAll(everywhereOperations);
        allOperations.addAll(applicationOperations);
        allOperations.addAll(systemsOperations);
        allOperations.addAll(pluginOperations);

        log.info("Running {} token access grant operations in parallel", allOperations.size());

        // Execute all operations in parallel and wait for them to complete
        Uni.combine()
                .all()
                .unis(allOperations)
                .discardItems()
                .onItem()
                .invoke(() -> log.info("All token access grant operations completed successfully"))
                .onFailure()
                .invoke(error -> log.error("Error granting token access: {}", error.getMessage(), error))
                .await()
                .atMost(Duration.ofMinutes(5))
        ;

        logProgress("Security Token Service", "Default Security Confirmed", 37);
    }

    void applyDefaultsToNewEnterprise(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("🏢 Starting reactive security defaults application for enterprise: {}", enterprise.getName());
        
        // Note: Using reactive programming internally but maintaining synchronous interface
        ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                                      .await()
                                                      .atMost(Duration.ofMinutes(1))
                ;
        logProgress("Security Token Service", "Checking Default Security for all enterprise default items");

        // Create parallel operations for all security table creation
        List<Uni<Void>> securityOperations = new ArrayList<>();
        
        logProgress("Security Token Service", "Starting basic security checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ActiveFlag(), activityMasterSystem));
        
        logProgress("Security Token Service", "Systems securities", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new Systems(), activityMasterSystem));
        
        logProgress("Security Token Service", "System Classifications securities", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new SystemsXClassification(), activityMasterSystem));
        
        logProgress("Security Token Service", "Classification Data Concepts security checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ClassificationDataConcept(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Classifications checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new Classification(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Hierarchy checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ClassificationXClassification(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Identification Type checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyIdentificationType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Name Types checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyNameType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Party Type checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Organic Types checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyOrganicType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Final Enterprise checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new EnterpriseXClassification(), activityMasterSystem));

        securityOperations.add(createDefaultSecurityForTableReactive(session, (WarehouseCoreTable<?, ?, ?, ?>) enterprise, activityMasterSystem));
        
        log.info("🚀 Executing {} security table operations in parallel", securityOperations.size());
        
        // Execute all operations in parallel
        Uni.combine()
                .all()
                .unis(securityOperations)
                .discardItems()
                .onItem()
                .invoke(() -> {
                    log.info("✅ All enterprise security defaults applied successfully");
                    logProgress("Security Token Service", "Completed Checks", 1);
                })
                .onFailure()
                .invoke(error -> {
                    log.error("❌ Failed to apply enterprise security defaults: {}", error.getMessage(), error);
                })
                .await()
                .atMost(Duration.ofMinutes(5));
    }

    void createActivityMasterInvolvedParty(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        // Note: Using reactive programming internally but maintaining synchronous interface
        ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                                      .await()
                                                      .atMost(Duration.ofMinutes(1))
                ;
        com.guicedee.client.IGuiceContext.get(SystemsSystem.class)
                .createInvolvedPartyForNewSystem(session, activityMasterSystem);
    }

    void applyDefaultsToNewEnterpriseAfterActivityMaster(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("🏢 Starting reactive post-ActivityMaster security defaults for enterprise: {}", enterprise.getName());
        
        // Note: Using reactive programming internally but maintaining synchronous interface
        ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                                                      .await()
                                                      .atMost(Duration.ofMinutes(1))
                ;
        
        // Create parallel operations for all post-ActivityMaster security table creation
        List<Uni<Void>> securityOperations = new ArrayList<>();
        
        logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedParty(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Organic Relationship checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyOrganic(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Non Organic checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyNonOrganic(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyXInvolvedPartyIdentificationType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Classifications Relationship checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyXClassification(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Name Type Relationship checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyXInvolvedPartyNameType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Involved Party Type Relationship checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new InvolvedPartyXInvolvedPartyType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Events checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new EventType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Resource Item Types checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ResourceItemType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Arrangement Types checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ArrangementType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting Arrangement checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new Arrangement(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting ArrangementXType checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ArrangementXArrangementType(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting ArrangementXClassification checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ArrangementXClassification(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting ArrangementXResourceItem checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ArrangementXResourceItem(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting ArrangementXInvolvedParty checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ArrangementXInvolvedParty(), activityMasterSystem));
        
        logProgress("Security Token Service", "Starting ArrangementXProduct checks", 1);
        securityOperations.add(createDefaultSecurityForTableReactive(session, new ArrangementXProduct(), activityMasterSystem));
        
        log.info("🚀 Executing {} post-ActivityMaster security operations in parallel", securityOperations.size());
        
        // Execute all operations in parallel
        Uni.combine()
                .all()
                .unis(securityOperations)
                .discardItems()
                .onItem()
                .invoke(() -> {
                    log.info("✅ All post-ActivityMaster security defaults applied successfully");
                })
                .onFailure()
                .invoke(error -> {
                    log.error("❌ Failed to apply post-ActivityMaster security defaults: {}", error.getMessage(), error);
                })
                .await()
                .atMost(Duration.ofMinutes(5));
    }

    void createDefaultSecurityForTable(WarehouseCoreTable<?, ?, ?, ?> table, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        // This method is currently disabled but could be implemented using reactive programming
        // The commented code below shows how it might be implemented

		/*
		// Note: Using reactive programming internally but maintaining synchronous interface
		table.builder()
		     .withEnterprise(system.getEnterpriseID())
		     .whereNoSecurityIsApplied()
		     .inDateRange()
		     .getAll()
		     .onItem().invoke(items -> log.debug("Found {} items without security", items.size()))
		     .chain(items -> {
		         // Create a list of operations to run in parallel
		         List<Uni<?>> securityOperations = new ArrayList<>();

		         for (WarehouseCoreTable next : items) {
		             logProgress("Security Token Service", "Checking - " + next.getClass().getSimpleName(), 0);
		             // Add security creation operation to the list
		             securityOperations.add(next.createDefaultSecurity(session, system, identityToken));
		         }

		         if (securityOperations.isEmpty()) {
		             return Uni.createFrom().voidItem();
		         }

		         // Execute all operations in parallel
		         return Uni.combine().all().unis(securityOperations)
		                  .discardItems()
		                  .onFailure().invoke(error -> log.error("Error creating default security: {}", 
		                                                        error.getMessage(), error));
		     })
		     .await().atMost(Duration.ofMinutes(2));
		*/
    }

    Uni<Void> createDefaultSecurityForTableReactive(Mutiny.Session session, WarehouseCoreTable<?, ?, ?, ?> table, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("🔐 Creating reactive security for table: {}", table.getClass().getSimpleName());
        
        return table.builder(session)
            //todo.withEnterprise(system.getEnterpriseID())
             // todo .whereNoSecurityIsApplied()
             .inDateRange()
             .getAll()
             .onItem().invoke(items -> log.debug("Found {} items without security for {}", items.size(), table.getClass().getSimpleName()))
             .chain(items -> {
                 // Create a list of operations to run in parallel
                 List<Uni<Void>> securityOperations = new ArrayList<>();

                 for (Object next : items) {
                     WarehouseCoreTable<?, ?, ?, ?> tableItem = (WarehouseCoreTable<?, ?, ?, ?>) next;
                     logProgress("Security Token Service", "Checking - " + tableItem.getClass().getSimpleName(), 0);
                     // Add security creation operation to the list
                     securityOperations.add(tableItem.createDefaultSecurity(session, system, identityToken));
                 }

                 if (securityOperations.isEmpty()) {
                     log.debug("✅ No security operations needed for {}", table.getClass().getSimpleName());
                     return Uni.createFrom().voidItem();
                 }

                 log.debug("🚀 Executing {} security operations for {}", securityOperations.size(), table.getClass().getSimpleName());
                 
                 // Execute all operations in parallel
                 return Uni.combine().all().unis(securityOperations)
                          .discardItems()
                          .onItem().invoke(() -> log.debug("✅ Completed security for {}", table.getClass().getSimpleName()))
                          .onFailure().invoke(error -> log.error("❌ Error creating default security for {}: {}", 
                                                                table.getClass().getSimpleName(), error.getMessage(), error))
                          .map(result -> null);
             });
    }

    @Override
    public int totalTasks()
    {
        return 190;
    }

    @Override
    public Integer sortOrder()
    {
        return Integer.MIN_VALUE + 6;
    }

    @Override
    public String getSystemName()
    {
        return SecurityTokenSystemName;
    }

    @Override
    public String getSystemDescription()
    {
        return "The system for managing Security Tokens";
    }
}

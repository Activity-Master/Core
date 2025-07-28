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
   	private Mutiny.SessionFactory sessionFactory;

   	@Inject
    private ISystemsService<?> systemsService;

    @Override
    public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("🚀 Registering Security Token System for enterprise: '{}'", enterprise.getName());
        log.debug("📋 Creating Security Token System with session: {}", session.hashCode());

        return systemsService
            .create(session, enterprise, getSystemName(), getSystemDescription())
            .onItem()
            .invoke(system -> {
                log.debug("✅ Created Security Token System: '{}' with session: {}", system.getName(), session.hashCode());
                
                // Chain the registerNewSystem call properly
                getSystem(session, enterprise)
                    .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                    .onItem()
                    .invoke(() -> {
                        log.debug("✅ Registered system: {}", getSystemName());
                        log.info("🎉 Successfully registered Security Token System for enterprise: '{}'", enterprise.getName());
                    })
                    .onFailure()
                    .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                    .chain(() -> Uni.createFrom().item(system)); // Chain back to return the original system
            })
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Security Token System: '{}' with session {}: {}",
                getSystemName(), session.hashCode(), error.getMessage(), error));
    }

    @Override
    public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Security Token Service", "Starting Security Structure Checks/Install");
        log.info("Creating security token defaults in a new session and transaction");

        // Use sessionFactory.withTransaction to create a new session
        return sessionFactory.withTransaction((newSession, tx) -> 
            // Get the ActivityMaster system
            systemsService.findSystem(newSession, enterprise, ActivityMasterSystemName)
                .onItem().invoke(activityMasterSystem -> {
                    log.debug("Found ActivityMaster system: '{}' with session: {}", 
                        activityMasterSystem.getName(), newSession.hashCode());
                })
                .onFailure().invoke(error -> log.error("Failed to find ActivityMaster system: {}", 
                    error.getMessage(), error))
                .chain(activityMasterSystem -> createSecurityDefaults(newSession, enterprise, activityMasterSystem))
        ).replaceWithVoid();
    }

    private Uni<Void> createSecurityDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        log.info("🔐 Creating security defaults for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());
        
        return sessionFactory.withTransaction((newSession, tx) ->
            createSecurityClassifications(newSession, enterprise, system)
                .chain(v -> createSecurityTokens(newSession, enterprise, system))
                .chain(rootToken -> createGroupsAndFolders(newSession, enterprise, rootToken, system))
                .chain(v -> applyDefaultsToNewEnterprise(newSession, enterprise, system))
                .chain(v -> createActivityMasterInvolvedParty(newSession, enterprise, system))
                .chain(v -> applyDefaultsToNewEnterpriseAfterActivityMaster(newSession, enterprise, system))
                .onItem().invoke(() -> log.info("✅ Security defaults created successfully for enterprise: '{}'", enterprise.getName()))
                .onFailure().invoke(error -> log.error("❌ Failed to create security defaults for enterprise '{}': {}", 
                    enterprise.getName(), error.getMessage(), error))
        ).replaceWithVoid();
    }

    private Uni<Void> createSecurityClassifications(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        log.info("🏷️ Creating security classifications for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());

        // Create the enterprise classification first
        return classificationService.create(session, enterprise.getName(), system)
            .onItem().invoke(entClassification -> 
                log.debug("✅ Created enterprise classification: '{}'", entClassification.getName()))
            .onFailure().invoke(error -> 
                log.error("❌ Failed to create enterprise classification: {}", error.getMessage(), error))
            .chain(entClassification -> {
                // Create all security-related classifications sequentially
                log.info("🏷️ Creating security classifications sequentially");

                // Chain all classification creation operations
                return classificationService.create(session,
                    SecurityTokenClassifications.UserGroup.toString(),
                    SecurityTokenClassifications.UserGroup.toString(), 
                    EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                    system, 1, entClassification)
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.User.toString(),
                        SecurityTokenClassifications.User.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 2))
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.Guests.toString(),
                        SecurityTokenClassifications.Guests.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 2))
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.Visitors.toString(),
                        SecurityTokenClassifications.Visitors.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 2))
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.Registered.toString(),
                        SecurityTokenClassifications.Registered.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 2))
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.Application.toString(),
                        SecurityTokenClassifications.Application.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 3))
                    .chain(v -> classificationService.create(session,
                        UserGroupSecurityTokenClassifications.System.toString(),
                        UserGroupSecurityTokenClassifications.System.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 4))
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.Plugin.toString(),
                        SecurityTokenClassifications.Plugin.toString(), 
                        EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, 
                        system, 5))
                    .chain(v -> classificationService.create(session,
                        SecurityTokenClassifications.Identity.toString(),
                        "A security token identity", 
                        EnterpriseClassificationDataConcepts.SecurityTokenXClassification, 
                        system, 1, entClassification))
                    .onItem().invoke(() -> {
                        log.info("✅ All security classifications created successfully");
                        logProgress("Security Token Service", "Security Classifications Installed", 11);
                    })
                    .onFailure().invoke(error -> 
                        log.error("❌ Error creating security classifications: {}", error.getMessage(), error));
            }).replaceWith(Uni.createFrom()
                                           .voidItem());
    }

    private Uni<SecurityToken> createSecurityTokens(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        log.info("🎫 Creating security tokens for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());

        // Prepare the description
        String description = enterprise.getDescription().isEmpty() 
            ? "An enterprise-wide project" 
            : enterprise.getDescription();

        // Create the root token
        return securityTokenService.create(session, enterprise.getName(),
                enterprise.getName(), description, system)
            .onItem().invoke(token -> 
                log.debug("✅ Created root token: '{}'", token.getName()))
            .onFailure().invoke(error -> 
                log.error("❌ Error creating root token: {}", error.getMessage(), error))
            .chain(rootToken -> {
                // Grant access to the token
                return securityTokenService.grantAccessToToken(session, rootToken, rootToken, 
                        false, false, false, false, system)
                    .onFailure().invoke(error -> 
                        log.error("❌ Error granting access to token: {}", error.getMessage(), error))
                    .map(v -> {
                        // Add classification to the enterprise
                        try {
                            enterprise.addOrUpdateClassification(session, 
                                EnterpriseClassifications.EnterpriseIdentity,
                                null,
                                rootToken.getSecurityToken(),
                                system);
                            log.debug("✅ Added classification to enterprise: {}", 
                                EnterpriseClassifications.EnterpriseIdentity);
                        } catch (Exception e) {
                            log.error("❌ Error adding classification to enterprise: {}", e.getMessage(), e);
                        }

                        logProgress("Security Token Service", "Enterprise Security Validated", 3);
                        log.info("✅ Successfully created security tokens for enterprise: '{}'", enterprise.getName());
                        return (SecurityToken) rootToken;
                    });
            });
    }

    private Uni<Void> createGroupsAndFolders(Mutiny.Session session, IEnterprise<?, ?> enterprise, SecurityToken rootToken, ISystems<?, ?> system)
    {
        log.info("👥 Creating groups and folders for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());

        // Create all security tokens first
        List<Uni<SecurityToken>> tokenCreations = new ArrayList<>();
        
        tokenCreations.add(securityTokenService.create(session, 
            SecurityTokenClassifications.UserGroup.toString(),
            UserGroupSecurityTokenClassifications.Everyone.toString(),
            UserGroupSecurityTokenClassifications.Everyone.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.UserGroup.toString(),
            UserGroupSecurityTokenClassifications.Everywhere.toString(),
            UserGroupSecurityTokenClassifications.Everywhere.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.UserGroup.toString(),
            UserGroupSecurityTokenClassifications.Administrators.toString(),
            UserGroupSecurityTokenClassifications.Administrators.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.UserGroup.toString(),
            SecurityTokenClassifications.Guests.toString(),
            SecurityTokenClassifications.Guests.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.UserGroup.toString(),
            SecurityTokenClassifications.Visitors.toString(),
            SecurityTokenClassifications.Visitors.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.UserGroup.toString(),
            SecurityTokenClassifications.Registered.toString(),
            SecurityTokenClassifications.Registered.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.Application.toString(),
            UserGroupSecurityTokenClassifications.Applications.toString(),
            UserGroupSecurityTokenClassifications.Applications.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            UserGroupSecurityTokenClassifications.System.toString(),
            UserGroupSecurityTokenClassifications.System.toString(),
            UserGroupSecurityTokenClassifications.System.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            SecurityTokenClassifications.Plugin.toString(),
            UserGroupSecurityTokenClassifications.Plugins.toString(),
            UserGroupSecurityTokenClassifications.Plugins.classificationDescription(),
            system).map(token -> (SecurityToken) token));
            
        tokenCreations.add(securityTokenService.create(session,
            UserGroupSecurityTokenClassifications.System.toString(),
            "Activity Master System", "Defines the activity master as a system", 
            system).map(token -> (SecurityToken) token));
        
        log.info("🚀 Creating {} security tokens sequentially", tokenCreations.size());
        
        // Create tokens sequentially
        return tokenCreations.get(0) // everyoneToken
            .chain(everyoneToken -> tokenCreations.get(1).map(everywhereToken -> new Object[]{everyoneToken, everywhereToken})) // everywhereToken
            .chain(tokens -> tokenCreations.get(2).map(administratorsToken -> new Object[]{tokens[0], tokens[1], administratorsToken})) // administratorsToken
            .chain(tokens -> tokenCreations.get(3).map(usersGuestsToken -> new Object[]{tokens[0], tokens[1], tokens[2], usersGuestsToken})) // usersGuestsToken
            .chain(tokens -> tokenCreations.get(4).map(usersGuestsVisitorsToken -> new Object[]{tokens[0], tokens[1], tokens[2], tokens[3], usersGuestsVisitorsToken})) // usersGuestsVisitorsToken
            .chain(tokens -> tokenCreations.get(5).map(usersGuestsRegisteredToken -> new Object[]{tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], usersGuestsRegisteredToken})) // usersGuestsRegisteredToken
            .chain(tokens -> tokenCreations.get(6).map(applicationToken -> new Object[]{tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], applicationToken})) // applicationToken
            .chain(tokens -> tokenCreations.get(7).map(systemsToken -> new Object[]{tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], systemsToken})) // systemsToken
            .chain(tokens -> tokenCreations.get(8).map(pluginToken -> new Object[]{tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], pluginToken})) // pluginToken
            .chain(tokens -> tokenCreations.get(9).map(activityMasterToken -> new Object[]{tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[8], activityMasterToken})) // activityMasterToken
            .chain(tokens -> {
                // Extract all tokens from the array
                SecurityToken everyoneToken = (SecurityToken) tokens[0];
                SecurityToken everywhereToken = (SecurityToken) tokens[1];
                SecurityToken administratorsToken = (SecurityToken) tokens[2];
                SecurityToken usersGuestsToken = (SecurityToken) tokens[3];
                SecurityToken usersGuestsVisitorsToken = (SecurityToken) tokens[4];
                SecurityToken usersGuestsRegisteredToken = (SecurityToken) tokens[5];
                SecurityToken applicationToken = (SecurityToken) tokens[6];
                SecurityToken systemsToken = (SecurityToken) tokens[7];
                SecurityToken pluginToken = (SecurityToken) tokens[8];
                SecurityToken activityMasterToken = (SecurityToken) tokens[9];

                logProgress("Security Token Service", "Base Security Tokens", 11);

                // Add classification to the activity master system
                return system.addOrReuseClassification(session, SystemsClassifications.SystemIdentity,
                        activityMasterToken.getSecurityToken(), system)
                    .onItem().invoke(result -> 
                        log.debug("✅ Added classification to activity master system: {}", SystemsClassifications.SystemIdentity))
                    .onFailure().invoke(error -> 
                        log.error("❌ Error adding classification to activity master system: {}", error.getMessage(), error))
                    .chain(v -> {
                        // Get all the required classifications sequentially
                        log.info("🔍 Retrieving classifications sequentially");
                        return classificationService.find(session, SecurityTokenClassifications.UserGroup, system)
                            .chain(userGroupClass -> 
                                classificationService.find(session, SecurityTokenClassifications.Application, system)
                                    .map(applicationClass -> new Object[]{userGroupClass, applicationClass}))
                            .chain(classes -> 
                                classificationService.find(session, UserGroupSecurityTokenClassifications.System, system)
                                    .map(systemClass -> new Object[]{classes[0], classes[1], systemClass}))
                            .chain(classes -> 
                                classificationService.find(session, SecurityTokenClassifications.Plugin, system)
                                    .map(pluginClass -> new Object[]{classes[0], classes[1], classes[2], pluginClass}))
                            .chain(classes -> {
                                IClassification<?, ?> userGroupClass = (IClassification<?, ?>) classes[0];
                                IClassification<?, ?> applicationClass = (IClassification<?, ?>) classes[1];
                                IClassification<?, ?> systemClass = (IClassification<?, ?>) classes[2];
                                IClassification<?, ?> pluginClass = (IClassification<?, ?>) classes[3];

                                // Now create all the links sequentially
                                log.info("🔗 Creating token links sequentially");
                                
                                // Link tokens with their classifications sequentially
                                return securityTokenService.link(session, rootToken, everyoneToken, userGroupClass)
                                    .chain(v1 -> securityTokenService.link(session, rootToken, everywhereToken, userGroupClass))
                                    .chain(v1 -> securityTokenService.link(session, everyoneToken, administratorsToken, userGroupClass))
                                    .chain(v1 -> securityTokenService.link(session, everyoneToken, usersGuestsToken, userGroupClass))
                                    .chain(v1 -> securityTokenService.link(session, usersGuestsToken, usersGuestsRegisteredToken, userGroupClass))
                                    .chain(v1 -> securityTokenService.link(session, usersGuestsToken, usersGuestsVisitorsToken, userGroupClass))
                                    .chain(v1 -> securityTokenService.link(session, rootToken, applicationToken, applicationClass))
                                    .chain(v1 -> securityTokenService.link(session, rootToken, systemsToken, systemClass))
                                    .chain(v1 -> securityTokenService.link(session, rootToken, pluginToken, pluginClass))
                                    .chain(v1 -> securityTokenService.link(session, systemsToken, activityMasterToken, systemClass))
                                    .onItem().invoke(() -> {
                                        log.info("✅ All token links created successfully");
                                        logProgress("Security Token Service", "Security Hierarchy Confirmed", 11);
                                    })
                                    .chain(v1 -> {
                                        // Now create all the access grants sequentially
                                        log.info("🔐 Creating access grants sequentially");
                                        
                                        return createAccessGrantsSequentially(session, 
                                            administratorsToken, everyoneToken, everywhereToken,
                                            usersGuestsToken, usersGuestsRegisteredToken, usersGuestsVisitorsToken,
                                            applicationToken, systemsToken, pluginToken, rootToken, system)
                                            .onItem().invoke(() -> {
                                                log.info("✅ All groups and folders created successfully");
                                                logProgress("Security Token Service", "Default Security Confirmed", 37);
                                            })
                                            .onFailure().invoke(error -> 
                                                log.error("❌ Error creating access grants: {}", error.getMessage(), error));
                                    });
                            });
                    });
            });
    }

    private Uni<Void> createAccessGrantsSequentially(Mutiny.Session session,
            SecurityToken administratorsToken, SecurityToken everyoneToken, SecurityToken everywhereToken,
            SecurityToken usersGuestsToken, SecurityToken usersGuestsRegisteredToken, SecurityToken usersGuestsVisitorsToken,
            SecurityToken applicationToken, SecurityToken systemsToken, SecurityToken pluginToken, 
            SecurityToken rootToken, ISystems<?, ?> system)
    {
        // Chain all access grant operations sequentially
        // Start with administratorsToken operations
        return securityTokenService.grantAccessToToken(session, administratorsToken, rootToken, true, true, true, true, system)
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, everyoneToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, administratorsToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, applicationToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, everywhereToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, usersGuestsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, systemsToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, administratorsToken, pluginToken, true, true, false, true, system))
            
            // usersGuestsToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, rootToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, everyoneToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, administratorsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, applicationToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, everywhereToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, usersGuestsToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, systemsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsToken, pluginToken, false, false, false, false, system))
            
            // usersGuestsRegisteredToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, rootToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, everyoneToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, administratorsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, applicationToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, everywhereToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, usersGuestsToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, systemsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsRegisteredToken, pluginToken, false, false, false, false, system))
            
            // usersGuestsVisitorsToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, rootToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, everyoneToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, administratorsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, applicationToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, everywhereToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, usersGuestsToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, systemsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, usersGuestsVisitorsToken, pluginToken, false, false, false, false, system))
            
            // everyoneToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, rootToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, everyoneToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, administratorsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, applicationToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, everywhereToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, systemsToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, pluginToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, usersGuestsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, usersGuestsRegisteredToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everyoneToken, usersGuestsVisitorsToken, false, false, false, false, system))
            
            // everywhereToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, rootToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, everyoneToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, administratorsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, applicationToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, systemsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, everywhereToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, pluginToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, usersGuestsToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, usersGuestsRegisteredToken, false, false, false, false, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, everywhereToken, usersGuestsVisitorsToken, false, false, false, false, system))
            
            // applicationToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, rootToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, everyoneToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, administratorsToken, true, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, applicationToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, everywhereToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, systemsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, pluginToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, usersGuestsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, usersGuestsRegisteredToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, applicationToken, usersGuestsVisitorsToken, true, true, true, true, system))
            
            // systemsToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, rootToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, everyoneToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, administratorsToken, true, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, applicationToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, everywhereToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, systemsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, pluginToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, usersGuestsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, usersGuestsRegisteredToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, systemsToken, usersGuestsVisitorsToken, true, true, true, true, system))
            
            // pluginToken operations
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, rootToken, false, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, everyoneToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, administratorsToken, true, false, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, applicationToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, everywhereToken, true, true, false, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, systemsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, pluginToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, usersGuestsToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, usersGuestsRegisteredToken, true, true, true, true, system))
            .chain(v -> securityTokenService.grantAccessToToken(session, pluginToken, usersGuestsVisitorsToken, true, true, true, true, system));
    }

    private Uni<Void> applyDefaultsToNewEnterprise(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        log.info("🏢 Applying reactive security defaults for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());
        
        logProgress("Security Token Service", "Checking Default Security for all enterprise default items");

        // Create sequential operations for all security table creation
        logProgress("Security Token Service", "Starting basic security checks", 1);
        return createDefaultSecurityForTableReactive(session, new ActiveFlag(), system)
            .chain(v -> {
                logProgress("Security Token Service", "Systems securities", 1);
                return createDefaultSecurityForTableReactive(session, new Systems(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "System Classifications securities", 1);
                return createDefaultSecurityForTableReactive(session, new SystemsXClassification(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Classification Data Concepts security checks", 1);
                return createDefaultSecurityForTableReactive(session, new ClassificationDataConcept(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Classifications checks", 1);
                return createDefaultSecurityForTableReactive(session, new Classification(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Hierarchy checks", 1);
                return createDefaultSecurityForTableReactive(session, new ClassificationXClassification(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Identification Type checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyIdentificationType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Name Types checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyNameType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Party Type checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Organic Types checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyOrganicType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Final Enterprise checks", 1);
                return createDefaultSecurityForTableReactive(session, new EnterpriseXClassification(), system);
            })
            .chain(v -> createDefaultSecurityForTableReactive(session, (WarehouseCoreTable<?, ?, ?, ?>) enterprise, system))
            .onItem().invoke(() -> {
                log.info("✅ All enterprise security defaults applied successfully");
                logProgress("Security Token Service", "Completed Checks", 1);
            })
            .onFailure().invoke(error -> {
                log.error("❌ Failed to apply enterprise security defaults: {}", error.getMessage(), error);
            });
    }

    private Uni<Void> createActivityMasterInvolvedParty(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        log.info("👤 Creating ActivityMaster involved party for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());
        
        return  IGuiceContext.get(SystemsSystem.class)
                .createInvolvedPartyForNewSystem(session, system)
                    .replaceWith(Uni.createFrom()
                                     .voidItem());
    }

    private Uni<Void> applyDefaultsToNewEnterpriseAfterActivityMaster(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
    {
        log.info("🏢 Applying reactive post-ActivityMaster security defaults for enterprise: '{}' with session: {}", 
            enterprise.getName(), session.hashCode());
        
        // Create sequential operations for all post-ActivityMaster security table creation
        logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1);
        return createDefaultSecurityForTableReactive(session, new InvolvedParty(), system)
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Organic Relationship checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyOrganic(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Non Organic checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyNonOrganic(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Relationship checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyXInvolvedPartyIdentificationType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Classifications Relationship checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyXClassification(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Name Type Relationship checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyXInvolvedPartyNameType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Involved Party Type Relationship checks", 1);
                return createDefaultSecurityForTableReactive(session, new InvolvedPartyXInvolvedPartyType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Events checks", 1);
                return createDefaultSecurityForTableReactive(session, new EventType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Resource Item Types checks", 1);
                return createDefaultSecurityForTableReactive(session, new ResourceItemType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Arrangement Types checks", 1);
                return createDefaultSecurityForTableReactive(session, new ArrangementType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting Arrangement checks", 1);
                return createDefaultSecurityForTableReactive(session, new Arrangement(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting ArrangementXType checks", 1);
                return createDefaultSecurityForTableReactive(session, new ArrangementXArrangementType(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting ArrangementXClassification checks", 1);
                return createDefaultSecurityForTableReactive(session, new ArrangementXClassification(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting ArrangementXResourceItem checks", 1);
                return createDefaultSecurityForTableReactive(session, new ArrangementXResourceItem(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting ArrangementXInvolvedParty checks", 1);
                return createDefaultSecurityForTableReactive(session, new ArrangementXInvolvedParty(), system);
            })
            .chain(v -> {
                logProgress("Security Token Service", "Starting ArrangementXProduct checks", 1);
                return createDefaultSecurityForTableReactive(session, new ArrangementXProduct(), system);
            })
            .onItem().invoke(() -> {
                log.info("✅ All post-ActivityMaster security defaults applied successfully");
            })
            .onFailure().invoke(error -> {
                log.error("❌ Failed to apply post-ActivityMaster security defaults: {}", error.getMessage(), error);
            });
    }

    private Uni<Void> createDefaultSecurityForTableReactive(Mutiny.Session session, WarehouseCoreTable<?, ?, ?, ?> table, ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        log.debug("🔐 Creating reactive security for table: {}", table.getClass().getSimpleName());
        
        return table.builder(session)
            //todo.withEnterprise(system.getEnterpriseID())
             // todo .whereNoSecurityIsApplied()
             .inDateRange()
             .getAll()
             .onItem().invoke(items -> log.debug("Found {} items without security for {}", items.size(), table.getClass().getSimpleName()))
             .chain(items -> {
                 // Check if there are any items to process
                 if (items.isEmpty()) {
                     log.debug("✅ No security operations needed for {}", table.getClass().getSimpleName());
                     return Uni.createFrom().voidItem();
                 }

                 log.debug("🚀 Executing {} security operations sequentially for {}", items.size(), table.getClass().getSimpleName());
                
                 // Start with an empty Uni that completes immediately
                 Uni<Void> chain = Uni.createFrom().voidItem();
                
                 // For each item, chain a security operation to run sequentially
                 for (Object next : items) {
                     WarehouseCoreTable<?, ?, ?, ?> tableItem = (WarehouseCoreTable<?, ?, ?, ?>) next;
                     chain = chain.chain(v -> {
                         logProgress("Security Token Service", "Checking - " + tableItem.getClass().getSimpleName(), 0);
                         // Execute security operation and chain to the next one
                         return tableItem.createDefaultSecurity(system, identityToken);
                     });
                 }
                
                 // Add logging for completion or failure
                 return chain
                     .onItem().invoke(() -> log.debug("✅ Completed security for {}", table.getClass().getSimpleName()))
                     .onFailure().invoke(error -> log.error("❌ Error creating default security for {}: {}", 
                                                          table.getClass().getSimpleName(), error.getMessage(), error));
             });
    }

    @Override
    public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("Starting reactive postStartup for Security Token System");

        // Create a reactive chain for the postStartup operations
        // Get the system
        return systemsService.findSystem(session, enterprise, getSystemName())
            .onItem().ifNull().failWith(() -> new RuntimeException("System not found: " + getSystemName()))
            .chain(system -> {
                log.debug("Found system: {}", system.getName());
                // Get the security token
                return systemsService.getSecurityIdentityToken(session, system)
                    .onItem().ifNull().failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
                    .map(token -> {
                        log.debug("Found security token for system: {}", system.getName());
                        return null; // Return Void
                    });
            })
            .replaceWith(Uni.createFrom().voidItem());
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

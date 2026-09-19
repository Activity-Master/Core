package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.MasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.*;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;


@Log4j2
public class SystemsSystem
        extends MasterDefaultSystem<SystemsSystem>
        implements IMasterSystem<SystemsSystem> {

    @Inject
    private ISystemsService<?> systemsService;

    /**
     * Creates all the applicable systems that are required before the actual activity master system can be created.
     * After the user groups setup then security controls kick in
     *
     * @param session
     * @param enterprise
     * @return
     */
    @Override
    public Uni<ISystems<?, ?>> registerSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        log.info("🚀 Registering Activity Master System for enterprise: '{}'", enterprise.getName());
        log.debug("📋 Creating Activity Master System with session: {}", session.hashCode());

        //Must register EnterpriseSystem, ActiveFlagSystem, and Activity Master System

        return systemsService
                .create(session, enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master")
                .onItem()
                .ifNull()
                .continueWith(() -> {
                    log.warn("Activity Master system creation returned null, trying to find it");
                    return null;
                })
                .chain(system -> {
                    if (system == null) {
                        return systemsService.findSystem(session, enterprise, ActivityMasterSystemName);
                    }
                    return Uni.createFrom().item(system);
                })
                .chain(system -> {
                    if (system == null) {
                        log.error("❌ Failed to resolve Activity Master system for enterprise: '{}'", enterprise.getName());
                        return Uni.createFrom().failure(new RuntimeException("Failed to resolve Activity Master system"));
                    }
                    log.debug("✅ Found/Created Activity Master System: '{}' with session: {}", system.getName(), session.hashCode());
                    return systemsService.registerNewSystem(session, enterprise, system)
                            .replaceWith(system);
                })
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create Activity Master System: '{}' with session {}: {}",
                        ActivityMasterSystemName, session.hashCode(), error.getMessage(), error))
                .map(result -> result);
    }

    /**
     * Stateless variant of {@link #createDefaults(Mutiny.StatelessSession, IEnterprise)} — creates the Enterprise,
     * Active Flag and Activity Master {@code Systems} rows on a {@link Mutiny.StatelessSession} via the
     * stateless find-or-create. Idempotent (existing systems are returned, not duplicated).
     */
    @Override
    public Uni<Void> createDefaults(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        log.info("🚀 (stateless) Registering default systems for enterprise: '{}'", enterprise.getName());
        return systemsService.create(session, enterprise, EnterpriseSystemName, "The system for handling enterprises")
                .flatMap(entSystem -> systemsService.create(session, enterprise, ActivateFlagSystemName, "The system for the active flag management"))
                .flatMap(flagSystem -> systemsService.create(session, enterprise, ActivityMasterSystemName,
                        "The Core Enterprise Activity Monitoring Application", "Activity Master"))
                .invoke(() -> log.info("🎉 (stateless) Successfully registered all systems for enterprise: '{}'", enterprise.getName()))
                .replaceWithVoid();
    }


    @Override
    public int totalTasks() {
        return 2;
    }

    /**
     * Stateless variant of {@link #createInvolvedPartyForNewSystem(Mutiny.StatelessSession, ISystems, UUID...)} — provisions
     * the system's {@code InvolvedParty} (UUID-identification party + identification/party/name-type links) entirely
     * on a {@link Mutiny.StatelessSession}. It composes the already-stateless pieces: the prepped
     * {@code getActivityMaster} / {@code getSecurityIdentityToken} reads, the stateless involved-party {@code create},
     * the prepped {@code findInvolvedParty*Type} finders and the stateless {@code addOrReuseInvolvedParty*} writers.
     */
    public Uni<IInvolvedParty<?, ?>> createInvolvedPartyForNewSystem(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        log.info("🚀 (stateless) Creating involved party for system: '{}'", system.getName());

        InvolvedPartyService ipService = com.guicedee.client.IGuiceContext.get(InvolvedPartyService.class);

        return systemsService
                .getActivityMaster(session, system.getEnterpriseID())
                .chain(activityMasterSystem -> systemsService
                        .getSecurityIdentityToken(session, activityMasterSystem)
                        .chain(activityMasterSystemUUID -> systemsService
                                .getSecurityIdentityToken(session, system, activityMasterSystemUUID)
                                .chain(newSystemUUID -> {
                                    if (newSystemUUID == null) {
                                        log.error("❌ (stateless) No UUID found for newly created system: '{}'", system.getName());
                                        return Uni.createFrom()
                                                .<IInvolvedParty<?, ?>>failure(new ActivityMasterException("No UUID for newly created system"));
                                    }
                                    return ipService.findByUUID(session, newSystemUUID, system, activityMasterSystemUUID)
                                                    .onFailure(jakarta.persistence.NoResultException.class)
                                                    .recoverWithUni(missing -> ipService.create(
                                                    session,
                                                    system,
                                                    Pair.of(IdentificationTypes.IdentificationTypeUUID.toString(), newSystemUUID.toString()),
                                                    false,
                                                    activityMasterSystemUUID))
                                            .chain(involvedParty ->
                                                    // 1. identification type
                                                    ipService.findInvolvedPartyIdentificationType(
                                                                    session,
                                                                    IdentificationTypes.IdentificationTypeSystemID.toString(),
                                                                    system,
                                                                    identityToken)
                                                            .chain(involvedPartyIdentificationType -> involvedParty.addOrReuseInvolvedPartyIdentificationType(
                                                                    session,
                                                                    NoClassification.toString(),
                                                                    involvedPartyIdentificationType,
                                                                    system.getId().toString(),
                                                                    system,
                                                                    activityMasterSystemUUID))
                                                            // 2. party type
                                                            .chain(v -> ipService.findType(
                                                                    session,
                                                                    IPTypes.TypeSystem.toString(),
                                                                    system,
                                                                    identityToken))
                                                            .chain(ipType -> involvedParty.addOrReuseInvolvedPartyType(
                                                                    session,
                                                                    NoClassification.toString(),
                                                                    ipType,
                                                                    newSystemUUID.toString(),
                                                                    system,
                                                                    activityMasterSystemUUID))
                                                            // 3. name type
                                                            .chain(v -> ipService.findInvolvedPartyNameType(
                                                                    session,
                                                                    NameTypes.PreferredNameType.toString(),
                                                                    system,
                                                                    identityToken))
                                                            .chain(nameType -> involvedParty.addOrReuseInvolvedPartyNameType(
                                                                    session,
                                                                    NoClassification.toString(),
                                                                    nameType,
                                                                    system.getName(),
                                                                    system,
                                                                    activityMasterSystemUUID))
                                                            .invoke(() -> log.info("🎉 (stateless) Successfully created involved party for system: '{}'", system.getName()))
                                                            .replaceWith((IInvolvedParty<?, ?>) involvedParty));
                                })))
                .onFailure()
                .invoke(error -> log.error("💥 (stateless) Failed to build InvolvedParty for system '{}': {}",
                        system.getName(), error.getMessage(), error));
    }

    @Override
    public Integer sortOrder() {
        return Integer.MIN_VALUE + 2;
    }

    @Override
    public String getSystemName() {
        return ActivityMasterSystemName;
    }

    @Override
    public String getSystemDescription() {
        return "The Core Enterprise Activity Monitoring Application";
    }
}

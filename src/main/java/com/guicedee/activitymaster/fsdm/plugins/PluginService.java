package com.guicedee.activitymaster.fsdm.plugins;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.plugins.PluginModels.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterPlugin;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

/** Stateless plugin catalogue and delegated system access, in the existing secured FSDM.
 * All mutations and invocation checks participate in the caller's transaction.
 * The host supplies verified identities; this service does not authenticate request-body IDs. */
public final class PluginService {
    static final String CATALOG = "Plugin Catalog";
    static final String INSTALLATION = "Plugin Installation";
    static final String DECLARATION = "Plugin System Declaration";
    static final String CONSENT = "Plugin User Consent";
    static final String POLICY = "Plugin System Policy";
    static final String INVOCATION = "Plugin Invocation";
    private static final UUID ZERO = new UUID(0, 0);
    private static final OffsetDateTime END = OffsetDateTime.parse("2999-12-31T23:59:59Z");

    @Inject private ISystemsService<?> systems;
    @Inject private ISecurityTokenService<?> security;
    @Inject private IActiveFlagService<?> flags;
    @Inject private IClassificationService<?> classes;
    @Inject private IArrangementsService<?> arrangements;
    @Inject private IEventService<?> events;

    private record Ctx(ISystems<?, ?> core, Identity identity, ISecurityToken<?, ?> credential,
                       ISecurityToken<?, ?> administrators, Set<UUID> tokens) { }
    private record State(UUID id, boolean enabled) { }

    /** Bootstrap-only taxonomy provisioning, called by the ordered update with the core credential. */
    public Uni<Void> installTaxonomy(Mutiny.StatelessSession session, ISystems<?, ?> core, UUID token) {
        Uni<?> chain = arrangements.createArrangementType(session, CATALOG, core, token);
        for (String type : List.of(INSTALLATION, DECLARATION, CONSENT, POLICY, INVOCATION))
            chain = chain.chain(() -> events.createEventType(session, type, core, token));
        Map<String, EnterpriseClassificationDataConcepts> roles = new LinkedHashMap<>();
        roles.put("PluginCatalogType", EnterpriseClassificationDataConcepts.ArrangementXArrangementType);
        for (String name : List.of("PluginTitle", "PluginVersion", "PluginIdentity"))
            roles.put(name, EnterpriseClassificationDataConcepts.ArrangementXClassification);
        for (String name : List.of("PluginIcon", "PluginScreenshot"))
            roles.put(name, EnterpriseClassificationDataConcepts.ArrangementXResourceItem);
        roles.put("PluginEventType", EnterpriseClassificationDataConcepts.EventXEventType);
        roles.put("PluginCatalog", EnterpriseClassificationDataConcepts.EventXArrangement);
        for (String name : List.of("PluginStateKey", "PluginEnabled", "PluginTargetSystem", "PluginOperation"))
            roles.put(name, EnterpriseClassificationDataConcepts.EventXClassification);
        for (String name : List.of("PluginActor", "PluginParty"))
            roles.put(name, EnterpriseClassificationDataConcepts.EventXInvolvedParty);
        for (var role : roles.entrySet())
            chain = chain.chain(() -> classes.create(session, role.getKey(), role.getKey(), role.getValue(), core, token));
        return chain.replaceWithVoid();
    }

    /** Administrator registration; creates a Plugin-typed identity and declares requested systems.
     * This grants neither party installation nor user consent. Re-registration updates metadata. */
    public Uni<Plugin> register(Mutiny.StatelessSession session, ISystems<?, ?> system,
                                Identity identity, Registration registration) {
        Objects.requireNonNull(registration, "registration");
        return context(session, system, identity).chain(c -> administrator(c)
                .chain(() -> session.createNativeQuery("select systemid from dbo.systems where systemid=:id for update", UUID.class)
                        .setParameter("id", c.core().getId()).getSingleResult())
                .chain(() -> validateResources(session, c, registration))
                .chain(() -> validateSystems(session, c, registration.systems()))
                .chain(() -> systems.create(session, c.core().getEnterprise(), registration.name(), registration.description(), identity.tokens()))
                .chain(plugin -> systems.registerNewPlugin(session, c.core().getEnterprise(), plugin)
                        .chain(credential -> catalogExists(session, c, plugin.getId())
                                .chain(exists -> exists ? Uni.createFrom().voidItem() : createCatalog(session, c, plugin.getId(), credential)))
                        .chain(() -> lockCatalog(session, c, plugin.getId(), true))
                        .chain(() -> metadata(session, c, plugin.getId(), registration))
                        .chain(() -> declarations(session, c, plugin.getId(), registration.systems()))
                        .replaceWith(new Plugin(plugin.getId(), registration.name(), registration.title(), registration.description(),
                                registration.version(), registration.icon(), registration.screenshots(), registration.systems()))));
    }

    /** Enterprise updater only: register a built-in extension using the live core bootstrap credential.
     * Retains the durable capability ID while retiring a legacy System credential and hierarchy links. */
    public Uni<Void> registerBuiltIn(Mutiny.StatelessSession session, ISystems<?, ?> core, UUID bootstrap,
                                     IMasterPlugin<?> extension) {
        if (extension instanceof com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem<?>
                || ISystemsService.ActivityMasterSystemName.equals(extension.getSystemName()))
            return Uni.createFrom().failure(new SecurityException("A plugin cannot be an ActivityMaster System"));
        if (session.currentTransaction() == null) throw new IllegalStateException("Plugin provisioning requires a transaction");
        return systems.getActivityMaster(session, core.getEnterprise()).chain(actual ->
                systems.getSecurityIdentityToken(session, actual).chain(expected -> {
                    if (!actual.getId().equals(core.getId()) || !expected.equals(bootstrap)) return denied();
                    Identity identity = new Identity(core.getId(), core.getEnterprise().getId(), bootstrap);
                    return security.getSecurityToken(session, bootstrap, core, bootstrap)
                            .chain(credential -> security.getAdministratorsFolder(session, core, bootstrap)
                                    .map(admin -> new Ctx(core, identity, credential, admin, Set.of())))
                            .chain(c -> systems.doesSystemExist(session, core.getEnterprise(), extension.getSystemName(), bootstrap)
                                    .chain(exists -> exists ? Uni.createFrom().voidItem()
                                            : extension.registerSystem(session, core.getEnterprise()).replaceWithVoid())
                                    .chain(() -> extension.getSystem(session, core.getEnterprise().getName()))
                                    .call(plugin -> core.getId().equals(plugin.getId())
                                            ? Uni.createFrom().failure(new SecurityException("Core registration cannot be converted to a plugin"))
                                            : Uni.createFrom().voidItem())
                                    .chain(plugin -> session.createNativeQuery("select systemid from dbo.systems where systemid=:id for update", UUID.class)
                                            .setParameter("id", plugin.getId()).getSingleResult()
                                            .chain(() -> retireLegacyIdentity(session, c, plugin.getId()))
                                            .chain(() -> systems.registerNewPlugin(session, core.getEnterprise(), plugin))
                                            .call(() -> flags.getActiveFlag(session, core.getEnterprise(), bootstrap)
                                                    .chain(flag -> {
                                                        // Stateless registration readers intentionally omit associations.
                                                        // Default grant writers require the writer's resolved ActiveFlag.
                                                        ((com.guicedee.activitymaster.fsdm.db.entities.systems.Systems) core)
                                                                .setActiveFlagID(flag);
                                                        return ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?>) plugin)
                                                                .createDefaultSecurity(session, core, bootstrap);
                                                    }))
                                            .chain(token -> catalogExists(session, c, plugin.getId())
                                                    .chain(exists -> exists ? Uni.createFrom().voidItem() : createCatalog(session, c, plugin.getId(), token)))
                                            .chain(() -> lockCatalog(session, c, plugin.getId(), true))
                                            .chain(() -> {
                                                Set<UUID> dependencies = new LinkedHashSet<>();
                                                Uni<Void> resolved = Uni.createFrom().voidItem();
                                                for (String name : extension.getPluginDependencies())
                                                    resolved = resolved.chain(() -> systems.findSystem(session, core.getEnterprise(), name)
                                                            .invoke(dependency -> dependencies.add(dependency.getId())).replaceWithVoid());
                                                return resolved.chain(() -> validateSystems(session, c, dependencies))
                                                        .chain(() -> resources(session, c, plugin.getId()))
                                                        .chain(media -> metadata(session, c, plugin.getId(), new Registration(extension.getSystemName(),
                                                                extension.getPluginTitle(), extension.getSystemDescription(), extension.getPluginVersion(),
                                                                media.get("PluginIcon").stream().findFirst().orElse(null),
                                                                media.get("PluginScreenshot"), dependencies)))
                                                        .chain(() -> declarations(session, c, plugin.getId(), dependencies));
                                            })));
                }));
    }

    private Uni<Void> retireLegacyIdentity(Mutiny.StatelessSession session, Ctx c, UUID plugin) {
        return session.createNativeQuery("select k.securitytokenid,k.securitytoken from dbo.systemxclassification x "
                        + "join classification.classification cx on cx.classificationid=x.classificationid "
                        + "join security.securitytoken k on k.securitytoken=x.value "
                        + "join classification.classification ck on ck.classificationid=k.securitytokenclassificationid "
                        + "where x.systemid=:plugin and cx.classificationname='SystemIdentity' and ck.classificationname='System' "
                        + "and " + live("x") + " and " + live("cx") + " and " + live("k") + " and " + live("ck"), Object[].class)
                .setParameter("plugin", plugin).setParameter("enterprise", c.identity().enterpriseId()).getResultList()
                .chain(previous -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (Object[] row : previous)
                        chain = chain.chain(() -> flags.getArchivedFlag(session, c.core().getEnterprise(), c.identity().tokens())
                                .chain(flag -> session.createNativeQuery("update security.securitytoken set activeflagid=:flag,"
                                                + "effectivetodate=statement_timestamp(),warehouselastupdatedtimestamp=statement_timestamp() "
                                                + "where securitytokenid=:id and enterpriseid=:enterprise")
                                        .setParameter("id", row[0]).setParameter("flag", flag.getId())
                                        .setParameter("enterprise", c.identity().enterpriseId()).executeUpdate()
                                        .chain(() -> session.createNativeQuery("update security.securitytokenxsecuritytoken set activeflagid=:flag,"
                                                        + "effectivetodate=statement_timestamp(),warehouselastupdatedtimestamp=statement_timestamp() "
                                                        + "where childsecuritytokenid=:id and enterpriseid=:enterprise")
                                                .setParameter("id", row[0]).setParameter("flag", flag.getId())
                                                .setParameter("enterprise", c.identity().enterpriseId()).executeUpdate())
                                        .chain(() -> session.createNativeQuery("update dbo.systemxclassification set activeflagid=:flag,"
                                                        + "effectivetodate=statement_timestamp(),warehouselastupdatedtimestamp=statement_timestamp() "
                                                        + "where systemid=:plugin and value=:token and enterpriseid=:enterprise")
                                                .setParameter("plugin", plugin).setParameter("token", row[1]).setParameter("flag", flag.getId())
                                                .setParameter("enterprise", c.identity().enterpriseId()).executeUpdate())).replaceWithVoid());
                    return chain;
                });
    }

    /** Every entry into a built-in plugin checks its installation and every declared dependency. */
    public Uni<Void> checkBuiltIn(Mutiny.StatelessSession session, ISystems<?, ?> plugin, Identity user, UUID party) {
        Invocation invocation = new Invocation(plugin.getId(), party);
        return context(session, plugin, user).chain(c -> lockCatalog(session, c, plugin.getId(), false)
                .chain(() -> declaredSystems(session, c, plugin.getId()))
                .chain(dependencies -> {
                    if (dependencies.isEmpty()) return denied();
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (UUID dependency : dependencies)
                        chain = chain.chain(() -> check(session,
                                new com.guicedee.activitymaster.fsdm.db.entities.systems.Systems().setId(dependency)
                                        .setEnterpriseID(plugin.getEnterprise()), user, invocation));
                    return chain;
                }));
    }

    public Uni<Plugin> find(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity, UUID plugin) {
        Objects.requireNonNull(plugin, "plugin");
        return context(session, system, identity).chain(c -> lockCatalog(session, c, plugin, false)
                .chain(() -> session.createNativeQuery("select systemname,systemdesc from dbo.systems where systemid=:id "
                                + "and " + live("dbo.systems"), Object[].class)
                        .setParameter("id", plugin).setParameter("enterprise", identity.enterpriseId()).getSingleResult())
                .chain(row -> catalogValues(session, c, plugin).chain(values -> resources(session, c, plugin)
                        .chain(media -> declaredSystems(session, c, plugin).map(declared -> new Plugin(plugin, (String) row[0],
                                values.get("PluginTitle"), (String) row[1], values.get("PluginVersion"),
                                media.get("PluginIcon").stream().findFirst().orElse(null), media.get("PluginScreenshot"), declared))))));
    }

    /** Installation management requires current write access to the installation party. */
    public Uni<Installation> install(Mutiny.StatelessSession session, ISystems<?, ?> system,
                                     Identity identity, UUID plugin, UUID party) {
        return installation(session, system, identity, plugin, party, true);
    }
    public Uni<Void> remove(Mutiny.StatelessSession session, ISystems<?, ?> system,
                             Identity identity, UUID plugin, UUID party) {
        return installation(session, system, identity, plugin, party, false).replaceWithVoid();
    }
    private Uni<Installation> installation(Mutiny.StatelessSession session, ISystems<?, ?> system,
                                           Identity identity, UUID plugin, UUID party, boolean enabled) {
        Objects.requireNonNull(party, "party");
        return context(session, system, identity).chain(c -> party(session, c, party, true)
                .chain(() -> lockCatalog(session, c, plugin, true))
                .chain(() -> writeState(session, c, plugin, INSTALLATION, key(plugin, party), enabled, null, party))
                .map(id -> new Installation(id, plugin, party)));
    }

    /** Authenticated user opt-in for exactly one system and current installation/declaration.
     * False withdraws consent. An installation for an organisation never consents for its members. */
    public Uni<Void> consent(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity,
                             Invocation invocation, UUID targetSystem, boolean enabled) {
        Objects.requireNonNull(invocation, "invocation");
        Objects.requireNonNull(targetSystem, "targetSystem");
        return context(session, system, identity).chain(c -> lockCatalog(session, c, invocation.pluginId(), true)
                .chain(() -> party(session, c, invocation.installationPartyId(), false))
                .chain(() -> requireState(session, c, INSTALLATION, key(invocation.pluginId(), invocation.installationPartyId())))
                .chain(installation -> requireState(session, c, DECLARATION, key(invocation.pluginId(), targetSystem))
                        .chain(declaration -> (enabled ? permitted(session, c, invocation, targetSystem) : Uni.createFrom().voidItem())
                                .chain(() -> writeState(session, c, invocation.pluginId(), CONSENT,
                                        key(installation.id(), declaration.id(), identity.partyId()), enabled, targetSystem,
                                        invocation.installationPartyId())))).replaceWithVoid());
    }

    /** Administrator denial at enterprise (party=null) or installation-party level. */
    public Uni<Void> setSystemAccess(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity,
                                     UUID plugin, UUID targetSystem, UUID installationParty, boolean enabled) {
        Objects.requireNonNull(targetSystem, "targetSystem");
        return context(session, system, identity).chain(c -> administrator(c)
                .chain(() -> lockCatalog(session, c, plugin, true))
                .chain(() -> validateSystems(session, c, Set.of(targetSystem)))
                .chain(() -> installationParty == null ? Uni.createFrom().voidItem() : liveParty(session, c, installationParty))
                .chain(() -> writeState(session, c, plugin, POLICY, key(plugin, targetSystem,
                        installationParty == null ? ZERO : installationParty), enabled, targetSystem, installationParty))
                .replaceWithVoid());
    }

    /** Call before domain work on the same session. Domain services MUST additionally check user row permissions.
     * The shared catalogue lock orders revocation against all calls for this plugin until commit. */
    public Uni<Void> check(Mutiny.StatelessSession session, ISystems<?, ?> targetSystem,
                           Identity identity, Invocation invocation) {
        Objects.requireNonNull(invocation, "invocation");
        return context(session, targetSystem, identity).chain(c -> lockCatalog(session, c, invocation.pluginId(), false)
                .chain(() -> validateSystems(session, c, Set.of(targetSystem.getId())))
                .chain(() -> party(session, c, invocation.installationPartyId(), false))
                .chain(() -> requireState(session, c, INSTALLATION, key(invocation.pluginId(), invocation.installationPartyId())))
                .chain(installation -> requireState(session, c, DECLARATION, key(invocation.pluginId(), targetSystem.getId()))
                        .chain(declaration -> permitted(session, c, invocation, targetSystem.getId())
                                .chain(() -> requireState(session, c, CONSENT,
                                        key(installation.id(), declaration.id(), identity.partyId())))
                                .chain(consent -> new Event().setId(consent.id()).canRead(session, c.core(), identity.tokens()))
                                .chain(readable -> readable ? Uni.createFrom().voidItem() : denied()))));
    }

    /** Reusable delegation boundary for other Masters. The callback receives the same verified user.
     * Domain permissions are still mandatory inside the callback. Null operation denotes a read;
     * a non-null operation records an initiation audit after successful work, before commit. */
    public <T> Uni<T> execute(Mutiny.StatelessSession session, ISystems<?, ?> targetSystem, Identity identity,
                              Invocation invocation, String operation, java.util.function.Function<Identity, Uni<T>> work) {
        Objects.requireNonNull(work, "work");
        return check(session, targetSystem, identity, invocation)
                .chain(() -> work.apply(identity))
                .call(_ -> operation == null ? Uni.createFrom().voidItem()
                        : audit(session, targetSystem, identity, invocation, operation));
    }

    /** Durable initiation audit. The writing system owns this Event's source ID; the plugin is a relationship. */
    public Uni<Void> audit(Mutiny.StatelessSession session, ISystems<?, ?> targetSystem, Identity identity,
                           Invocation invocation, String operation) {
        if (operation == null || operation.isBlank() || operation.length() > 150)
            throw new IllegalArgumentException("Plugin operation required (maximum 150 characters)");
        return check(session, targetSystem, identity, invocation)
                .chain(() -> context(session, targetSystem, identity))
                .chain(c -> newEvent(session, c, invocation.pluginId(), INVOCATION, null, true,
                        targetSystem.getId(), invocation.installationPartyId(), targetSystem.getId())
                        .chain(event -> eventValue(session, c, event, "PluginOperation", operation)));
    }

    private Uni<Ctx> context(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity) {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(identity, "identity");
        if (session.currentTransaction() == null) throw new IllegalStateException("Plugin operations require a caller-owned transaction");
        if (system == null || system.getEnterprise() == null || !identity.enterpriseId().equals(system.getEnterprise().getId()))
            return denied();
        // A library/system/plugin credential cannot be substituted for an authenticated user.
        return session.createNativeQuery("select 1 from security.securitytoken k "
                        + "join classification.classification c on c.classificationid=k.securitytokenclassificationid "
                        + "join party.involvedpartyorganic p on p.involvedpartyorganicid=:actor "
                        + "where k.securitytoken=:credential and c.classificationname in ('User','Identity') "
                        + "and " + live("k") + " and " + live("c") + " and " + live("p"), Integer.class)
                .setParameter("actor", identity.partyId()).setParameter("credential", identity.identityToken().toString())
                .setParameter("enterprise", identity.enterpriseId()).setMaxResults(1).getResultList()
                .chain(rows -> rows.isEmpty() ? PluginService.<ISystems<?, ?>>denied()
                        : systems.getActivityMaster(session, system.getEnterprise(), identity.tokens()))
                .chain(core -> security.getSecurityToken(session, identity.identityToken(), core, identity.tokens())
                        .chain(credential -> security.getAdministratorsFolder(session, core, identity.tokens())
                                .chain(admin -> security.getApplicableSecurityTokenIds(session, core, identity.tokens())
                                        .map(tokens -> new Ctx(core, identity, credential, admin, Set.copyOf(tokens))))));
    }

    private Uni<Void> administrator(Ctx c) {
        return c.tokens().contains(c.administrators().getId()) ? Uni.createFrom().voidItem() : denied();
    }
    private Uni<Void> liveParty(Mutiny.StatelessSession session, Ctx c, UUID party) {
        return session.createNativeQuery("select 1 from party.involvedparty p where p.involvedpartyid=:id and " + live("p"), Integer.class)
                .setParameter("id", party).setParameter("enterprise", c.identity().enterpriseId()).setMaxResults(1).getResultList()
                .chain(rows -> rows.isEmpty() ? denied() : Uni.createFrom().voidItem());
    }
    private Uni<Void> party(Mutiny.StatelessSession session, Ctx c, UUID id, boolean write) {
        return liveParty(session, c, id).chain(() -> {
            InvolvedParty party = new InvolvedParty().setId(id);
            return (write ? party.canWrite(session, c.core(), c.identity().tokens())
                          : party.canRead(session, c.core(), c.identity().tokens()))
                    .chain(allowed -> allowed ? Uni.createFrom().voidItem() : denied());
        });
    }
    private Uni<Void> validateSystems(Mutiny.StatelessSession session, Ctx c, Set<UUID> targets) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (UUID id : targets) {
            chain = chain.chain(() -> session.createNativeQuery("select 1 from dbo.systems s where s.systemid=:id and " + live("s")
                            + " and exists(select 1 from dbo.systemxclassification x "
                            + "join classification.classification c on c.classificationid=x.classificationid "
                            + "join security.securitytoken k on k.securitytoken=x.value "
                            + "join classification.classification kt on kt.classificationid=k.securitytokenclassificationid "
                            + "where x.systemid=s.systemid and c.classificationname='SystemIdentity' and kt.classificationname in ('System','Plugin') "
                            + "and " + live("x") + " and " + live("c") + " and " + live("k") + " and " + live("kt") + ")", Integer.class)
                    .setParameter("id", id).setParameter("enterprise", c.identity().enterpriseId()).setMaxResults(1).getResultList()
                    .chain(rows -> rows.isEmpty() ? denied() : Uni.createFrom().voidItem()));
        }
        return chain;
    }
    private Uni<Void> validateResources(Mutiny.StatelessSession session, Ctx c, Registration registration) {
        Set<UUID> resources = new LinkedHashSet<>(registration.screenshots());
        if (registration.icon() != null) resources.add(registration.icon());
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (UUID resource : resources)
            chain = chain.chain(() -> session.createNativeQuery("select 1 from resource.resourceitem r where r.resourceitemid=:id and " + live("r"), Integer.class)
                    .setParameter("id", resource).setParameter("enterprise", c.identity().enterpriseId()).setMaxResults(1).getResultList()
                    .chain(rows -> rows.isEmpty() ? denied() : new ResourceItem().setId(resource)
                            .canRead(session, c.core(), c.identity().tokens()).chain(readable -> readable ? Uni.createFrom().voidItem() : denied())));
        return chain;
    }

    private Uni<Boolean> catalogExists(Mutiny.StatelessSession session, Ctx c, UUID plugin) {
        return session.createNativeQuery("select 1 from arrangement.arrangement a where a.arrangementid=:id and " + live("a"), Integer.class)
                .setParameter("id", plugin).setParameter("enterprise", c.identity().enterpriseId()).setMaxResults(1).getResultList().map(rows -> !rows.isEmpty());
    }
    private Uni<Void> createCatalog(Mutiny.StatelessSession session, Ctx c, UUID plugin, String credential) {
        Arrangement row = new Arrangement().setId(plugin);
        return store(session, c, row, null).chain(() -> definition(session, c, "arrangement.arrangementtype", "arrangementtypeid", "arrangementtypename", CATALOG))
                .chain(type -> role(session, c, "PluginCatalogType", "ArrangementXArrangementType")
                        .chain(classification -> store(session, c, valued(new ArrangementXArrangementType().setArrangement(row)
                                .setType(new ArrangementType().setId(type)).setClassificationID(classification), "1"), null)))
                .chain(() -> catalogValue(session, c, row, "PluginIdentity", credential));
    }
    private Uni<Void> lockCatalog(Mutiny.StatelessSession session, Ctx c, UUID plugin, boolean write) {
        Objects.requireNonNull(plugin, "plugin");
        return session.createNativeQuery("""
                select a.arrangementid from arrangement.arrangement a
                join dbo.systems s on s.systemid=a.arrangementid
                join arrangement.arrangementxarrangementtype x on x.arrangementid=a.arrangementid
                join arrangement.arrangementtype t on t.arrangementtypeid=x.arrangementtypeid
                join arrangement.arrangementxclassification i on i.arrangementid=a.arrangementid
                join classification.classification ci on ci.classificationid=i.classificationid
                join security.securitytoken k on k.securitytoken=i.value
                join classification.classification ck on ck.classificationid=k.securitytokenclassificationid
                join security.securitytokenxsecuritytoken h on h.childsecuritytokenid=k.securitytokenid
                join security.securitytoken f on f.securitytokenid=h.parentsecuritytokenid
                where a.arrangementid=:id and a.systemid=:core and x.systemid=:core and t.systemid=:core
                  and i.systemid=:core and ci.systemid=:core and ci.classificationname='PluginIdentity'
                  and t.arrangementtypename='Plugin Catalog' and ck.classificationname='Plugin'
                  and f.securitytokenfriendlyname='Plugins'
                """ + " and " + live("a") + " and " + live("s") + " and " + live("x") + " and " + live("t")
                + " and " + live("i") + " and " + live("ci") + " and " + live("k") + " and " + live("ck")
                + " and " + live("h") + " and " + live("f") + (write ? " for update of a" : " for share of a"), UUID.class)
                .setParameter("id", plugin).setParameter("core", c.core().getId()).setParameter("enterprise", c.identity().enterpriseId())
                .setMaxResults(2).getResultList().chain(rows -> rows.size() == 1 ? Uni.createFrom().voidItem() : denied());
    }

    private Uni<Void> metadata(Mutiny.StatelessSession session, Ctx c, UUID plugin, Registration registration) {
        return flags.getArchivedFlag(session, c.core().getEnterprise(), c.identity().tokens()).chain(flag ->
                session.createNativeQuery("update arrangement.arrangementxclassification set activeflagid=:flag,effectivetodate=statement_timestamp(),"
                                + "warehouselastupdatedtimestamp=statement_timestamp() where arrangementid=:id and " + live("arrangement.arrangementxclassification")
                                + " and classificationid in (select classificationid from classification.classification where systemid=:core "
                                + "and classificationname in ('PluginTitle','PluginVersion'))")
                        .setParameter("id", plugin).setParameter("core", c.core().getId()).setParameter("enterprise", c.identity().enterpriseId())
                        .setParameter("flag", flag.getId()).executeUpdate()
                        .chain(() -> session.createNativeQuery("update arrangement.arrangementxresourceitem set activeflagid=:flag,effectivetodate=statement_timestamp(),"
                                        + "warehouselastupdatedtimestamp=statement_timestamp() where arrangementid=:id and " + live("arrangement.arrangementxresourceitem")
                                        + " and classificationid in (select classificationid from classification.classification where systemid=:core "
                                        + "and classificationname in ('PluginIcon','PluginScreenshot'))")
                                .setParameter("id", plugin).setParameter("flag", flag.getId()).setParameter("core", c.core().getId())
                                .setParameter("enterprise", c.identity().enterpriseId()).executeUpdate()))
                .chain(() -> session.createNativeQuery("update dbo.systems set systemdesc=:description,warehouselastupdatedtimestamp=statement_timestamp() "
                                + "where systemid=:id and enterpriseid=:enterprise")
                        .setParameter("description", registration.description()).setParameter("id", plugin).setParameter("enterprise", c.identity().enterpriseId()).executeUpdate())
                .chain(() -> catalogValue(session, c, new Arrangement().setId(plugin), "PluginTitle", registration.title()))
                .chain(() -> catalogValue(session, c, new Arrangement().setId(plugin), "PluginVersion", registration.version()))
                .chain(() -> media(session, c, plugin, "PluginIcon", registration.icon(), 0))
                .chain(() -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (int i = 0; i < registration.screenshots().size(); i++) {
                        int index = i;
                        chain = chain.chain(() -> media(session, c, plugin, "PluginScreenshot", registration.screenshots().get(index), index));
                    }
                    return chain;
                });
    }
    private Uni<Void> media(Mutiny.StatelessSession session, Ctx c, UUID plugin, String name, UUID resource, int index) {
        if (resource == null) return Uni.createFrom().voidItem();
        return role(session, c, name, "ArrangementXResourceItem").chain(classification -> store(session, c,
                valued(new ArrangementXResourceItem().setArrangementID(new Arrangement().setId(plugin))
                        .setResourceItemID(new ResourceItem().setId(resource)).setClassificationID(classification), Integer.toString(index)), null));
    }
    private Uni<Void> catalogValue(Mutiny.StatelessSession session, Ctx c, Arrangement catalog, String name, String value) {
        return role(session, c, name, "ArrangementXClassification").chain(classification -> store(session, c,
                valued(new ArrangementXClassification().setArrangementID(catalog).setClassificationID(classification), value), null));
    }
    private Uni<Map<String,String>> catalogValues(Mutiny.StatelessSession session, Ctx c, UUID plugin) {
        return session.createNativeQuery("select c.classificationname,x.value from arrangement.arrangementxclassification x "
                        + "join classification.classification c on c.classificationid=x.classificationid where x.arrangementid=:id "
                        + "and x.systemid=:core and c.systemid=:core and " + live("x") + " and " + live("c"), Object[].class)
                .setParameter("id", plugin).setParameter("core", c.core().getId()).setParameter("enterprise", c.identity().enterpriseId())
                .getResultList().map(rows -> { Map<String,String> result = new HashMap<>();
                    for (Object[] row : rows) result.put((String)row[0], (String)row[1]); return result; });
    }
    private Uni<Map<String,List<UUID>>> resources(Mutiny.StatelessSession session, Ctx c, UUID plugin) {
        return session.createNativeQuery("select c.classificationname,x.resourceitemid from arrangement.arrangementxresourceitem x "
                        + "join classification.classification c on c.classificationid=x.classificationid "
                        + "join resource.resourceitem r on r.resourceitemid=x.resourceitemid where x.arrangementid=:id "
                        + "and x.systemid=:core and c.systemid=:core and c.classificationname in ('PluginIcon','PluginScreenshot') and "
                        + live("x") + " and " + live("c") + " and " + live("r")
                        + " order by cast(x.value as integer)", Object[].class)
                .setParameter("id", plugin).setParameter("core", c.core().getId()).setParameter("enterprise", c.identity().enterpriseId())
                .getResultList().map(rows -> { Map<String,List<UUID>> result = new HashMap<>();
                    result.put("PluginIcon", new ArrayList<>()); result.put("PluginScreenshot", new ArrayList<>());
                    for (Object[] row : rows) result.get((String)row[0]).add((UUID)row[1]); return result; });
    }
    private Uni<Set<UUID>> declaredSystems(Mutiny.StatelessSession session, Ctx c, UUID plugin) {
        return session.createNativeQuery("select v.value" + stateFrom()
                        + " join event.eventxclassification v on v.eventid=e.eventid "
                        + "join classification.classification cv on cv.classificationid=v.classificationid "
                        + "join event.eventxarrangement a on a.eventid=e.eventid where a.arrangementid=:plugin "
                        + "and t.eventtypename=:type and enabled.value='1' and cv.classificationname='PluginTargetSystem' "
                        + "and e.systemid=:core and " + stateLive() + " and " + live("v") + " and " + live("cv") + " and " + live("a"), String.class)
                .setParameter("plugin", plugin).setParameter("type", DECLARATION).setParameter("core", c.core().getId())
                .setParameter("enterprise", c.identity().enterpriseId()).getResultList()
                .map(rows -> { Set<UUID> result = new HashSet<>(); rows.forEach(value -> result.add(UUID.fromString(value))); return Set.copyOf(result); });
    }
    private Uni<Void> declarations(Mutiny.StatelessSession session, Ctx c, UUID plugin, Set<UUID> requested) {
        return declaredSystems(session, c, plugin).chain(previous -> {
            Set<UUID> targets = new LinkedHashSet<>(previous); targets.addAll(requested);
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (UUID target : targets)
                chain = chain.chain(() -> writeState(session, c, plugin, DECLARATION, key(plugin, target),
                        requested.contains(target), target, null).replaceWithVoid());
            return chain;
        });
    }

    private Uni<Void> permitted(Mutiny.StatelessSession session, Ctx c, Invocation invocation, UUID system) {
        return state(session, c, POLICY, key(invocation.pluginId(), system, ZERO))
                .chain(global -> global != null && !global.enabled() ? denied()
                        : state(session, c, POLICY, key(invocation.pluginId(), system, invocation.installationPartyId()))
                                .chain(local -> local != null && !local.enabled() ? denied() : Uni.createFrom().voidItem()));
    }
    private Uni<State> requireState(Mutiny.StatelessSession session, Ctx c, String type, String key) {
        return state(session, c, type, key).chain(state -> state == null || !state.enabled() ? denied() : Uni.createFrom().item(state));
    }
    private Uni<State> state(Mutiny.StatelessSession session, Ctx c, String type, String key) {
        return session.createNativeQuery("select e.eventid,enabled.value" + stateFrom()
                        + " where t.eventtypename=:type and k.value=:key and e.systemid=:core and " + stateLive(), Object[].class)
                .setParameter("type", type).setParameter("key", key).setParameter("core", c.core().getId())
                .setParameter("enterprise", c.identity().enterpriseId()).setMaxResults(2).getResultList()
                .map(rows -> {
                    if (rows.size() > 1) throw new SecurityException("Ambiguous plugin authority");
                    return rows.isEmpty() ? null : new State((UUID) rows.getFirst()[0], "1".equals(rows.getFirst()[1]));
                });
    }
    private static String stateFrom() {
        return " from event.event e join event.eventxeventtype xt on xt.eventid=e.eventid "
                + "join event.eventtype t on t.eventtypeid=xt.eventtypeid "
                + "join event.eventxclassification k on k.eventid=e.eventid "
                + "join classification.classification ck on ck.classificationid=k.classificationid "
                + "join event.eventxclassification enabled on enabled.eventid=e.eventid "
                + "join classification.classification ce on ce.classificationid=enabled.classificationid ";
    }
    private static String stateLive() {
        return "ck.classificationname='PluginStateKey' and ce.classificationname='PluginEnabled' "
                + "and xt.systemid=:core and t.systemid=:core and k.systemid=:core and ck.systemid=:core "
                + "and enabled.systemid=:core and ce.systemid=:core "
                + "and " + live("e") + " and " + live("xt") + " and " + live("t") + " and " + live("k")
                + " and " + live("ck") + " and " + live("enabled") + " and " + live("ce");
    }
    private Uni<UUID> writeState(Mutiny.StatelessSession session, Ctx c, UUID plugin, String type,
                                 String key, boolean enabled, UUID target, UUID party) {
        return state(session, c, type, key).chain(previous -> {
            if (previous != null && previous.enabled() == enabled && !CONSENT.equals(type)) return Uni.createFrom().item(previous.id());
            Uni<Void> retired = previous == null ? Uni.createFrom().voidItem()
                    : flags.getArchivedFlag(session, c.core().getEnterprise(), c.identity().tokens())
                            .chain(flag -> session.createNativeQuery("update event.event set activeflagid=:flag,"
                                            + "effectivetodate=statement_timestamp(),warehouselastupdatedtimestamp=statement_timestamp() "
                                            + "where eventid=:id and enterpriseid=:enterprise")
                                    .setParameter("flag", flag.getId()).setParameter("id", previous.id())
                                    .setParameter("enterprise", c.identity().enterpriseId()).executeUpdate()).replaceWithVoid();
            return retired.chain(() -> newEvent(session, c, plugin, type, key, enabled, target, party, c.core().getId()))
                    .map(Event::getId);
        });
    }
    private Uni<Event> newEvent(Mutiny.StatelessSession session, Ctx c, UUID plugin, String type, String key,
                                boolean enabled, UUID target, UUID party, UUID writer) {
        Event event = new Event();
        event.setOriginalSourceSystemID(writer);
        return store(session, c, event, null)
                .chain(() -> definition(session, c, "event.eventtype", "eventtypeid", "eventtypename", type))
                .chain(typeId -> role(session, c, "PluginEventType", "EventXEventType")
                        .chain(classification -> store(session, c, valued(new EventXEventType().setEventID(event)
                                .setEventTypeID(new EventType().setId(typeId)).setClassificationID(classification), "1"), null)))
                .chain(() -> role(session, c, "PluginCatalog", "EventXArrangement")
                        .chain(classification -> store(session, c, valued(new EventXArrangement().setEventID(event)
                                .setArrangementID(new Arrangement().setId(plugin)).setClassificationID(classification), "1"), null)))
                .chain(() -> key == null ? Uni.createFrom().voidItem() : eventValue(session, c, event, "PluginStateKey", key))
                .chain(() -> eventValue(session, c, event, "PluginEnabled", enabled ? "1" : "0"))
                .chain(() -> target == null ? Uni.createFrom().voidItem() : eventValue(session, c, event, "PluginTargetSystem", target.toString()))
                .chain(() -> participant(session, c, event, "PluginActor", c.identity().partyId()))
                .chain(() -> party == null ? Uni.createFrom().voidItem() : participant(session, c, event, "PluginParty", party))
                .replaceWith(event);
    }
    private Uni<Void> eventValue(Mutiny.StatelessSession session, Ctx c, Event event, String name, String value) {
        return role(session, c, name, "EventXClassification").chain(classification -> store(session, c,
                valued(new EventXClassification().setEventID(event).setClassificationID(classification), value), null));
    }
    private Uni<Void> participant(Mutiny.StatelessSession session, Ctx c, Event event, String name, UUID party) {
        return role(session, c, name, "EventXInvolvedParty").chain(classification -> store(session, c,
                valued(new EventXInvolvedParty().setEventID(event).setInvolvedPartyID(new InvolvedParty().setId(party))
                        .setClassificationID(classification), "1"), null));
    }
    private Uni<Classification> role(Mutiny.StatelessSession session, Ctx c, String name, String concept) {
        return session.createNativeQuery("select c.classificationid from classification.classification c "
                        + "join classification.classificationdataconcept d on d.classificationdataconceptid=c.classificationdataconceptid "
                        + "where c.systemid=:core and c.classificationname=:name and d.classificationdataconceptname=:concept "
                        + "and " + live("c") + " and " + live("d"), UUID.class)
                .setParameter("core", c.core().getId()).setParameter("name", name).setParameter("concept", concept)
                .setParameter("enterprise", c.identity().enterpriseId()).setMaxResults(2).getResultList()
                .map(rows -> { if (rows.size() != 1) throw new IllegalStateException("Plugin taxonomy unavailable: " + name);
                    return new Classification().setId(rows.getFirst()); });
    }
    private Uni<UUID> definition(Mutiny.StatelessSession session, Ctx c, String table, String id, String column, String name) {
        return session.createNativeQuery("select " + id + " from " + table + " d where d.systemid=:core and "
                        + column + "=:name and " + live("d"), UUID.class)
                .setParameter("core", c.core().getId()).setParameter("name", name).setParameter("enterprise", c.identity().enterpriseId())
                .setMaxResults(2).getResultList().map(rows -> {
                    if (rows.size() != 1) throw new IllegalStateException("Plugin taxonomy unavailable: " + name);
                    return rows.getFirst(); });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Uni<Void> store(Mutiny.StatelessSession session, Ctx c, WarehouseSCDTable row, UUID predecessor) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (row.getId() == null) row.setId(UUID.randomUUID());
        row.setEnterpriseID(c.core().getEnterprise());
        row.setSystemID(c.core());
        if (row.getOriginalSourceSystemID() == null || ZERO.equals(row.getOriginalSourceSystemID()))
            row.setOriginalSourceSystemID(c.core().getId());
        row.setOriginalSourceSystemUniqueID(predecessor == null ? ZERO : predecessor);
        row.setWarehouseCreatedTimestamp(now); row.setWarehouseLastUpdatedTimestamp(now);
        row.setEffectiveFromDate(now); row.setEffectiveToDate(END); row.setWarehouseFromDate(now.toLocalDate());
        return flags.getActiveFlag(session, c.core().getEnterprise(), c.identity().tokens()).chain(flag -> {
            row.setActiveFlagID(flag);
            return session.insert(row)
                    .chain(() -> row.createSecurityGrant(session, c.core(), c.core().getEnterprise(), flag, c.administrators(),
                            true, true, true, true, c.identity().tokens()))
                    .chain(() -> row.createSecurityGrant(session, c.core(), c.core().getEnterprise(), flag, c.credential(),
                            false, false, false, true, c.identity().tokens())).replaceWithVoid();
        });
    }
    private static <T extends WarehouseRelationshipTable<?, ?, ?, ?, ?, ?>> T valued(T row, String value) {
        row.setValue(value);
        return row;
    }
    private static String key(UUID... ids) {
        return Arrays.stream(ids).map(UUID::toString).collect(java.util.stream.Collectors.joining(":"));
    }
    private static String live(String alias) {
        return alias + ".enterpriseid=:enterprise and " + alias + ".effectivefromdate<=statement_timestamp() and "
                + alias + ".effectivetodate>statement_timestamp() and exists(select 1 from dbo.activeflag f where "
                + "f.activeflagid=" + alias + ".activeflagid and f.enterpriseid=:enterprise and f.allowaccess=1)";
    }
    private static <T> Uni<T> denied() { return Uni.createFrom().failure(new SecurityException("Plugin access denied")); }
}

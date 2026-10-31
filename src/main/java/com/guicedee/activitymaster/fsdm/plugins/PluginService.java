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
    @Inject private AgeRestrictionService ages;

    private interface ActorContext {
        ISystems<?, ?> core();
        Identity identity();
    }
    private record ReadContext(ISystems<?, ?> core, Identity identity) implements ActorContext { }
    private record Ctx(ISystems<?, ?> core, Identity identity, ISecurityToken<?, ?> credential,
                       ISecurityToken<?, ?> administrators, Set<UUID> tokens) implements ActorContext { }
    private record State(UUID id, boolean enabled) { }

    /** Bootstrap-only taxonomy provisioning, called by the ordered update with the core credential. */
    public Uni<Void> installTaxonomy(Mutiny.StatelessSession session, ISystems<?, ?> core, UUID token) {
        Uni<?> chain = arrangements.createArrangementType(session, CATALOG, core, token);
        for (String type : List.of(INSTALLATION, DECLARATION, CONSENT, POLICY, INVOCATION))
            chain = chain.chain(() -> events.createEventType(session, type, core, token));
        Map<String, EnterpriseClassificationDataConcepts> roles = new LinkedHashMap<>();
        roles.put("PluginCatalogType", EnterpriseClassificationDataConcepts.ArrangementXArrangementType);
        for (String name : List.of("PluginTitle", "PluginVersion", "PluginIdentity", "PluginAgeRating"))
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

    /** Bootstrap-only forward taxonomy for catalogue age ratings on enterprises provisioned before ratings. */
    public Uni<Void> installAgeRatingTaxonomy(Mutiny.StatelessSession session, ISystems<?, ?> core, UUID token) {
        return classes.create(session, "PluginAgeRating", "PluginAgeRating",
                EnterpriseClassificationDataConcepts.ArrangementXClassification, core, token).replaceWithVoid();
    }

    /** Rating options for the verified user's profile country (or the default set). */
    public Uni<List<String>> ageRatingOptions(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity) {
        return context(session, system, identity)
                .chain(c -> ages.options(session, c.core().getEnterprise(), identity.partyId()));
    }

    /** Administrator registration; creates a Plugin-typed identity and declares requested systems.
     * This grants neither party installation nor user consent. Re-registration updates metadata.
     * A numeric age rating must be one of the administrator's profile-country options. */
    public Uni<Plugin> register(Mutiny.StatelessSession session, ISystems<?, ?> system,
                                Identity identity, Registration registration) {
        Objects.requireNonNull(registration, "registration");
        return context(session, system, identity).chain(c -> administrator(c)
                .chain(() -> ages.assignable(session, c.core().getEnterprise(), identity.partyId(), registration.ageRating()))
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
                                registration.version(), registration.icon(), registration.screenshots(), registration.systems(),
                                registration.ageRating()))));
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
                                                                media.get("PluginScreenshot"), dependencies, extension.getPluginAgeRating())))
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
        return readEntry(session,plugin,user,plugin.getId(),party,true).replaceWithVoid();
    }

    private Uni<Void> checkDependencies(Mutiny.StatelessSession session, ActorContext c, Invocation invocation, Set<UUID> dependencies) {
        return admission(session, c, invocation, dependencies)
                .chain(() -> ageAllowed(session, c, invocation.pluginId()));
    }

    /** Display metadata and current admission, sharing context only inside this caller-owned transaction.
     * No authorization result is cached across requests or used as a later invocation capability. */
    public record CatalogEntry(Plugin plugin, boolean active, Set<UUID> pluginDependencies) {
        public CatalogEntry { pluginDependencies = Set.copyOf(pluginDependencies); }
    }

    public Uni<CatalogEntry> catalogEntry(Mutiny.StatelessSession session, ISystems<?, ?> writer,
                                          Identity identity, UUID plugin, UUID party) {
        return readEntry(session,writer,identity,plugin,party,false);
    }

    private Uni<CatalogEntry> readEntry(Mutiny.StatelessSession session, ISystems<?, ?> writer,
                                        Identity identity, UUID plugin, UUID party, boolean enforce) {
        Objects.requireNonNull(plugin, "plugin");
        return readContext(session, writer, identity).chain(c -> lockCatalog(session,c,plugin,false)
                .chain(() -> com.guicedee.activitymaster.fsdm.client.services.ApplicableTokenQuery.prepare(session,c.core(),identity.tokens()))
                .chain(tokens -> catalogAdmission(session,c,new Invocation(plugin,party),tokens,enforce)));
    }

    private Uni<CatalogEntry> catalogAdmission(Mutiny.StatelessSession session, ActorContext c, Invocation invocation, com.guicedee.activitymaster.fsdm.client.services.ApplicableTokenQuery tokens, boolean enforce) {
        // No list-wide snapshot: this statement follows this entry's lock and token refresh.
        // Metadata and the exact authority keys are derived from the same declaration set.
        String sql = tokens.cte() + ", " + declarationScope().substring(5) + ", metadata(kind,name,value,extra) as materialized (" + metadataRows() + "), "
                + "wanted(statekey) as (select cast(:installation as text) union select :pluginText||':'||name from metadata where kind='target' "
                + "union select :pluginText||':'||name||':'||:zero from metadata where kind='target' "
                + "union select :pluginText||':'||name||':'||:partyText from metadata where kind='target'), "
                + admissionSql("select statekey from wanted", "select cast(name as uuid) from metadata where kind='target'", "select securitytokenid from applicable").substring(5)
                + " union all select 'metadata',kind,name,value,extra,cast(null as text) from metadata"
                + " union all select 'dependency',cast(s.systemid as text),cast(null as text),cast(null as text),cast(null as text),cast(null as text) "
                + "from dbo.systems s join arrangement.arrangement a on a.arrangementid=s.systemid and a.enterpriseid=s.enterpriseid "
                + "join arrangement.arrangementxarrangementtype x on x.arrangementid=a.arrangementid and x.enterpriseid=a.enterpriseid and x.systemid=a.systemid "
                + "join arrangement.arrangementtype t on t.arrangementtypeid=x.arrangementtypeid and t.enterpriseid=x.enterpriseid and t.systemid=x.systemid "
                + "where s.systemid in (select cast(name as uuid) from metadata where kind='target') and t.arrangementtypename='Plugin Catalog' "
                + "and " + live("s") + " and " + live("a") + " and " + live("x") + " and " + live("t")
                + " union all select 'verifiedActor',cast(null as text),cast(null as text),cast(null as text),cast(null as text),cast(null as text) "
                + "where exists(" + actorSql().replace(":actor",":actorId") + ")";
        var query = session.createNativeQuery(sql,Object[].class)
                .setParameter("plugin",invocation.pluginId()).setParameter("pluginText",invocation.pluginId().toString())
                .setParameter("type",DECLARATION).setParameter("core",c.core().getId()).setParameter("enterprise",c.identity().enterpriseId())
                .setParameter("installation",key(invocation.pluginId(),invocation.installationPartyId()))
                .setParameter("party",invocation.installationPartyId()).setParameter("partyText",invocation.installationPartyId().toString())
                .setParameter("actor",c.identity().partyId().toString()).setParameter("zero",ZERO.toString())
                .setParameter("actorId",c.identity().partyId()).setParameter("credential",c.identity().identityToken().toString());
        return tokens.bind(query).getResultList().chain(rows -> {
                    List<Object[]> metadataRows = new ArrayList<>(), authorityRows = new ArrayList<>();
                    Set<UUID> dependencies = new HashSet<>();
                    boolean ambiguousDependency = false, verifiedActor = false;
                    for(var row:rows) {
                        if("metadata".equals(row[0])) metadataRows.add(new Object[]{row[1],row[2],row[3],row[4]});
                        else if("dependency".equals(row[0])) {
                            if(!dependencies.add(UUID.fromString((String)row[1]))) ambiguousDependency=true;
                        } else if("verifiedActor".equals(row[0])) verifiedActor=true;
                        else authorityRows.add(row);
                    }
                    Plugin plugin = metadata(invocation.pluginId(),metadataRows);
                    try {
                        if(ambiguousDependency || !verifiedActor) throw new SecurityException("Plugin credential or dependency denied");
                        verifyAdmission(c,invocation,plugin.systems(),authorityRows);
                    } catch(SecurityException denied) {
                        return enforce ? Uni.createFrom().failure(denied) : Uni.createFrom().item(new CatalogEntry(plugin,false,dependencies));
                    }
                    if(enforce) return ages.require(session,c.core().getEnterprise(),c.identity().partyId(),AgeRating.parse(plugin.ageRating()))
                            .replaceWith(new CatalogEntry(plugin,true,dependencies));
                    return ages.require(session,c.core().getEnterprise(),c.identity().partyId(),AgeRating.parse(plugin.ageRating()))
                            .replaceWith(true).onFailure(SecurityException.class).recoverWithItem(false)
                            .map(active -> new CatalogEntry(plugin,active,dependencies));
                });
    }

    public Uni<Plugin> find(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity, UUID plugin) {
        Objects.requireNonNull(plugin, "plugin");
        return readContext(session, system, identity).chain(c -> find(session, c, plugin));
    }

    private Uni<Plugin> find(Mutiny.StatelessSession session, ActorContext c, UUID plugin) {
        return lockCatalog(session, c, plugin, false).chain(() -> catalogSnapshot(session, c, plugin));
    }

    /** One canonical metadata read after the existing per-plugin catalogue lock. */
    private Uni<Plugin> catalogSnapshot(Mutiny.StatelessSession session, ActorContext c, UUID plugin) {
        String sql = declarationScope() + metadataRows();
        return session.createNativeQuery(sql, Object[].class).setParameter("plugin", plugin).setParameter("core", c.core().getId())
                .setParameter("enterprise", c.identity().enterpriseId()).setParameter("type", DECLARATION).getResultList().map(rows -> metadata(plugin, rows));
    }

    private static String metadataRows() {
        return "select 'system',s.systemname,s.systemdesc,cast(null as text) from dbo.systems s where s.systemid=:plugin and " + live("s")
                + " union all select 'value',c.classificationname,x.value,cast(null as text) from arrangement.arrangementxclassification x "
                + "join classification.classification c on c.classificationid=x.classificationid where x.arrangementid=:plugin "
                + "and x.systemid=:core and c.systemid=:core and " + live("x") + " and " + live("c")
                + " union all select 'media',c.classificationname,cast(x.resourceitemid as text),x.value from arrangement.arrangementxresourceitem x "
                + "join classification.classification c on c.classificationid=x.classificationid join resource.resourceitem r on r.resourceitemid=x.resourceitemid "
                + "where x.arrangementid=:plugin and x.systemid=:core and c.systemid=:core and c.classificationname in ('PluginIcon','PluginScreenshot') "
                + "and " + live("x") + " and " + live("c") + " and " + live("r")
                + " union all select 'target',v.value,cast(null as text),cast(null as text)" + declarationRows();
    }

    private static Plugin metadata(UUID plugin, List<Object[]> rows) {

        Object[] system = null;
        Map<String,String> values = new HashMap<>();
        Set<UUID> targets = new HashSet<>();
        List<Object[]> media = new ArrayList<>();
        for (Object[] row : rows) switch ((String)row[0]) {
            case "system" -> { if(system != null) throw new SecurityException("Ambiguous plugin metadata"); system = row; }
            case "value" -> values.put((String)row[1], (String)row[2]);
            case "target" -> targets.add(UUID.fromString((String)row[1]));
            case "media" -> media.add(row);
            default -> throw new IllegalStateException("Unknown catalogue row");
        }
        if(system == null) throw new SecurityException("Plugin unavailable");
        media.sort(Comparator.comparingInt(row -> Integer.parseInt((String)row[3])));
        UUID icon = media.stream().filter(row -> "PluginIcon".equals(row[1])).map(row -> UUID.fromString((String)row[2])).findFirst().orElse(null);
        List<UUID> screenshots = media.stream().filter(row -> "PluginScreenshot".equals(row[1])).map(row -> UUID.fromString((String)row[2])).toList();
        return new Plugin(plugin, (String)system[1], values.get("PluginTitle"), (String)system[2], values.get("PluginVersion"),
                icon, screenshots, Set.copyOf(targets), values.get("PluginAgeRating"));
    }

    private record StateKey(String type, String key) { }

    /** Read the exact installation/declaration/policy/consent set together. No result outlives this
     * transaction; catalogue locks and normal domain authorization remain with their callers. */
    private Uni<Void> admission(Mutiny.StatelessSession session, ActorContext c, Invocation invocation, Set<UUID> targets) {
        if (targets.isEmpty()) return denied();
        // Refresh after acquiring the catalogue lock: a blocked admission must not reuse
        // the token expansion captured before a concurrent credential/group revocation.
        return com.guicedee.activitymaster.fsdm.client.services.ApplicableTokenQuery.prepare(session,c.core(),c.identity().tokens())
                .chain(tokens -> admissionRows(session,c,invocation,targets,tokens));
    }

    private Uni<Void> admissionRows(Mutiny.StatelessSession session, ActorContext c, Invocation invocation,
                                    Set<UUID> targets, com.guicedee.activitymaster.fsdm.client.services.ApplicableTokenQuery tokens) {
        Set<String> keys = new HashSet<>();
        String installationKey = key(invocation.pluginId(), invocation.installationPartyId());
        keys.add(installationKey);
        for(UUID target : targets) {
            keys.add(key(invocation.pluginId(), target));
            keys.add(key(invocation.pluginId(), target, ZERO));
            keys.add(key(invocation.pluginId(), target, invocation.installationPartyId()));
        }
        String sql = tokens.cte() + ", " + admissionSql(":keys", ":targets", "select securitytokenid from applicable").substring(5);
        var query = session.createNativeQuery(sql, Object[].class).setParameter("core", c.core().getId()).setParameter("enterprise", c.identity().enterpriseId())
                .setParameter("keys", keys).setParameter("installation", installationKey)
                .setParameter("actor", c.identity().partyId().toString()).setParameter("targets", targets).setParameter("party", invocation.installationPartyId());
        return tokens.bind(query).getResultList().invoke(rows -> verifyAdmission(c, invocation, targets, rows)).replaceWithVoid();
    }

    private static String admissionSql(String keys, String targets, String tokens) {
        return "with requested_systems as materialized (select s.systemid,s.enterpriseid,s.activeflagid,s.effectivefromdate,s.effectivetodate from dbo.systems s where s.systemid in (" + targets + ") and " + live("s") + "), "
                + "authority_keys as materialized (select k.eventid,k.classificationid,k.systemid,k.enterpriseid,k.activeflagid,k.effectivefromdate,k.effectivetodate,k.value from event.eventxclassification k where k.value in (" + keys + ") "
                + "and k.systemid=:core and " + live("k") + "), "
                + "states as materialized (select t.eventtypename as kind,k.value as statekey,e.eventid,enabled.value as enabled"
                + stateFrom("authority_keys") + " where e.systemid=:core and t.eventtypename in ('Plugin Installation','Plugin System Declaration','Plugin System Policy') "
                + "and k.value in (" + keys + ") and " + stateLive() + "), "
                + "consent_keys as materialized (select k.eventid,k.classificationid,k.systemid,k.enterpriseid,k.activeflagid,k.effectivefromdate,k.effectivetodate,k.value from event.eventxclassification k where k.systemid=:core and " + live("k")
                + " and k.value in (select cast(installation.eventid as text)||':'||cast(declaration.eventid as text)||':'||:actor "
                + "from states installation join states declaration on declaration.kind='Plugin System Declaration' and declaration.enabled='1' "
                + "where installation.kind='Plugin Installation' and installation.statekey=:installation and installation.enabled='1')) "
                + "select 'state',kind,statekey,cast(eventid as text),enabled,cast(null as text) from states "
                + "union all select 'consent',t.eventtypename,k.value,cast(e.eventid as text),enabled.value,cast(exists(select 1 from event.eventsecuritytoken g "
                + "where g.eventsid=e.eventid and g.enterpriseid=:enterprise and g.securitytokenid in (" + tokens + ") and g.readallowed=1 "
                + "and g.effectivefromdate<=statement_timestamp() and g.effectivetodate>statement_timestamp()) as text)"
                + stateFrom("consent_keys")
                + " join states installation on installation.kind='Plugin Installation' and installation.statekey=:installation and installation.enabled='1' "
                + "join states declaration on declaration.kind='Plugin System Declaration' and declaration.enabled='1' "
                + "where t.eventtypename='Plugin User Consent' and e.systemid=:core "
                + "and k.value=cast(installation.eventid as text)||':'||cast(declaration.eventid as text)||':'||:actor and " + stateLive()
                + " union all select 'target',cast(s.systemid as text),cast(null as text),cast(null as text),cast(null as text),cast(null as text) "
                + "from requested_systems s where true "
                + "and exists(select 1 from states where kind='Plugin Installation' and statekey=:installation and enabled='1') and " + live("s")
                + " and exists(select 1 from dbo.systemxclassification x join classification.classification c on c.classificationid=x.classificationid "
                + "join security.securitytoken k on k.securitytoken=x.value join classification.classification kt on kt.classificationid=k.securitytokenclassificationid "
                + "where x.systemid=s.systemid and c.classificationname='SystemIdentity' and kt.classificationname in ('System','Plugin') "
                + "and " + live("x") + " and " + live("c") + " and " + live("k") + " and " + live("kt") + ")"
                + " union all select 'party',cast(p.involvedpartyid as text),cast(null as text),cast(null as text),cast(null as text),cast(null as text) "
                + "from party.involvedparty p where p.involvedpartyid=:party "
                + "and exists(select 1 from states where kind='Plugin Installation' and statekey=:installation and enabled='1') and " + live("p")
                + " and exists(select 1 from party.involvedpartysecuritytoken g where g.involvedpartyid=p.involvedpartyid "
                + "and g.enterpriseid=:enterprise and g.securitytokenid in (" + tokens + ") and g.readallowed=1 "
                + "and g.effectivefromdate<=statement_timestamp() and g.effectivetodate>statement_timestamp())";
    }

    private static void verifyAdmission(ActorContext c, Invocation invocation, Set<UUID> targets, List<Object[]> rows) {

        if(targets.isEmpty()) throw new SecurityException("Plugin has no declared targets");
        String installationKey = key(invocation.pluginId(), invocation.installationPartyId());
        Map<StateKey,State> states = new HashMap<>();
        Set<UUID> validTargets = new HashSet<>();
        Set<UUID> readable = new HashSet<>();
        boolean party = false;
        for(Object[] row : rows) {
            String kind = (String)row[0];
            if("target".equals(kind)) { validTargets.add(UUID.fromString((String)row[1])); continue; }
            if("party".equals(kind)) { party = true; continue; }
            State state = new State(UUID.fromString((String)row[3]), "1".equals(row[4]));
            if(states.putIfAbsent(new StateKey((String)row[1], (String)row[2]), state) != null)
                throw new SecurityException("Ambiguous plugin authority");
            if("consent".equals(kind) && "true".equals(row[5])) readable.add(state.id());
        }
        if(!party || !validTargets.equals(targets)) throw new SecurityException("Plugin target or party denied");
        State installation = required(states, INSTALLATION, installationKey);
        for(UUID target : targets) {
            State declaration = required(states, DECLARATION, key(invocation.pluginId(), target));
            for(UUID scope : List.of(ZERO, invocation.installationPartyId())) {
                State policy = states.get(new StateKey(POLICY, key(invocation.pluginId(), target, scope)));
                if(policy != null && !policy.enabled()) throw new SecurityException("Plugin policy denied");
            }
            State consent = required(states, CONSENT, key(installation.id(), declaration.id(), c.identity().partyId()));
            if(!readable.contains(consent.id())) throw new SecurityException("Plugin consent row denied");
        }
    }

    private static State required(Map<StateKey,State> states, String type, String key) {
        State state = states.get(new StateKey(type, key));
        if(state == null || !state.enabled()) throw new SecurityException("Plugin access denied");
        return state;
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
                        .chain(declaration -> (enabled ? permitted(session, c, invocation, targetSystem)
                                        .chain(() -> ageAllowed(session, c, invocation.pluginId())) : Uni.createFrom().voidItem())
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
        return readContext(session, targetSystem, identity).chain(c -> lockCatalog(session, c, invocation.pluginId(), false)
                .chain(() -> check(session, c, targetSystem.getId(), invocation)));
    }

    /** The caller already owns the catalogue lock and verified context for this operation. */
    private Uni<Void> check(Mutiny.StatelessSession session, ActorContext c, UUID target, Invocation invocation) {
        return checkDependencies(session, c, invocation, Set.of(target));
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

    /** Direct built-in domain write: admission covers its declared dependencies, rather than a self-declaration. */
    public Uni<Void> auditBuiltIn(Mutiny.StatelessSession session, ISystems<?, ?> writer, Identity identity,
                                  UUID installationParty, String operation) {
        if (operation == null || operation.isBlank() || operation.length() > 150)
            throw new IllegalArgumentException("Plugin operation required (maximum 150 characters)");
        return checkBuiltIn(session, writer, identity, installationParty)
                .chain(() -> context(session, writer, identity))
                .chain(c -> newEvent(session, c, writer.getId(), INVOCATION, null, true,
                        writer.getId(), installationParty, writer.getId())
                        .chain(event -> eventValue(session, c, event, "PluginOperation", operation)));
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

    private Uni<ISystems<?, ?>> verifiedCore(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity) {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(identity, "identity");
        if (session.currentTransaction() == null) throw new IllegalStateException("Plugin operations require a caller-owned transaction");
        if (system == null || system.getEnterprise() == null || !identity.enterpriseId().equals(system.getEnterprise().getId()))
            return denied();
        // A library/system/plugin credential cannot be substituted for an authenticated user.
        return session.createNativeQuery(actorSql(), Integer.class)
                .setParameter("actor", identity.partyId()).setParameter("credential", identity.identityToken().toString())
                .setParameter("enterprise", identity.enterpriseId()).setMaxResults(1).getResultList()
                .chain(rows -> rows.isEmpty() ? PluginService.<ISystems<?, ?>>denied()
                        : systems.getActivityMaster(session, system.getEnterprise(), identity.tokens()));
    }

    private static String actorSql() {
        return "select 1 from security.securitytoken k "
                        + "join classification.classification c on c.classificationid=k.securitytokenclassificationid "
                        + "join party.involvedpartyorganic p on p.involvedpartyorganicid=:actor "
                        + "where k.securitytoken=:credential and c.classificationname in ('User','Identity') "
                        + "and " + live("k") + " and " + live("c") + " and " + live("p");
    }

    private Uni<ReadContext> readContext(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity) {
        Objects.requireNonNull(session, "session");
        Objects.requireNonNull(identity, "identity");
        if(session.currentTransaction()==null) throw new IllegalStateException("Plugin operations require a caller-owned transaction");
        if(system==null || system.getEnterprise()==null || !identity.enterpriseId().equals(system.getEnterprise().getId())) return denied();
        return systems.getActivityMaster(session,system.getEnterprise(),identity.tokens()).map(core -> new ReadContext(core,identity));
    }

    private Uni<Ctx> context(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity) {
        return verifiedCore(session, system, identity)
                .chain(core -> security.getSecurityToken(session, identity.identityToken(), core, identity.tokens())
                        .chain(credential -> security.getAdministratorsFolder(session, core, identity.tokens())
                                .chain(admin -> security.getApplicableSecurityTokenIds(session, core, identity.tokens())
                                        .map(tokens -> new Ctx(core, identity, credential, admin, Set.copyOf(tokens))))));
    }

    private Uni<Void> administrator(Ctx c) {
        return c.tokens().contains(c.administrators().getId()) ? Uni.createFrom().voidItem() : denied();
    }

    /** Read-only current administrator guard for trusted catalogue review adapters.
     * The host must bind the organic identity before calling; this never registers a plugin. */
    public Uni<Void> requireAdministrator(Mutiny.StatelessSession session, ISystems<?, ?> system, Identity identity) {
        return context(session, system, identity).chain(this::administrator);
    }

    /** Review mutation boundary: same exclusive catalogue lock used by consent/policy changes.
     * The caller owns the transaction and must append its secured evidence and audit before commit. */
    public Uni<Void> lockReviewCatalog(Mutiny.StatelessSession session, ISystems<?, ?> system,
                                       Identity identity, UUID plugin) {
        return context(session, system, identity).chain(c -> administrator(c)
                .chain(() -> lockCatalog(session, c, plugin, true))
                .chain(() -> new Arrangement().setId(plugin).canWrite(session, c.core(), identity.tokens()))
                .chain(allowed -> allowed ? Uni.createFrom().voidItem() : denied()));
    }
    /** The verified user must satisfy the plugin's catalogue age rating; an unrated plugin is "All". */
    private Uni<Void> ageAllowed(Mutiny.StatelessSession session, ActorContext c, UUID plugin) {
        return catalogValues(session, c, plugin)
                .chain(values -> ages.require(session, c.core().getEnterprise(), c.identity().partyId(),
                        AgeRating.parse(values.get("PluginAgeRating"))));
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
    private Uni<Void> lockCatalog(Mutiny.StatelessSession session, ActorContext c, UUID plugin, boolean write) {
        Objects.requireNonNull(plugin, "plugin");
        var query = session.createNativeQuery("""
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
                + " and " + live("h") + " and " + live("f")
                + (c instanceof ReadContext ? " and exists(" + actorSql() + ")" : "") + (write ? " for update of a" : " for share of a"), UUID.class)
                .setParameter("id", plugin).setParameter("core", c.core().getId()).setParameter("enterprise", c.identity().enterpriseId())
                .setMaxResults(2);
        if(c instanceof ReadContext) query.setParameter("actor",c.identity().partyId())
                .setParameter("credential",c.identity().identityToken().toString());
        return query.getResultList().chain(rows -> rows.size() == 1 ? Uni.createFrom().voidItem() : denied());
    }

    private Uni<Void> metadata(Mutiny.StatelessSession session, Ctx c, UUID plugin, Registration registration) {
        return flags.getArchivedFlag(session, c.core().getEnterprise(), c.identity().tokens()).chain(flag ->
                session.createNativeQuery("update arrangement.arrangementxclassification set activeflagid=:flag,effectivetodate=statement_timestamp(),"
                                + "warehouselastupdatedtimestamp=statement_timestamp() where arrangementid=:id and " + live("arrangement.arrangementxclassification")
                                + " and classificationid in (select classificationid from classification.classification where systemid=:core "
                                + "and classificationname in ('PluginTitle','PluginVersion','PluginAgeRating'))")
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
                // Absent means "All", so unrated catalogues never depend on the forward age-rating taxonomy.
                .chain(() -> AgeRating.ALL.equals(registration.ageRating()) ? Uni.createFrom().voidItem()
                        : catalogValue(session, c, new Arrangement().setId(plugin), "PluginAgeRating", registration.ageRating()))
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
    private Uni<Map<String,String>> catalogValues(Mutiny.StatelessSession session, ActorContext c, UUID plugin) {
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
    /** Restrict the event IDs before joining their role rows; avoid a catalogue-wide classification join. */
    private static String declarationScope() {
        return "with declarations as materialized (select e.eventid from event.event e "
                + "join event.eventxarrangement a on a.eventid=e.eventid "
                + "join event.eventxeventtype xt on xt.eventid=e.eventid join event.eventtype t on t.eventtypeid=xt.eventtypeid "
                + "where a.arrangementid=:plugin and t.eventtypename=:type and e.systemid=:core and xt.systemid=:core and t.systemid=:core "
                + "and " + live("e") + " and " + live("a") + " and " + live("xt") + " and " + live("t") + ") ";
    }
    private static String declarationRows() {
        return " from declarations d join event.eventxclassification k on k.eventid=d.eventid "
                + "join classification.classification ck on ck.classificationid=k.classificationid "
                + "join event.eventxclassification enabled on enabled.eventid=d.eventid "
                + "join classification.classification ce on ce.classificationid=enabled.classificationid "
                + "join event.eventxclassification v on v.eventid=d.eventid "
                + "join classification.classification cv on cv.classificationid=v.classificationid "
                + "where ck.classificationname='PluginStateKey' and ce.classificationname='PluginEnabled' and enabled.value='1' "
                + "and cv.classificationname='PluginTargetSystem' and k.systemid=:core and ck.systemid=:core "
                + "and enabled.systemid=:core and ce.systemid=:core "
                + "and " + live("k") + " and " + live("ck") + " and " + live("enabled") + " and " + live("ce")
                + " and " + live("v") + " and " + live("cv");
    }
    private Uni<Set<UUID>> declaredSystems(Mutiny.StatelessSession session, ActorContext c, UUID plugin) {
        return session.createNativeQuery(declarationScope() + "select v.value" + declarationRows(), String.class)
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
    private static String stateFrom() { return stateFrom("event.eventxclassification"); }
    private static String stateFrom(String keys) {
        return " from event.event e join event.eventxeventtype xt on xt.eventid=e.eventid "
                + "join event.eventtype t on t.eventtypeid=xt.eventtypeid "
                + "join " + keys + " k on k.eventid=e.eventid "
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
                // ActiveFlagID is a primary key. A scalar lookup cannot be pulled up into a
                // Cartesian product of independent flags before the secured rows are filtered.
                + alias + ".effectivetodate>statement_timestamp() and coalesce((select plugin_live_flag.allowaccess=1 from dbo.activeflag plugin_live_flag where "
                + "plugin_live_flag.activeflagid=" + alias + ".activeflagid and plugin_live_flag.enterpriseid=:enterprise "
                + "and plugin_live_flag.effectivefromdate<=statement_timestamp() and plugin_live_flag.effectivetodate>statement_timestamp()),false)";
    }
    private static <T> Uni<T> denied() { return Uni.createFrom().failure(new SecurityException("Plugin access denied")); }
}

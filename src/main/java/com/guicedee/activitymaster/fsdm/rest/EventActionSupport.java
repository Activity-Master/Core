package com.guicedee.activitymaster.fsdm.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.ArrangementsService;
import com.guicedee.activitymaster.fsdm.EventsService;
import com.guicedee.activitymaster.fsdm.ProductService;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.IRulesService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EventArrangementClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EventResourceItemClassifications;
import com.guicedee.activitymaster.fsdm.client.services.rest.EventActionRequest;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

/**
 * Shared helper that associates a REST create/update action with an {@code Event} and records a
 * change summary against it.
 * <p>
 * When a domain create/update request carries an {@link EventActionRequest} with a non-null
 * {@code eventId}, the affected entity is linked to that event via the existing
 * {@code EventX<Domain>} relationship using an audit <em>action</em> classification (e.g.
 * {@code UpdatedTheResourceItem}), and the request's {@code summary} is stored as the relationship
 * value. The whole association runs <strong>fire-and-forget</strong> in its own session/transaction,
 * so it never blocks or fails the primary operation.
 * <p>
 * The action classification is <em>ensured</em> (idempotent find-or-create) under the relevant data
 * concept before linking, so the helper is self-sufficient regardless of install ordering and also
 * supports caller-supplied custom action verbs via {@link EventActionRequest#action}.
 */
@Singleton
@Log4j2
public class EventActionSupport {

    // ── Default per-domain audit verbs (globally-unique classification names) ──
    public static final String RESOURCE_ITEM_CREATED = EventResourceItemClassifications.AddedResourceItem.name();
    public static final String RESOURCE_ITEM_UPDATED = EventResourceItemClassifications.UpdatedTheResourceItem.name();
    public static final String PARTY_CREATED = "CreatedTheInvolvedParty";
    public static final String PARTY_UPDATED = "UpdatedTheInvolvedParty";
    public static final String ARRANGEMENT_CREATED = EventArrangementClassifications.Started.name();
    public static final String ARRANGEMENT_UPDATED = EventArrangementClassifications.AffectedThe.name();
    public static final String PRODUCT_CREATED = "AddedTheProduct";
    public static final String PRODUCT_UPDATED = "UpdatedTheProduct";
    public static final String RULES_CREATED = "AddedTheRule";
    public static final String RULES_UPDATED = "UpdatedTheRule";

    @Inject
    private IEventService<EventsService> eventService;

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private IResourceItemService<?> resourceItemService;

    @Inject
    private IInvolvedPartyService<?> involvedPartyService;

    @Inject
    private IArrangementsService<ArrangementsService> arrangementsService;

    @Inject
    private IProductService<ProductService> productService;

    @Inject
    private IRulesService<?> rulesService;

    // ──────────────────────────────────────────────────────────────────────────
    // Per-domain entry points
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Associates a resource item create/update with the requested event (if any).
     *
     * @param created {@code true} for a create action, {@code false} for an update action
     */
    public void recordResourceItemAction(String enterpriseName, String requestingSystemName,
                                          EventActionRequest request, boolean created, UUID resourceItemId) {
        record(enterpriseName, requestingSystemName, request,
                created ? RESOURCE_ITEM_CREATED : RESOURCE_ITEM_UPDATED,
                EnterpriseClassificationDataConcepts.EventXResourceItem,
                (s, event, action, summary, system, token) ->
                        resourceItemService.findByUUID(s, resourceItemId)
                                .chain(ri -> event.addOrUpdateResourceItem(s, action, ri, null, summary, system, token))
                                .replaceWithVoid());
    }

    /**
     * Associates an involved party create/update with the requested event (if any).
     */
    public void recordPartyAction(String enterpriseName, String requestingSystemName,
                                  EventActionRequest request, boolean created, UUID partyId) {
        record(enterpriseName, requestingSystemName, request,
                created ? PARTY_CREATED : PARTY_UPDATED,
                EnterpriseClassificationDataConcepts.EventXInvolvedParty,
                (s, event, action, summary, system, token) ->
                        involvedPartyService.find(s, partyId)
                                .chain(party -> event.addOrUpdateInvolvedParty(s, action, party, null, summary, system, token))
                                .replaceWithVoid());
    }

    /**
     * Associates an arrangement create/update with the requested event (if any).
     */
    public void recordArrangementAction(String enterpriseName, String requestingSystemName,
                                        EventActionRequest request, boolean created, UUID arrangementId) {
        record(enterpriseName, requestingSystemName, request,
                created ? ARRANGEMENT_CREATED : ARRANGEMENT_UPDATED,
                EnterpriseClassificationDataConcepts.EventXArrangement,
                (s, event, action, summary, system, token) ->
                        arrangementsService.find(s, arrangementId, system, token)
                                .chain(arr -> event.addOrUpdateArrangement(s, action, arr, null, summary, system, token))
                                .replaceWithVoid());
    }

    /**
     * Associates a product create/update with the requested event (if any).
     */
    public void recordProductAction(String enterpriseName, String requestingSystemName,
                                    EventActionRequest request, boolean created, UUID productId) {
        record(enterpriseName, requestingSystemName, request,
                created ? PRODUCT_CREATED : PRODUCT_UPDATED,
                EnterpriseClassificationDataConcepts.EventXProduct,
                (s, event, action, summary, system, token) ->
                        productService.find(s, productId)
                                .chain(prod -> event.addOrUpdateProduct(s, action, prod, null, summary, system, token))
                                .replaceWithVoid());
    }

    /**
     * Associates a rule create/update with the requested event (if any).
     */
    public void recordRulesAction(String enterpriseName, String requestingSystemName,
                                  EventActionRequest request, boolean created, UUID rulesId) {
        record(enterpriseName, requestingSystemName, request,
                created ? RULES_CREATED : RULES_UPDATED,
                EnterpriseClassificationDataConcepts.EventXRules,
                (s, event, action, summary, system, token) ->
                        rulesService.find(s, rulesId)
                                .chain(rule -> rule == null
                                        ? Uni.createFrom().voidItem()
                                        : event.addOrUpdateRules(s, action, rule, null, summary, system, token).replaceWithVoid()));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Core fire-and-forget association
    // ──────────────────────────────────────────────────────────────────────────

    @FunctionalInterface
    private interface EventLinkOperation {
        Uni<Void> apply(Mutiny.Session session, IEvent<?, ?> event, String action, String summary,
                        ISystems<?, ?> system, UUID[] token);
    }

    private void record(String enterpriseName, String requestingSystemName, EventActionRequest request,
                        String defaultAction, EnterpriseClassificationDataConcepts actionConcept,
                        EventLinkOperation operation) {
        if (request == null || request.eventId == null) {
            return;
        }
        final UUID eventId = request.eventId;
        final String action = (request.action != null && !request.action.isBlank())
                ? request.action.trim() : defaultAction;
        final String summary = request.summary;

        SessionUtils.fireAndForget(
                SessionUtils.<Void>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                    Mutiny.Session s = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return eventService.find(s, eventId)
                            .chain(event -> ensureActionClassification(s, action, actionConcept, system, token)
                                    .chain(ignored -> operation.apply(s, (IEvent<?, ?>) event, action, summary, system, token)))
                            .replaceWithVoid();
                }),
                "event " + eventId + " action '" + action + "'");
    }

    /**
     * Idempotently ensures the audit action classification exists under its data concept so the
     * subsequent link's name lookup succeeds. {@code create(...)} is name+concept+enterprise scoped,
     * so this is a no-op when the classification already exists. Failures are swallowed — a missing
     * action verb must never break the primary operation.
     */
    private Uni<?> ensureActionClassification(Mutiny.Session session, String action,
                                              EnterpriseClassificationDataConcepts concept,
                                              ISystems<?, ?> system, UUID[] token) {
        return classificationService.create(session, action, action, concept, system, token)
                .onFailure().recoverWithNull();
    }
}





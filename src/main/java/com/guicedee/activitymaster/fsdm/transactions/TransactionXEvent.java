package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_event")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXEvent extends WarehouseClassificationRelationshipTable<Transaction, Event,
        TransactionXEvent, TransactionXEvent.Builder, UUID, TransactionXEventSecurityToken> {
    @Id @Column(name = "transaction_x_event_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", referencedColumnName = "EventID", nullable = false)
    private Event eventID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXEventSecurityToken> securities;

    public TransactionXEvent() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXEvent setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXEvent setTransaction(Transaction value) { transaction = value; return this; }
    public Event getEventID() { return eventID; }
    public TransactionXEvent setEventID(Event value) { eventID = value; return this; }
    public List<TransactionXEventSecurityToken> getSecurities() { return securities; }
    public TransactionXEvent setSecurities(List<TransactionXEventSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXEventSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Event getSecondary() { return getEventID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Event, Builder,
            TransactionXEvent, UUID, TransactionXEventSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Event> getSecondaryAttribute() { return getAttribute("eventID"); }
    }
}

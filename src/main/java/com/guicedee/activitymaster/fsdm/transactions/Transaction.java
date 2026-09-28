package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Secured, immutable movement under a parent FSDM Event and Arrangement. */
@Entity
@Immutable
@Table(schema = "transactions", name = "entry")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class Transaction extends WarehouseSCDTable<Transaction, Transaction.Builder, UUID, TransactionSecurityToken> {
    @Id @Column(name = "entry_id", nullable = false, updatable = false)
    private UUID id;
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "EventID", insertable = false, updatable = false)
    private Event event;
    @Column(name = "operation_key", nullable = false, updatable = false)
    private UUID operationKey;
    @Column(name = "line_no", nullable = false, updatable = false)
    private int lineNumber;
    @Column(name = "arrangement_id", nullable = false, updatable = false)
    private UUID arrangementId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrangement_id", referencedColumnName = "ArrangementID", insertable = false, updatable = false)
    private Arrangement arrangement;
    @Column(name = "transaction_type_id", nullable = false, updatable = false)
    private UUID transactionTypeId;
    @Column(name = "direction", nullable = false, updatable = false)
    private short direction;
    @Column(name = "amount", nullable = false, precision = 38, scale = 8, updatable = false)
    private BigDecimal amount;
    @Column(name = "unit", nullable = false, updatable = false)
    private String unit;
    @OneToMany(mappedBy = "base")
    private List<TransactionSecurityToken> securities;
    @OneToMany(mappedBy = "transaction")
    private List<TransactionXTransactionType> types;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXInvolvedParty> involvedPartyRelationships;

    public List<TransactionXInvolvedParty> getInvolvedPartyRelationships() { return involvedPartyRelationships; }
    public Transaction setInvolvedPartyRelationships(List<TransactionXInvolvedParty> value) { involvedPartyRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXResourceItem> resourceItemRelationships;

    public List<TransactionXResourceItem> getResourceItemRelationships() { return resourceItemRelationships; }
    public Transaction setResourceItemRelationships(List<TransactionXResourceItem> value) { resourceItemRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXArrangement> arrangementRelationships;

    public List<TransactionXArrangement> getArrangementRelationships() { return arrangementRelationships; }
    public Transaction setArrangementRelationships(List<TransactionXArrangement> value) { arrangementRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXEvent> eventRelationships;

    public List<TransactionXEvent> getEventRelationships() { return eventRelationships; }
    public Transaction setEventRelationships(List<TransactionXEvent> value) { eventRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXProduct> productRelationships;

    public List<TransactionXProduct> getProductRelationships() { return productRelationships; }
    public Transaction setProductRelationships(List<TransactionXProduct> value) { productRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXAddress> addressRelationships;

    public List<TransactionXAddress> getAddressRelationships() { return addressRelationships; }
    public Transaction setAddressRelationships(List<TransactionXAddress> value) { addressRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXGeography> geographyRelationships;

    public List<TransactionXGeography> getGeographyRelationships() { return geographyRelationships; }
    public Transaction setGeographyRelationships(List<TransactionXGeography> value) { geographyRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXRules> rulesRelationships;

    public List<TransactionXRules> getRulesRelationships() { return rulesRelationships; }
    public Transaction setRulesRelationships(List<TransactionXRules> value) { rulesRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXTransaction> transactionRelationships;

    public List<TransactionXTransaction> getTransactionRelationships() { return transactionRelationships; }
    public Transaction setTransactionRelationships(List<TransactionXTransaction> value) { transactionRelationships = value; return this; }

    @OneToMany(mappedBy = "transaction")
    private List<TransactionXClassification> classificationRelationships;

    public List<TransactionXClassification> getClassificationRelationships() { return classificationRelationships; }
    public Transaction setClassificationRelationships(List<TransactionXClassification> value) { classificationRelationships = value; return this; }

    public Transaction() { }
    @Override public UUID getId() { return id; }
    @Override public Transaction setId(UUID id) { this.id = id; return this; }
    public UUID getEventId() { return eventId; }
    public Transaction setEventId(UUID value) { eventId = value; return this; }
    public Event getEvent() { return event; }
    public UUID getOperationKey() { return operationKey; }
    public Transaction setOperationKey(UUID value) { operationKey = value; return this; }
    public int getLineNumber() { return lineNumber; }
    public Transaction setLineNumber(int value) { lineNumber = value; return this; }
    public UUID getArrangementId() { return arrangementId; }
    public Transaction setArrangementId(UUID value) { arrangementId = value; return this; }
    public Arrangement getArrangement() { return arrangement; }
    public UUID getTransactionTypeId() { return transactionTypeId; }
    public Transaction setTransactionTypeId(UUID value) { transactionTypeId = value; return this; }
    public short getDirection() { return direction; }
    public Transaction setDirection(short value) { direction = value; return this; }
    public BigDecimal getAmount() { return amount; }
    public Transaction setAmount(BigDecimal value) { amount = value; return this; }
    public String getUnit() { return unit; }
    public Transaction setUnit(String value) { unit = value; return this; }
    public List<TransactionSecurityToken> getSecurities() { return securities; }
    public Transaction setSecurities(List<TransactionSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionSecurityToken security) { security.setBase(this); }

    public static class Builder extends QueryBuilderSCD<Builder, Transaction, UUID, TransactionSecurityToken.Builder> { }
}

package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_involved_party")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXInvolvedParty extends WarehouseClassificationRelationshipTable<Transaction, InvolvedParty,
        TransactionXInvolvedParty, TransactionXInvolvedParty.Builder, UUID, TransactionXInvolvedPartySecurityToken> {
    @Id @Column(name = "transaction_x_involved_party_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "involved_party_id", referencedColumnName = "InvolvedPartyID", nullable = false)
    private InvolvedParty involvedPartyID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXInvolvedPartySecurityToken> securities;

    public TransactionXInvolvedParty() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXInvolvedParty setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXInvolvedParty setTransaction(Transaction value) { transaction = value; return this; }
    public InvolvedParty getInvolvedPartyID() { return involvedPartyID; }
    public TransactionXInvolvedParty setInvolvedPartyID(InvolvedParty value) { involvedPartyID = value; return this; }
    public List<TransactionXInvolvedPartySecurityToken> getSecurities() { return securities; }
    public TransactionXInvolvedParty setSecurities(List<TransactionXInvolvedPartySecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXInvolvedPartySecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public InvolvedParty getSecondary() { return getInvolvedPartyID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, InvolvedParty, Builder,
            TransactionXInvolvedParty, UUID, TransactionXInvolvedPartySecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, InvolvedParty> getSecondaryAttribute() { return getAttribute("involvedPartyID"); }
    }
}

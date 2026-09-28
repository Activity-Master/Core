package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_rules")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXRules extends WarehouseClassificationRelationshipTable<Transaction, Rules,
        TransactionXRules, TransactionXRules.Builder, UUID, TransactionXRulesSecurityToken> {
    @Id @Column(name = "transaction_x_rules_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rules_id", referencedColumnName = "RulesID", nullable = false)
    private Rules rulesID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXRulesSecurityToken> securities;

    public TransactionXRules() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXRules setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXRules setTransaction(Transaction value) { transaction = value; return this; }
    public Rules getRulesID() { return rulesID; }
    public TransactionXRules setRulesID(Rules value) { rulesID = value; return this; }
    public List<TransactionXRulesSecurityToken> getSecurities() { return securities; }
    public TransactionXRules setSecurities(List<TransactionXRulesSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXRulesSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Rules getSecondary() { return getRulesID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Rules, Builder,
            TransactionXRules, UUID, TransactionXRulesSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Rules> getSecondaryAttribute() { return getAttribute("rulesID"); }
    }
}

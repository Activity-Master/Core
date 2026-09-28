package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_classification")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXClassification extends WarehouseClassificationRelationshipTable<Transaction, Classification,
        TransactionXClassification, TransactionXClassification.Builder, UUID, TransactionXClassificationSecurityToken> {
    @Id @Column(name = "transaction_x_classification_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @OneToMany(mappedBy = "base")
    private List<TransactionXClassificationSecurityToken> securities;

    public TransactionXClassification() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXClassification setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXClassification setTransaction(Transaction value) { transaction = value; return this; }
    public List<TransactionXClassificationSecurityToken> getSecurities() { return securities; }
    public TransactionXClassification setSecurities(List<TransactionXClassificationSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXClassificationSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Classification getSecondary() { return getClassificationID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Classification, Builder,
            TransactionXClassification, UUID, TransactionXClassificationSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Classification> getSecondaryAttribute() { return getAttribute("classificationID"); }
    }
}

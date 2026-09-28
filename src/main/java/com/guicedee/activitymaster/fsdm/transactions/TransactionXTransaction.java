package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_transaction")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXTransaction extends WarehouseClassificationRelationshipTable<Transaction, Transaction,
        TransactionXTransaction, TransactionXTransaction.Builder, UUID, TransactionXTransactionSecurityToken> {
    @Id @Column(name = "transaction_x_transaction_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transactionID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXTransactionSecurityToken> securities;

    public TransactionXTransaction() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXTransaction setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXTransaction setTransaction(Transaction value) { transaction = value; return this; }
    public Transaction getTransactionID() { return transactionID; }
    public TransactionXTransaction setTransactionID(Transaction value) { transactionID = value; return this; }
    public List<TransactionXTransactionSecurityToken> getSecurities() { return securities; }
    public TransactionXTransaction setSecurities(List<TransactionXTransactionSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXTransactionSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Transaction getSecondary() { return getTransactionID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Transaction, Builder,
            TransactionXTransaction, UUID, TransactionXTransactionSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Transaction> getSecondaryAttribute() { return getAttribute("transactionID"); }
    }
}

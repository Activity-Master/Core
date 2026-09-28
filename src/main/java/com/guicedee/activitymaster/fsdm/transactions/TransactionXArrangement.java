package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_arrangement")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXArrangement extends WarehouseClassificationRelationshipTable<Transaction, Arrangement,
        TransactionXArrangement, TransactionXArrangement.Builder, UUID, TransactionXArrangementSecurityToken> {
    @Id @Column(name = "transaction_x_arrangement_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrangement_id", referencedColumnName = "ArrangementID", nullable = false)
    private Arrangement arrangementID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXArrangementSecurityToken> securities;

    public TransactionXArrangement() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXArrangement setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXArrangement setTransaction(Transaction value) { transaction = value; return this; }
    public Arrangement getArrangementID() { return arrangementID; }
    public TransactionXArrangement setArrangementID(Arrangement value) { arrangementID = value; return this; }
    public List<TransactionXArrangementSecurityToken> getSecurities() { return securities; }
    public TransactionXArrangement setSecurities(List<TransactionXArrangementSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXArrangementSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Arrangement getSecondary() { return getArrangementID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Arrangement, Builder,
            TransactionXArrangement, UUID, TransactionXArrangementSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Arrangement> getSecondaryAttribute() { return getAttribute("arrangementID"); }
    }
}

package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTypesTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.entityassist.querybuilder.builders.JoinExpression;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.annotations.Immutable;
import java.util.List;
import java.util.UUID;

/** Explicit FSDM relationship between a movement and its accounting type. */
@Entity
@Immutable
@Table(schema = "transactions", name = "transaction_x_transaction_type")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXTransactionType extends WarehouseClassificationRelationshipTypesTable<Transaction, TransactionType,
        TransactionXTransactionType, TransactionXTransactionType.Builder, UUID,
        TransactionXTransactionTypeSecurityToken> {
    @Id @Column(name = "transaction_x_transaction_type_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "transaction_type_id", nullable = false)
    private TransactionType type;
    @OneToMany(mappedBy = "base")
    private List<TransactionXTransactionTypeSecurityToken> securities;

    public TransactionXTransactionType() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXTransactionType setId(UUID id) { this.id = id; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXTransactionType setTransaction(Transaction value) { transaction = value; return this; }
    public TransactionType getType() { return type; }
    public TransactionXTransactionType setType(TransactionType value) { type = value; return this; }
    public List<TransactionXTransactionTypeSecurityToken> getSecurities() { return securities; }
    public TransactionXTransactionType setSecurities(List<TransactionXTransactionTypeSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXTransactionTypeSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public TransactionType getSecondary() { return type; }

    public static class Builder extends QueryBuilderRelationshipClassificationTypes<Transaction, TransactionType, Builder,
            TransactionXTransactionType, UUID, TransactionXTransactionTypeSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, TransactionType> getSecondaryAttribute() { return getAttribute("type"); }
        @Override public Builder withType(String typeValue, ISystems<?, ?> system, UUID... identityToken) {
            if (typeValue != null) {
                JoinExpression<?, ?, ?> joined = new JoinExpression<>();
                join(getAttribute("type"), JoinType.INNER, joined);
                getFilters().add(joined.getFilter("code", com.entityassist.enumerations.Operand.Equals, typeValue));
            }
            return this;
        }
    }
}

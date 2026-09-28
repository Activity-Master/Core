package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_transaction_type_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXTransactionTypeSecurityToken extends WarehouseSecurityTable<TransactionXTransactionTypeSecurityToken,
        TransactionXTransactionTypeSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_transaction_type_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_transaction_type_id", referencedColumnName = "transaction_x_transaction_type_id", nullable = false)
    private TransactionXTransactionType base;

    public TransactionXTransactionTypeSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXTransactionTypeSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXTransactionType getBase() { return base; }
    public TransactionXTransactionTypeSecurityToken setBase(TransactionXTransactionType value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXTransactionTypeSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

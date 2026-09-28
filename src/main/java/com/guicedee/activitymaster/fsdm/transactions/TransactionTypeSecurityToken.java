package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_type_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionTypeSecurityToken extends WarehouseSecurityTable<TransactionTypeSecurityToken, TransactionTypeSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_type_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "transaction_type_id", nullable = false)
    private TransactionType base;

    public TransactionTypeSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionTypeSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionType getBase() { return base; }
    public TransactionTypeSecurityToken setBase(TransactionType value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionTypeSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

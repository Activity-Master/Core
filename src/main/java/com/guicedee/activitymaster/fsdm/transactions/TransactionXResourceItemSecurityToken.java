package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_resource_item_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXResourceItemSecurityToken extends WarehouseSecurityTable<TransactionXResourceItemSecurityToken,
        TransactionXResourceItemSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_resource_item_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_resource_item_id", referencedColumnName = "transaction_x_resource_item_id", nullable = false)
    private TransactionXResourceItem base;

    public TransactionXResourceItemSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXResourceItemSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXResourceItem getBase() { return base; }
    public TransactionXResourceItemSecurityToken setBase(TransactionXResourceItem value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXResourceItemSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

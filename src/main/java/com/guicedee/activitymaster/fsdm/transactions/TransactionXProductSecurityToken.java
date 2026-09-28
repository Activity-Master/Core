package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_product_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXProductSecurityToken extends WarehouseSecurityTable<TransactionXProductSecurityToken,
        TransactionXProductSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_product_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_product_id", referencedColumnName = "transaction_x_product_id", nullable = false)
    private TransactionXProduct base;

    public TransactionXProductSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXProductSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXProduct getBase() { return base; }
    public TransactionXProductSecurityToken setBase(TransactionXProduct value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXProductSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

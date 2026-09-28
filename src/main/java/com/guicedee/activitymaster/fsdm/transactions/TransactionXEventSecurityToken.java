package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_event_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXEventSecurityToken extends WarehouseSecurityTable<TransactionXEventSecurityToken,
        TransactionXEventSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_event_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_event_id", referencedColumnName = "transaction_x_event_id", nullable = false)
    private TransactionXEvent base;

    public TransactionXEventSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXEventSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXEvent getBase() { return base; }
    public TransactionXEventSecurityToken setBase(TransactionXEvent value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXEventSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

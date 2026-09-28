package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "entry_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionSecurityToken extends WarehouseSecurityTable<TransactionSecurityToken, TransactionSecurityToken.Builder, UUID> {
    @Id @Column(name = "entry_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction base;

    public TransactionSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionSecurityToken setId(UUID id) { this.id = id; return this; }
    public Transaction getBase() { return base; }
    public TransactionSecurityToken setBase(Transaction value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

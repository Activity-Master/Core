package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_arrangement_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXArrangementSecurityToken extends WarehouseSecurityTable<TransactionXArrangementSecurityToken,
        TransactionXArrangementSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_arrangement_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_arrangement_id", referencedColumnName = "transaction_x_arrangement_id", nullable = false)
    private TransactionXArrangement base;

    public TransactionXArrangementSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXArrangementSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXArrangement getBase() { return base; }
    public TransactionXArrangementSecurityToken setBase(TransactionXArrangement value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXArrangementSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

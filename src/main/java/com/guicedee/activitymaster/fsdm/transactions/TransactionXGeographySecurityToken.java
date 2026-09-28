package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_geography_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXGeographySecurityToken extends WarehouseSecurityTable<TransactionXGeographySecurityToken,
        TransactionXGeographySecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_geography_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_geography_id", referencedColumnName = "transaction_x_geography_id", nullable = false)
    private TransactionXGeography base;

    public TransactionXGeographySecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXGeographySecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXGeography getBase() { return base; }
    public TransactionXGeographySecurityToken setBase(TransactionXGeography value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXGeographySecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

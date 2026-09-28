package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_classification_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXClassificationSecurityToken extends WarehouseSecurityTable<TransactionXClassificationSecurityToken,
        TransactionXClassificationSecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_classification_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_classification_id", referencedColumnName = "transaction_x_classification_id", nullable = false)
    private TransactionXClassification base;

    public TransactionXClassificationSecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXClassificationSecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXClassification getBase() { return base; }
    public TransactionXClassificationSecurityToken setBase(TransactionXClassification value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXClassificationSecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

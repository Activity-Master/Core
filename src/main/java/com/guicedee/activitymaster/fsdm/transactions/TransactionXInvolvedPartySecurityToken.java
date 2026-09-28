package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.UUID;

@Entity
@Table(schema = "transactions", name = "transaction_x_involved_party_security_token")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXInvolvedPartySecurityToken extends WarehouseSecurityTable<TransactionXInvolvedPartySecurityToken,
        TransactionXInvolvedPartySecurityToken.Builder, UUID> {
    @Id @Column(name = "transaction_x_involved_party_security_token_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_x_involved_party_id", referencedColumnName = "transaction_x_involved_party_id", nullable = false)
    private TransactionXInvolvedParty base;

    public TransactionXInvolvedPartySecurityToken() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionXInvolvedPartySecurityToken setId(UUID id) { this.id = id; return this; }
    public TransactionXInvolvedParty getBase() { return base; }
    public TransactionXInvolvedPartySecurityToken setBase(TransactionXInvolvedParty value) { base = value; return this; }

    public static class Builder extends QueryBuilderSecurities<Builder, TransactionXInvolvedPartySecurityToken, UUID> {
        @Override protected Attribute getMyAttribute() { return getAttribute("base"); }
    }
}

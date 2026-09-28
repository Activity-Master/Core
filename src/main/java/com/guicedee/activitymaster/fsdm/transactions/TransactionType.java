package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

/** Enterprise-owned, secured transaction classification and accounting direction. */
@Entity
@Table(schema = "transactions", name = "transaction_type")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionType extends WarehouseSCDTable<TransactionType, TransactionType.Builder, UUID, TransactionTypeSecurityToken> {
    @Id @Column(name = "transaction_type_id", nullable = false)
    private UUID id;
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "direction", nullable = false)
    private short direction;
    @Column(name = "active", nullable = false)
    private boolean active = true;
    @OneToMany(mappedBy = "base")
    private List<TransactionTypeSecurityToken> securities;
    @OneToMany(mappedBy = "type")
    private List<TransactionXTransactionType> transactions;

    public TransactionType() { }
    @Override public UUID getId() { return id; }
    @Override public TransactionType setId(UUID id) { this.id = id; return this; }
    public String getCode() { return code; }
    public TransactionType setCode(String value) { code = value; return this; }
    public String getDescription() { return description; }
    public TransactionType setDescription(String value) { description = value; return this; }
    public short getDirection() { return direction; }
    public TransactionType setDirection(short value) { direction = value; return this; }
    public boolean isActive() { return active; }
    public TransactionType setActive(boolean value) { active = value; return this; }
    public List<TransactionTypeSecurityToken> getSecurities() { return securities; }
    public TransactionType setSecurities(List<TransactionTypeSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionTypeSecurityToken security) { security.setBase(this); }

    public static class Builder extends QueryBuilderSCD<Builder, TransactionType, UUID, TransactionTypeSecurityToken.Builder> { }
}

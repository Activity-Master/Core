package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_resource_item")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXResourceItem extends WarehouseClassificationRelationshipTable<Transaction, ResourceItem,
        TransactionXResourceItem, TransactionXResourceItem.Builder, UUID, TransactionXResourceItemSecurityToken> {
    @Id @Column(name = "transaction_x_resource_item_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_item_id", referencedColumnName = "ResourceItemID", nullable = false)
    private ResourceItem resourceItemID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXResourceItemSecurityToken> securities;

    public TransactionXResourceItem() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXResourceItem setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXResourceItem setTransaction(Transaction value) { transaction = value; return this; }
    public ResourceItem getResourceItemID() { return resourceItemID; }
    public TransactionXResourceItem setResourceItemID(ResourceItem value) { resourceItemID = value; return this; }
    public List<TransactionXResourceItemSecurityToken> getSecurities() { return securities; }
    public TransactionXResourceItem setSecurities(List<TransactionXResourceItemSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXResourceItemSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public ResourceItem getSecondary() { return getResourceItemID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, ResourceItem, Builder,
            TransactionXResourceItem, UUID, TransactionXResourceItemSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, ResourceItem> getSecondaryAttribute() { return getAttribute("resourceItemID"); }
    }
}

package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_product")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXProduct extends WarehouseClassificationRelationshipTable<Transaction, Product,
        TransactionXProduct, TransactionXProduct.Builder, UUID, TransactionXProductSecurityToken> {
    @Id @Column(name = "transaction_x_product_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "ProductID", nullable = false)
    private Product productID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXProductSecurityToken> securities;

    public TransactionXProduct() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXProduct setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXProduct setTransaction(Transaction value) { transaction = value; return this; }
    public Product getProductID() { return productID; }
    public TransactionXProduct setProductID(Product value) { productID = value; return this; }
    public List<TransactionXProductSecurityToken> getSecurities() { return securities; }
    public TransactionXProduct setSecurities(List<TransactionXProductSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXProductSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Product getSecondary() { return getProductID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Product, Builder,
            TransactionXProduct, UUID, TransactionXProductSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Product> getSecondaryAttribute() { return getAttribute("productID"); }
    }
}

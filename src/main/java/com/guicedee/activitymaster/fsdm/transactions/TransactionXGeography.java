package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_geography")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXGeography extends WarehouseClassificationRelationshipTable<Transaction, Geography,
        TransactionXGeography, TransactionXGeography.Builder, UUID, TransactionXGeographySecurityToken> {
    @Id @Column(name = "transaction_x_geography_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "geography_id", referencedColumnName = "GeographyID", nullable = false)
    private Geography geographyID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXGeographySecurityToken> securities;

    public TransactionXGeography() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXGeography setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXGeography setTransaction(Transaction value) { transaction = value; return this; }
    public Geography getGeographyID() { return geographyID; }
    public TransactionXGeography setGeographyID(Geography value) { geographyID = value; return this; }
    public List<TransactionXGeographySecurityToken> getSecurities() { return securities; }
    public TransactionXGeography setSecurities(List<TransactionXGeographySecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXGeographySecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Geography getSecondary() { return getGeographyID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Geography, Builder,
            TransactionXGeography, UUID, TransactionXGeographySecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Geography> getSecondaryAttribute() { return getAttribute("geographyID"); }
    }
}

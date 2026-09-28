package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.UUID;

/** Role-classified FSDM relationship with warehouse lifecycle and row security. */
@Entity
@Table(schema = "transactions", name = "transaction_x_address")
@AssociationOverride(name = "enterpriseID", joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "EnterpriseID"))
public class TransactionXAddress extends WarehouseClassificationRelationshipTable<Transaction, Address,
        TransactionXAddress, TransactionXAddress.Builder, UUID, TransactionXAddressSecurityToken> {
    @Id @Column(name = "transaction_x_address_id", nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "entry_id", nullable = false)
    private Transaction transaction;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", referencedColumnName = "AddressID", nullable = false)
    private Address addressID;
    @OneToMany(mappedBy = "base")
    private List<TransactionXAddressSecurityToken> securities;

    public TransactionXAddress() { setValue("1"); }
    @Override public UUID getId() { return id; }
    @Override public TransactionXAddress setId(UUID value) { id = value; return this; }
    public Transaction getTransaction() { return transaction; }
    public TransactionXAddress setTransaction(Transaction value) { transaction = value; return this; }
    public Address getAddressID() { return addressID; }
    public TransactionXAddress setAddressID(Address value) { addressID = value; return this; }
    public List<TransactionXAddressSecurityToken> getSecurities() { return securities; }
    public TransactionXAddress setSecurities(List<TransactionXAddressSecurityToken> value) { securities = value; return this; }
    @Override public void configureSecurityEntity(TransactionXAddressSecurityToken security) { security.setBase(this); }
    @Override public Transaction getPrimary() { return transaction; }
    @Override public Address getSecondary() { return getAddressID(); }

    public static class Builder extends QueryBuilderRelationshipClassification<Transaction, Address, Builder,
            TransactionXAddress, UUID, TransactionXAddressSecurityToken.Builder> {
        @Override public Attribute<?, Transaction> getPrimaryAttribute() { return getAttribute("transaction"); }
        @Override public Attribute<?, Address> getSecondaryAttribute() { return getAttribute("addressID"); }
    }
}

package com.guicedee.activitymaster.fsdm.db.entities.address;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseNameAndDescriptionTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddressType;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.entities.address.builders.AddressTypeQueryBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static jakarta.persistence.FetchType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "AddressType",
        schema = "Address")
@XmlRootElement
@Access(AccessType.FIELD)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressType
        extends WarehouseSCDTable<AddressType, AddressTypeQueryBuilder, UUID, AddressTypeSecurityToken>
        implements IAddressType<AddressType, AddressTypeQueryBuilder>,
        IWarehouseNameAndDescriptionTable<AddressType, AddressTypeQueryBuilder, UUID>
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "AddressTypeID")
    @JsonValue

    private java.util.UUID id;
    @Basic(optional = false,
            fetch = EAGER)
    @NotNull
    @Size(min = 1,
            max = 100)
    @Column(nullable = false,
            length = 100,
            name = "AddressTypeName")
    private String name;
    @Basic(optional = false,
            fetch = EAGER)
    @NotNull
    @Column(nullable = false,
            name = "AddressTypeDesc")
    private String description;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<AddressTypeSecurityToken> securities;



    public AddressType(UUID addressTypeID, String addressTypeName, String addressTypeDesc)
    {
        id = addressTypeID;
        name = addressTypeName;
        description = addressTypeDesc;
    }

    @Override
    public void configureSecurityEntity(AddressTypeSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<AddressTypeSecurityToken> getSecurities()
    {
        return securities;
    }

    public AddressType setSecurities(List<AddressTypeSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        AddressType that = (AddressType) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public AddressType setName(String name)
    {
        this.name = name;
        return this;
    }

    @Override
    public @NotNull String getDescription()
    {
        return description;
    }

    @Override
    public AddressType setDescription(@NotNull String description)
    {
        this.description = description;
        return this;
    }
}

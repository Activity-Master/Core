package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders.ResourceItemDataXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Resource", name = "ResourceItemDataXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceItemDataXClassificationSecurityToken
        extends WarehouseSecurityTable<ResourceItemDataXClassificationSecurityToken, ResourceItemDataXClassificationSecurityTokenQueryBuilder, UUID>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ResourceItemDataXClassificationSecurityTokenID")

    private java.util.UUID id;

    @JoinColumn(name = "ResourceItemDataXClassificationID",
            referencedColumnName = "ResourceItemDataXClassificationID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private ResourceItemDataXClassification base;

    public String toString()
    {
        return "ResourceItemDataXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
    }

    public ResourceItemDataXClassification getBase()
    {
        return this.base;
    }

    public ResourceItemDataXClassificationSecurityToken setBase(ResourceItemDataXClassification base)
    {
        this.base = base;
        return this;
    }

    public boolean equals(final Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof ResourceItemDataXClassificationSecurityToken))
        {
            return false;
        }
        final ResourceItemDataXClassificationSecurityToken other = (ResourceItemDataXClassificationSecurityToken) o;
        if (!other.canEqual(this))
        {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return this$id == null ? other$id == null : this$id.equals(other$id);
    }

    protected boolean canEqual(final Object other)
    {
        return other instanceof ResourceItemDataXClassificationSecurityToken;
    }

    public int hashCode()
    {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }
}

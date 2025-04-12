/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.hierarchies;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseHierarchyView;
import com.guicedee.activitymaster.fsdm.db.hierarchies.builders.ResourceItemHierarchyViewQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Marc Magon
 */
@Entity
@Table(name = "ResourceItemHierarchyView")
@XmlRootElement

@Immutable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceItemHierarchyView
        extends WarehouseHierarchyView<ResourceItemHierarchyView, ResourceItemHierarchyViewQueryBuilder, UUID>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    private java.util.UUID id;

    public boolean equals(final Object o)
    {
        if (o == this)
        {
            return true;
        }
        if (!(o instanceof ResourceItemHierarchyView))
        {
            return false;
        }
        final ResourceItemHierarchyView other = (ResourceItemHierarchyView) o;
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
        return other instanceof ResourceItemHierarchyView;
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

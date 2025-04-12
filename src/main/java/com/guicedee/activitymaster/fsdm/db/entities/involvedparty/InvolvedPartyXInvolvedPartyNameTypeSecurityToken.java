package com.guicedee.activitymaster.fsdm.db.entities.involvedparty;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder;
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
@Table(schema = "Party", name = "InvolvedPartyXInvolvedPartyNameTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvolvedPartyXInvolvedPartyNameTypeSecurityToken
        extends WarehouseSecurityTable<InvolvedPartyXInvolvedPartyNameTypeSecurityToken, InvolvedPartyXInvolvedPartyNameTypeSecurityTokenQueryBuilder, UUID>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "InvolvedPartyXInvolvedPartyNameTypeSecurityTokenID")

    private java.util.UUID id;
    @JoinColumn(name = "InvolvedPartyXInvolvedPartyNameTypeID",
            referencedColumnName = "InvolvedPartyXInvolvedPartyNameTypeID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private InvolvedPartyXInvolvedPartyNameType base;

    public String toString()
    {
        return "InvolvedPartyXInvolvedPartyNameTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
    }

    public InvolvedPartyXInvolvedPartyNameType getBase()
    {
        return this.base;
    }

    public InvolvedPartyXInvolvedPartyNameTypeSecurityToken setBase(InvolvedPartyXInvolvedPartyNameType base)
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
        if (!(o instanceof InvolvedPartyXInvolvedPartyNameTypeSecurityToken))
        {
            return false;
        }
        final InvolvedPartyXInvolvedPartyNameTypeSecurityToken other = (InvolvedPartyXInvolvedPartyNameTypeSecurityToken) o;
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
        return other instanceof InvolvedPartyXInvolvedPartyNameTypeSecurityToken;
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

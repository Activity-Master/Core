package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokensSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Security", name = "SecurityTokensSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityTokensSecurityToken
        extends WarehouseSecurityTable<SecurityTokensSecurityToken,
        SecurityTokensSecurityTokenQueryBuilder, UUID>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id

    @Column(nullable = false,
            name = "SecurityTokenAccessID")

    private java.util.UUID id;

    @JoinColumn(name = "SecurityTokenToID",
            referencedColumnName = "SecurityTokenID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private SecurityToken base;

    public SecurityToken getBase()
    {
        return base;
    }

    public SecurityTokensSecurityToken setBase(SecurityToken base)
    {
        this.base = base;
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
        SecurityTokensSecurityToken that = (SecurityTokensSecurityToken) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}

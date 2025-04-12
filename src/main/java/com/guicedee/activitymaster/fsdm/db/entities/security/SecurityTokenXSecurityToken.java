package com.guicedee.activitymaster.fsdm.db.entities.security;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenXSecurityTokenQueryBuilder;
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

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Security", name = "SecurityTokenXSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
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
public class SecurityTokenXSecurityToken
        extends WarehouseClassificationRelationshipTable<SecurityToken,
        SecurityToken,
        SecurityTokenXSecurityToken,
        SecurityTokenXSecurityTokenQueryBuilder,
        UUID,
        SecurityTokenXSecurityTokenSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "SecurityTokenXSecurityTokenID")

    private java.util.UUID id;

    @JoinColumn(name = "ParentSecurityTokenID",
            referencedColumnName = "SecurityTokenID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private SecurityToken parentSecurityTokenID;

    @JoinColumn(name = "ChildSecurityTokenID",
            referencedColumnName = "SecurityTokenID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private SecurityToken childSecurityTokenID;

    @Override
    public void configureSecurityEntity(SecurityTokenXSecurityTokenSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public SecurityToken getParentSecurityTokenID()
    {
        return this.parentSecurityTokenID;
    }

    public SecurityTokenXSecurityToken setParentSecurityTokenID(SecurityToken parentSecurityTokenID)
    {
        this.parentSecurityTokenID = parentSecurityTokenID;
        return this;
    }

    public SecurityToken getChildSecurityTokenID()
    {
        return this.childSecurityTokenID;
    }

    public SecurityTokenXSecurityToken setChildSecurityTokenID(SecurityToken childSecurityTokenID)
    {
        this.childSecurityTokenID = childSecurityTokenID;
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
        SecurityTokenXSecurityToken that = (SecurityTokenXSecurityToken) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }

    @Override
    public SecurityToken getPrimary()
    {
        return getParentSecurityTokenID();
    }

    @Override
    public SecurityToken getSecondary()
    {
        return getChildSecurityTokenID();
    }
}

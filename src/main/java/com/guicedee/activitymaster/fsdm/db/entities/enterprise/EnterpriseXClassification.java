package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseXClassificationQueryBuilder;
import jakarta.persistence.*;
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

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EnterpriseXClassification",
        schema = "dbo")
@XmlRootElement
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
public class EnterpriseXClassification
        extends WarehouseClassificationRelationshipTable<Enterprise,
        Classification,
        EnterpriseXClassification,
        EnterpriseXClassificationQueryBuilder,
        UUID,
        EnterpriseXClassificationSecurityToken
        >
{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "EnterpriseXClassificationID")

    private java.util.UUID id;

    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<EnterpriseXClassificationSecurityToken> securities;


    @Override
    public void configureSecurityEntity(EnterpriseXClassificationSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }

    public List<EnterpriseXClassificationSecurityToken> getSecurities()
    {
        return securities;
    }

    public EnterpriseXClassification setSecurities(List<EnterpriseXClassificationSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    @Override
    public Enterprise getPrimary()
    {
        return getEnterpriseID();
    }

    @Override
    public Classification getSecondary()
    {
        return getClassificationID();
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
        EnterpriseXClassification that = (EnterpriseXClassification) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}

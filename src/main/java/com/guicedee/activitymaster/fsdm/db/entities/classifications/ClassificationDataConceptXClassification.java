package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.fasterxml.jackson.annotation.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationDataConceptXClassificationQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
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
@Table(schema = "Classification", name = "ClassificationDataConceptXClassification")
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
public class ClassificationDataConceptXClassification
        extends WarehouseClassificationRelationshipTable<ClassificationDataConcept,
        Classification,
        ClassificationDataConceptXClassification,
        ClassificationDataConceptXClassificationQueryBuilder,
        UUID,
        ClassificationDataConceptXClassificationSecurityToken>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ClassificationDataConceptXClassificationID")

    private java.util.UUID id;
    @OneToMany(
            mappedBy = "base",
            fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<ClassificationDataConceptXClassificationSecurityToken> securities;

    @JoinColumn(name = "ClassificationDataConceptID",
            referencedColumnName = "ClassificationDataConceptID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private ClassificationDataConcept classificationDataConceptID;

    @Override
    public void configureSecurityEntity(ClassificationDataConceptXClassificationSecurityToken securityEntity)
    {
        securityEntity.setBase(this);
    }


    public List<ClassificationDataConceptXClassificationSecurityToken> getSecurities()
    {
        return this.securities;
    }

    public ClassificationDataConceptXClassification setSecurities(List<ClassificationDataConceptXClassificationSecurityToken> securities)
    {
        this.securities = securities;
        return this;
    }

    public ClassificationDataConcept getClassificationDataConceptID()
    {
        return this.classificationDataConceptID;
    }

    public ClassificationDataConceptXClassification setClassificationDataConceptID(ClassificationDataConcept classificationDataConceptID)
    {
        this.classificationDataConceptID = classificationDataConceptID;
        return this;
    }

    @Override
    public ClassificationDataConcept getPrimary()
    {
        return getClassificationDataConceptID();
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
        ClassificationDataConceptXClassification that = (ClassificationDataConceptXClassification) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}

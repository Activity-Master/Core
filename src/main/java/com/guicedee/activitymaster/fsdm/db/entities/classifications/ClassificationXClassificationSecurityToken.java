package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationXClassificationSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationXClassificationSecurityToken
        extends WarehouseSecurityTable<ClassificationXClassificationSecurityToken, ClassificationXClassificationSecurityTokenQueryBuilder, UUID>
        implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Id

    @Column(nullable = false,
            name = "ClassificationXClassificationSecurityTokenID")

    private java.util.UUID id;

    @JoinColumn(name = "ClassificationXClassificationID",
            referencedColumnName = "ClassificationXClassificationID",
            nullable = false)
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)

    private ClassificationXClassification base;

    public String toString()
    {
        return "ClassificationXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
    }

    public ClassificationXClassificationSecurityToken setBase(ClassificationXClassification base)
    {
        this.base = base;
        return this;
    }


    public ClassificationXClassification getBase()
    {
        return base;
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
        ClassificationXClassificationSecurityToken that = (ClassificationXClassificationSecurityToken) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(getId());
    }
}

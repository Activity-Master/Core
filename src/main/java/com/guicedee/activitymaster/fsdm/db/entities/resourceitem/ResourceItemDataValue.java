package com.guicedee.activitymaster.fsdm.db.entities.resourceitem;

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

@Entity
@Table(schema = "resource", name = "resourceitemdatavalue")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceItemDataValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, name = "resourceitemdatavalueid")
    private UUID id;

    @Column(nullable = false, name = "resourceitemdatavalue")
    private byte[] data;

    @OneToOne(mappedBy = "dataValue", fetch = FetchType.LAZY)
    private ResourceItemData resourceItemData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceItemDataValue that = (ResourceItemDataValue) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return Objects.toString(getId());
    }
}

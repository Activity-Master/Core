package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementTypeXClassificationSecurityTokenQueryBuilder;
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
@Table(schema = "Arrangement",
       name = "ArrangementTypeXClassificationSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrangementTypeXClassificationSecurityToken
		extends WarehouseSecurityTable<ArrangementTypeXClassificationSecurityToken, ArrangementTypeXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationSecurityTokenID")

	private java.util.UUID id;
	
	@JoinColumn(name = "ArrangementTypeXClassificationID",
	            referencedColumnName = "ArrangementTypeXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementTypeXClassification base;

	@Override
	public String toString()
	{
		return "ArrangementTypeXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public ArrangementTypeXClassificationSecurityToken setBase(ArrangementTypeXClassification base)
	{
		this.base = base;
		return this;
	}
	
	public ArrangementTypeXClassification getBase()
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
		ArrangementTypeXClassificationSecurityToken that = (ArrangementTypeXClassificationSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}

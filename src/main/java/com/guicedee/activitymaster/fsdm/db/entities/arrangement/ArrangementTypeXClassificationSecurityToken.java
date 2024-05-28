package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementTypeXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

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
public class ArrangementTypeXClassificationSecurityToken
		extends WarehouseSecurityTable<ArrangementTypeXClassificationSecurityToken, ArrangementTypeXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ArrangementTypeXClassificationID",
	            referencedColumnName = "ArrangementTypeXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementTypeXClassification base;
	
	public ArrangementTypeXClassificationSecurityToken()
	{
	
	}
	
	public ArrangementTypeXClassificationSecurityToken(java.lang.String arrangementXClassificationSecurityTokenID)
	{
		this.id = arrangementXClassificationSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "ArrangementTypeXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public ArrangementTypeXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
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

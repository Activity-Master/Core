package com.guicedee.activitymaster.core.db.entities.arrangement;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementTypeXClassificationSecurityTokenQueryBuilder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementTypeXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ArrangementTypeXClassificationSecurityToken
		extends WarehouseSecurityTable<ArrangementTypeXClassificationSecurityToken, ArrangementTypeXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationSecurityTokenID")
	private Long id;
	
	@JoinColumn(name = "ArrangementTypeXClassificationID",
	            referencedColumnName = "ArrangementTypeXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementTypeXClassification base;
	
	public ArrangementTypeXClassificationSecurityToken()
	{
	
	}
	
	public ArrangementTypeXClassificationSecurityToken(Long arrangementXClassificationSecurityTokenID)
	{
		this.id = arrangementXClassificationSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "ArrangementTypeXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public Long getId()
	{
		return this.id;
	}
	
	public ArrangementTypeXClassification getBase()
	{
		return this.base;
	}
	
	@Override
	public ArrangementTypeXClassificationSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementTypeXClassificationSecurityToken setBase(ArrangementTypeXClassification base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ArrangementTypeXClassificationSecurityToken))
		{
			return false;
		}
		final ArrangementTypeXClassificationSecurityToken other = (ArrangementTypeXClassificationSecurityToken) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}
	
	protected boolean canEqual(final Object other)
	{
		return other instanceof ArrangementTypeXClassificationSecurityToken;
	}
	
	@Override
	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}

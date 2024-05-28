package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesSecurityTokenQueryBuilder;
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
@Table(schema = "Arrangement", name = "ArrangementXRulesSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
public class ArrangementXRulesSecurityToken
		extends WarehouseSecurityTable<ArrangementXRulesSecurityToken, ArrangementXRulesSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ArrangementXRulesID",
	            referencedColumnName = "ArrangementXRulesID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXRules base;
	
	public ArrangementXRulesSecurityToken()
	{
	
	}
	
	public ArrangementXRulesSecurityToken(java.lang.String arrangementXRulesSecurityTokenID)
	{
		this.id = arrangementXRulesSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXRulesSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ArrangementXRulesSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXRulesSecurityToken setBase(ArrangementXRules base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ArrangementXRules getBase()
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
		ArrangementXRulesSecurityToken that = (ArrangementXRulesSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}

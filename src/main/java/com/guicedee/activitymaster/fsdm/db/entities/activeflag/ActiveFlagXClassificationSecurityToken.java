package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "ActiveFlagXClassificationSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
public class ActiveFlagXClassificationSecurityToken
		extends WarehouseSecurityTable<ActiveFlagXClassificationSecurityToken, ActiveFlagXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ActiveFlagXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ActiveFlagXClassificationID",
	            referencedColumnName = "ActiveFlagXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ActiveFlagXClassification base;
	
	public ActiveFlagXClassificationSecurityToken()
	{
	
	}
	
	public ActiveFlagXClassificationSecurityToken(java.lang.String activeFlagXClassificationSecurityTokenID)
	{
		this.id = activeFlagXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ActiveFlagXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ActiveFlagXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ActiveFlagXClassificationSecurityToken setBase(ActiveFlagXClassification base)
	{
		this.base = base;
		return this;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public ActiveFlagXClassification getBase()
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
		ActiveFlagXClassificationSecurityToken that = (ActiveFlagXClassificationSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}

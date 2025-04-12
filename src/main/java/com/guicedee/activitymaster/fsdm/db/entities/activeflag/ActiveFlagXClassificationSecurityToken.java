package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "ActiveFlagXClassificationSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActiveFlagXClassificationSecurityToken
		extends WarehouseSecurityTable<ActiveFlagXClassificationSecurityToken, ActiveFlagXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ActiveFlagXClassificationSecurityTokenID")

	private java.util.UUID id;
	
	@JoinColumn(name = "ActiveFlagXClassificationID",
	            referencedColumnName = "ActiveFlagXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ActiveFlagXClassification base;
	

	public String toString()
	{
		return "ActiveFlagXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public ActiveFlagXClassificationSecurityToken setBase(ActiveFlagXClassification base)
	{
		this.base = base;
		return this;
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

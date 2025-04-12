package com.guicedee.activitymaster.fsdm.db.entities.enterprise;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseSecurityTokenQueryBuilder;
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
@Table(name = "EnterpriseSecurityToken",
       schema = "dbo")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseSecurityToken
		extends WarehouseSecurityTable<EnterpriseSecurityToken, EnterpriseSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "EnterpriseSecurityTokenID")
	
	private java.util.UUID id;
	
	@JoinColumn(name = "EnterpriseID",
	            referencedColumnName = "EnterpriseID",
	            nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private Enterprise base;

	public String toString()
	{
		return "EnterpriseSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public EnterpriseSecurityToken setBase(Enterprise base)
	{
		this.base = base;
		return this;
	}

	public Enterprise getBase()
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
		EnterpriseSecurityToken that = (EnterpriseSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}

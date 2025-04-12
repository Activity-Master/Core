package com.guicedee.activitymaster.fsdm.db.entities.classifications;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationXResourceItemSecurityTokenQueryBuilder;
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
@Table(schema = "Classification", name = "ClassificationXResourceItemSecurityToken")
@XmlRootElement
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationXResourceItemSecurityToken
		extends WarehouseSecurityTable<ClassificationXResourceItemSecurityToken, ClassificationXResourceItemSecurityTokenQueryBuilder, UUID>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ClassificationXResourceItemSecurityTokenID")
	
	private java.util.UUID id;
	
	@JoinColumn(name = "ClassificationXResourceItemID",
	            referencedColumnName = "ClassificationXResourceItemID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ClassificationXResourceItem base;

	public String toString()
	{
		return "ClassificationXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	
	public ClassificationXResourceItemSecurityToken setBase(ClassificationXResourceItem base)
	{
		this.base = base;
		return this;
	}
	
	public ClassificationXResourceItem getBase()
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
		ClassificationXResourceItemSecurityToken that = (ClassificationXResourceItemSecurityToken) o;
		return Objects.equals(getId(), that.getId());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getId());
	}
}

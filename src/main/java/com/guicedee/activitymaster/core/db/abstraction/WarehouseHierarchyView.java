package com.guicedee.activitymaster.core.db.abstraction;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderHierarchyView;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@XmlRootElement

@Immutable
@MappedSuperclass
public abstract class WarehouseHierarchyView <J extends WarehouseHierarchyView<J, Q, I>, Q extends QueryBuilderHierarchyView<Q, J, I>, I extends java.util.UUID>
		extends BaseEntity<J, Q, I>
{
	@Column
	private String name;
	@Column
	@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID parentID;
	@Column
	private String pather;
	@Column
	private String path;
/*	@Column
	private String value;*/
	@Column
	private Integer one;

	public String getName()
	{
		return this.name;
	}

	public UUID getParentID()
	{
		return this.parentID;
	}

	public String getPather()
	{
		return this.pather;
	}

	public String getPath()
	{
		return this.path;
	}

	public Integer getOne()
	{
		return this.one;
	}

/*	public String getValue() {
		return value;
	}

	public WarehouseHierarchyView<J, Q, I> setValue(String value) {
		this.value = value;
		return this;
	}*/

	public WarehouseHierarchyView<J, Q, I> setName(String name)
	{
		this.name = name;
		return this;
	}

	public WarehouseHierarchyView<J, Q, I> setParentID(UUID parentID)
	{
		this.parentID = parentID;
		return this;
	}

	public WarehouseHierarchyView<J, Q, I> setPather(String pather)
	{
		this.pather = pather;
		return this;
	}

	public WarehouseHierarchyView<J, Q, I> setPath(String path)
	{
		this.path = path;
		return this;
	}

	public WarehouseHierarchyView<J, Q, I> setOne(Integer one)
	{
		this.one = one;
		return this;
	}

	public String toString()
	{
		return "WarehouseHierarchyView(name=" + this.getName() + ")";
	}
}

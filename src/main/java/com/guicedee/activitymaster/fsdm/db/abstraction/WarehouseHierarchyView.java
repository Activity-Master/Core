package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.entityassist.BaseEntity;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderHierarchyView;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@XmlRootElement

@Immutable
@MappedSuperclass
public abstract class WarehouseHierarchyView<J extends WarehouseHierarchyView<J, Q, I>, Q extends QueryBuilderHierarchyView<Q, J, I>, I extends UUID>
		extends BaseEntity<J, Q, I>
{
	@Column
	private String name;
	@Column
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
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
	
	public WarehouseHierarchyView<J, Q, I> setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public UUID getParentID()
	{
		return this.parentID;
	}
	
	public WarehouseHierarchyView<J, Q, I> setParentID(UUID parentID)
	{
		this.parentID = parentID;
		return this;
	}
	
	public String getPather()
	{
		return this.pather;
	}

/*	public String getValue() {
		return value;
	}

	public WarehouseHierarchyView<J, Q, I> setValue(String value) {
		this.value = value;
		return this;
	}*/
	
	public WarehouseHierarchyView<J, Q, I> setPather(String pather)
	{
		this.pather = pather;
		return this;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public WarehouseHierarchyView<J, Q, I> setPath(String path)
	{
		this.path = path;
		return this;
	}
	
	public Integer getOne()
	{
		return this.one;
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

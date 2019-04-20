package com.armineasy.activitymaster.activitymaster.db.abstraction;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderHierarchyView;
import com.jwebmp.entityassist.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@Accessors(chain = true)
@Getter
@Setter
@ToString(of = "name")
@Immutable
@MappedSuperclass
public abstract class WarehouseHierarchyView <J extends WarehouseHierarchyView<J, Q, I>, Q extends QueryBuilderHierarchyView<Q, J, I>, I extends Serializable>
		extends BaseEntity<J, Q, I>
{
	@Column
	private String name;
	@Column
	private Long parentID;
	@Column
	private String pather;
	@Column
	private String path;
	@Column
	private Integer one;
}

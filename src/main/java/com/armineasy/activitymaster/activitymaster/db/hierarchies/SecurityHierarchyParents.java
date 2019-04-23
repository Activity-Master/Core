/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.hierarchies;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseBaseTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.builders.SecurityHierarchyParentsQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.builders.SecurityHierarchyViewQueryBuilder;
import com.jwebmp.entityassist.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "SecurityHierarchyParents")
@XmlRootElement
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)

@Immutable
public class SecurityHierarchyParents
		extends BaseEntity<SecurityHierarchyParents, SecurityHierarchyParentsQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "id",insertable = false,updatable = false,referencedColumnName = "id")
	private SecurityHierarchyView child;
	@Column(name = "Value")
	private Long value;

	public SecurityHierarchyParents()
	{

	}


}

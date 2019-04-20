/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.hierarchies;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.builders.SecurityHierarchyViewQueryBuilder;
import com.jwebmp.entityassist.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author GedMarc
 */
@Entity
@Table(name = "SecurityHierarchyView")
@XmlRootElement
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)

@Immutable
public class SecurityHierarchyView
		extends WarehouseHierarchyView<SecurityHierarchyView, SecurityHierarchyViewQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;

	public SecurityHierarchyView()
	{
	}
}

package com.armineasy.activitymaster.activitymaster.db.entities.systems;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.builders.SystemXClassificationSecurityTokenQueryBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "SystemXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class SystemXClassificationSecurityToken
		extends WarehouseSecurityTable<SystemXClassificationSecurityToken, SystemXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "SystemXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "SystemXClassificationID",
			referencedColumnName = "SystemXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private SystemXClassification base;

	public SystemXClassificationSecurityToken()
	{

	}

	public SystemXClassificationSecurityToken(Long systemXClassificationSecurityTokenID)
	{
		this.id = systemXClassificationSecurityTokenID;
	}
}

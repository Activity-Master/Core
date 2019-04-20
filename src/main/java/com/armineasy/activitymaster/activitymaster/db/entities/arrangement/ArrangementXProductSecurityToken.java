package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXProductSecurityTokenQueryBuilder;
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
@Table(name = "ArrangementXProductSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ArrangementXProductSecurityToken
		extends WarehouseSecurityTable<ArrangementXProductSecurityToken, ArrangementXProductSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXProductSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ArrangementXProductID",
			referencedColumnName = "ArrangementXProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private ArrangementXProduct base;

	public ArrangementXProductSecurityToken()
	{

	}

	public ArrangementXProductSecurityToken(Long arrangementXProductSecurityTokenID)
	{
		this.id = arrangementXProductSecurityTokenID;
	}
}

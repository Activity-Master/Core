package com.armineasy.activitymaster.activitymaster.db.entities.arrangement;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "ArrangementXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ArrangementXClassificationSecurityToken
		extends WarehouseSecurityTable<ArrangementXClassificationSecurityToken, ArrangementXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ArrangementXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ArrangementXClassificationID",
			referencedColumnName = "ArrangementXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ArrangementXClassification base;

	public ArrangementXClassificationSecurityToken()
	{

	}

	public ArrangementXClassificationSecurityToken(Long arrangementXClassificationSecurityTokenID)
	{
		this.id = arrangementXClassificationSecurityTokenID;
	}
}

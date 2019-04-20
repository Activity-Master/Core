package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoXClassificationSecurityTokenQueryBuilder;
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
@Table(name = "YesNoXClassificationSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class YesNoXClassificationSecurityToken
		extends WarehouseSecurityTable<YesNoXClassificationSecurityToken, YesNoXClassificationSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoXClassificationSecurityTokenID")
	private Long id;

	@JoinColumn(name = "YesNoXClassificationID",
			referencedColumnName = "YesNoXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private YesNoXClassification base;

	public YesNoXClassificationSecurityToken()
	{

	}

	public YesNoXClassificationSecurityToken(Long systemXClassificationSecurityTokenID)
	{
		this.id = systemXClassificationSecurityTokenID;
	}
}

package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoXClassificationQueryBuilder;
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
@Table(name = "YesNoXClassification")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class YesNoXClassification
		extends WarehouseClassificationRelationshipTable<YesNo, Classification, YesNoXClassification, YesNoXClassificationQueryBuilder, Long, YesNoXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoXClassificationID")
	private Long id;

	@JoinColumn(name = "YesNoID",
			referencedColumnName = "YesNoID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private YesNo yesNoID;

	public YesNoXClassification()
	{

	}

	public YesNoXClassification(Long yesNoXClassificationID)
	{
		this.id = yesNoXClassificationID;
	}

	@Override
	protected YesNoXClassificationSecurityToken configureDefaultsForNewToken(YesNoXClassificationSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}

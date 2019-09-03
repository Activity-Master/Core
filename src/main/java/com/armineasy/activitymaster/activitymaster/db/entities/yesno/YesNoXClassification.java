package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.dto.IYesNo;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "YesNoXClassification")
@XmlRootElement

@Access(FIELD)
public class YesNoXClassification
		extends WarehouseClassificationRelationshipTable<YesNo,
				                                                Classification,
				                                                YesNoXClassification,
				                                                YesNoXClassificationQueryBuilder,
				                                                Long,
				                                                YesNoXClassificationSecurityToken,
				                                                IYesNo<?>, IClassification<?>>
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
	protected YesNoXClassificationSecurityToken configureDefaultsForNewToken(YesNoXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
	
	public Long getId()
	{
		return this.id;
	}

	public YesNo getYesNoID()
	{
		return this.yesNoID;
	}

	public YesNoXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public YesNoXClassification setYesNoID(YesNo yesNoID)
	{
		this.yesNoID = yesNoID;
		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		YesNoXClassification that = (YesNoXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IYesNo<?> getPrimary()
	{
		return getYesNoID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getClassificationID();
	}
}

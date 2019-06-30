package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "YesNoXClassification")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
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
	protected YesNoXClassificationSecurityToken configureDefaultsForNewToken(YesNoXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public String toString()
	{
		return "YesNoXClassification(id=" + this.getId() + ", yesNoID=" + this.getYesNoID() + ")";
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

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof YesNoXClassification))
		{
			return false;
		}
		final YesNoXClassification other = (YesNoXClassification) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof YesNoXClassification;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}

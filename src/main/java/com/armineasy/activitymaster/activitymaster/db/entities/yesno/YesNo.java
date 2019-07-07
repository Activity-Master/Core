package com.armineasy.activitymaster.activitymaster.db.entities.yesno;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders.YesNoQueryBuilder;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.yesno.IYesNoClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.dto.IYesNo;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;
import static javax.persistence.FetchType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "YesNo")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class YesNo
		extends WarehouseTable<YesNo, YesNoQueryBuilder, Long, YesNoSecurityToken>
		implements IContainsClassifications<YesNo, Classification, YesNoXClassification, IYesNoClassification<?>,YesNo>,
				           IActivityMasterEntity<YesNo>,
				           IYesNo<YesNo>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoID")
	private Long id;

	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 5)
	@Column(nullable = false,
			length = 5,
			name = "YesNoDesc")
	private String yesNoDesc;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 6)
	@Column(nullable = false,
			length = 6,
			name = "BooleanDesc")
	private String booleanDesc;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 4)
	@Column(nullable = false,
			length = 4,
			name = "OnOffDesc")
	private String onOffDesc;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 10)
	@Column(nullable = false,
			length = 10,
			name = "ActiveDesc")
	private String activeDesc;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 1)
	@Column(nullable = false,
			length = 1,
			name = "YNDesc")
	private String yNDesc;
	@Basic(optional = false,fetch = EAGER)
	@NotNull
	@Size(min = 1,
			max = 4)
	@Column(nullable = false,
			length = 4,
			name = "InOutDesc")
	private String inOutDesc;

	@OneToMany(
			mappedBy = "yesNoID",
			fetch = FetchType.LAZY)
	private List<YesNoXClassification> yesNoXClassificationList;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<YesNoSecurityToken> securities;

	public YesNo()
	{

	}

	public YesNo(IEnterprise<?> enterprise, Systems system)
	{
		setEnterpriseID((Enterprise) enterprise);
		setSystemID(system);
	}

	public YesNo(Long yesNoID)
	{
		this.id = yesNoID;
	}

	public YesNo(Long yesNoID, String yesNoDesc, String booleanDesc, String onOffDesc, String activeDesc, String yNDesc, String inOutDesc)
	{
		this.id = yesNoID;
		this.yesNoDesc = yesNoDesc;
		this.booleanDesc = booleanDesc;
		this.onOffDesc = onOffDesc;
		this.activeDesc = activeDesc;
		this.yNDesc = yNDesc;
		this.inOutDesc = inOutDesc;
	}

	@Override
	public String toString()
	{
		return "YesNo - " + getYesNoDesc();
	}


	@Override
	protected YesNoSecurityToken configureDefaultsForNewToken(YesNoSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(YesNoXClassification classificationLink, IEnterprise<?> enterprise)
	{
		classificationLink.setYesNoID(this);
	}

	public List<YesNoXClassification> getYesNoXClassificationList()
	{
		return this.yesNoXClassificationList;
	}

	public List<YesNoSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public YesNo setYesNoXClassificationList(List<YesNoXClassification> yesNoXClassificationList)
	{
		this.yesNoXClassificationList = yesNoXClassificationList;
		return this;
	}

	public YesNo setSecurities(List<YesNoSecurityToken> securities)
	{
		this.securities = securities;
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
		YesNo yesNo = (YesNo) o;
		return Objects.equals(getYesNoDesc(), yesNo.getYesNoDesc());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getYesNoDesc());
	}


	public Long getId()
	{
		return this.id;
	}

	public @NotNull @Size(min = 1,
			max = 5) String getYesNoDesc()
	{
		return this.yesNoDesc;
	}

	public @NotNull @Size(min = 1,
			max = 6) String getBooleanDesc()
	{
		return this.booleanDesc;
	}

	public @NotNull @Size(min = 1,
			max = 4) String getOnOffDesc()
	{
		return this.onOffDesc;
	}

	public @NotNull @Size(min = 1,
			max = 10) String getActiveDesc()
	{
		return this.activeDesc;
	}

	public @NotNull @Size(min = 1,
			max = 1) String getYNDesc()
	{
		return this.yNDesc;
	}

	public @NotNull @Size(min = 1,
			max = 4) String getInOutDesc()
	{
		return this.inOutDesc;
	}

	public YesNo setId(Long id)
	{
		this.id = id;
		return this;
	}

	public YesNo setYesNoDesc(@NotNull @Size(min = 1,
			max = 5) String yesNoDesc)
	{
		this.yesNoDesc = yesNoDesc;
		return this;
	}

	public YesNo setBooleanDesc(@NotNull @Size(min = 1,
			max = 6) String booleanDesc)
	{
		this.booleanDesc = booleanDesc;
		return this;
	}

	public YesNo setOnOffDesc(@NotNull @Size(min = 1,
			max = 4) String onOffDesc)
	{
		this.onOffDesc = onOffDesc;
		return this;
	}

	public YesNo setActiveDesc(@NotNull @Size(min = 1,
			max = 10) String activeDesc)
	{
		this.activeDesc = activeDesc;
		return this;
	}

	public YesNo setYNDesc(@NotNull @Size(min = 1,
			max = 1) String yNDesc)
	{
		this.yNDesc = yNDesc;
		return this;
	}

	public YesNo setInOutDesc(@NotNull @Size(min = 1,
			max = 4) String inOutDesc)
	{
		this.inOutDesc = inOutDesc;
		return this;
	}
}

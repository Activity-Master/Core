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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class YesNo
		extends WarehouseTable<YesNo, YesNoQueryBuilder, Long, YesNoSecurityToken>
		implements IContainsClassifications<YesNo, Classification, YesNoXClassification, IYesNoClassification>,
				           IActivityMasterEntity<YesNo>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "YesNoID")
	@Getter
	@Setter
	private Long id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 5)
	@Column(nullable = false,
			length = 5,
			name = "YesNoDesc")
	@Getter
	@Setter
	private String yesNoDesc;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 6)
	@Column(nullable = false,
			length = 6,
			name = "BooleanDesc")
	@Getter
	@Setter
	private String booleanDesc;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 4)
	@Column(nullable = false,
			length = 4,
			name = "OnOffDesc")
	@Getter
	@Setter
	private String onOffDesc;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 10)
	@Column(nullable = false,
			length = 10,
			name = "ActiveDesc")
	@Getter
	@Setter
	private String activeDesc;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 1)
	@Column(nullable = false,
			length = 1,
			name = "YNDesc")
	@Getter
	@Setter
	private String yNDesc;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1,
			max = 4)
	@Column(nullable = false,
			length = 4,
			name = "InOutDesc")
	@Getter
	@Setter
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

	public YesNo(Enterprise enterprise, Systems system)
	{
		setEnterpriseID(enterprise);
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
		return getYesNoDesc();
	}


	@Override
	protected YesNoSecurityToken configureDefaultsForNewToken(YesNoSecurityToken stAdmin, IEnterprise enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	@Override
	public void configureForClassification(YesNoXClassification classificationLink, Enterprise enterprise)
	{
		classificationLink.setYesNoID(this);
	}
}

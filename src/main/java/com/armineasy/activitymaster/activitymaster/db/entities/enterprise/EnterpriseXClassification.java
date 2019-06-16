package com.armineasy.activitymaster.activitymaster.db.entities.enterprise;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.builders.EnterpriseXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoSecurityToken;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

import static javax.persistence.AccessType.*;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "EnterpriseXClassification")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
@Access(FIELD)@lombok.Data
public class EnterpriseXClassification
		extends WarehouseClassificationRelationshipTable<Enterprise, Classification, EnterpriseXClassification,
				                                                EnterpriseXClassificationQueryBuilder, Long, EnterpriseXClassificationSecurityToken>
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "EnterpriseXClassificationID")
	private Long id;


	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<EnterpriseXClassificationSecurityToken> securities;


	public EnterpriseXClassification()
	{

	}

	public EnterpriseXClassification(Long enterpriseXClassificationID)
	{
		this.id = enterpriseXClassificationID;
	}

	@Override
	protected EnterpriseXClassificationSecurityToken configureDefaultsForNewToken(EnterpriseXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}

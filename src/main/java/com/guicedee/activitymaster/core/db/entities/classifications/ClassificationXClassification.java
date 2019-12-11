package com.guicedee.activitymaster.core.db.entities.classifications;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.entities.classifications.builders.ClassificationXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static javax.persistence.AccessType.*;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Classification",name = "ClassificationXClassification")
@XmlRootElement
@Access(FIELD)
public class ClassificationXClassification
		extends WarehouseClassificationRelationshipTable<Classification,
						                                                Classification,
						                                                ClassificationXClassification,
				                                                ClassificationXClassificationQueryBuilder,
						                                                Long,
						                                                ClassificationXClassificationSecurityToken,
				                                                IClassification<?>,IClassification<?>>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ClassificationXClassificationID")
	private Long id;

	@JoinColumn(name = "ChildClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification childClassificationID;

	@JoinColumn(name = "ParentClassificationID",
			referencedColumnName = "ClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Classification parentClassificationID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ClassificationXClassificationSecurityToken> securities;

	public ClassificationXClassification()
	{

	}

	public ClassificationXClassification(Long classificationXClassificationID)
	{
		this.id = classificationXClassificationID;
	}

	@Override
	protected ClassificationXClassificationSecurityToken configureDefaultsForNewToken(ClassificationXClassificationSecurityToken stAdmin, IEnterprise<?> enterprise, ISystems<?> activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}

	public Long getId()
	{
		return this.id;
	}

	public Classification getChildClassificationID()
	{
		return this.childClassificationID;
	}

	public Classification getParentClassificationID()
	{
		return this.parentClassificationID;
	}

	public List<ClassificationXClassificationSecurityToken> getSecurities()
	{
		return this.securities;
	}

	public ClassificationXClassification setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ClassificationXClassification setChildClassificationID(Classification childClassificationID)
	{
		this.childClassificationID = childClassificationID;
		return this;
	}

	public ClassificationXClassification setParentClassificationID(Classification parentClassificationID)
	{
		this.parentClassificationID = parentClassificationID;
		return this;
	}



	public ClassificationXClassification setSecurities(List<ClassificationXClassificationSecurityToken> securities)
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
		ClassificationXClassification that = (ClassificationXClassification) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getId());
	}

	@Override
	public IClassification<?> getPrimary()
	{
		return getParentClassificationID();
	}

	@Override
	public IClassification<?> getSecondary()
	{
		return getChildClassificationID();
	}
}

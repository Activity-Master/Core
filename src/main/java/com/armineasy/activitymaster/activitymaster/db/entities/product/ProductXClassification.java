package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * @author GedMarc
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(name = "ProductXClassification")
@XmlRootElement
@Accessors(chain = true)
@Getter(onMethod = @__(@XmlTransient))
@Setter
@EqualsAndHashCode(of = "id",
		callSuper = false)
public class ProductXClassification
		extends WarehouseClassificationRelationshipTable<Product, Classification, ProductXClassification, ProductXClassificationQueryBuilder, Long, ProductXClassificationSecurityToken>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductXClassificationID")
	private Long id;

	@JoinColumn(name = "ProductID",
			referencedColumnName = "ProductID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)
	private Product productID;

	@OneToMany(
			mappedBy = "base",
			fetch = FetchType.LAZY)
	private List<ProductXClassificationSecurityToken> securities;

	public ProductXClassification()
	{

	}

	public ProductXClassification(Long productXClassificationID)
	{
		this.id = productXClassificationID;
	}

	@Override
	protected ProductXClassificationSecurityToken configureDefaultsForNewToken(ProductXClassificationSecurityToken stAdmin, Enterprise enterprise, Systems activityMasterSystem)
	{
		return super.configureDefaultsForNewToken(stAdmin, enterprise, activityMasterSystem)
		            .setBase(this);
	}
}

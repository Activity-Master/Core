package com.guicedee.activitymaster.core.db.entities.product;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.product.builders.ProductTypeXClassificationSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.AccessType.FIELD;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Entity
@Table(schema="Product",name = "ProductTypeXClassificationSecurityToken")
@XmlRootElement

@Access(FIELD)
public class ProductTypeXClassificationSecurityToken
		extends WarehouseSecurityTable<ProductTypeXClassificationSecurityToken, ProductTypeXClassificationSecurityTokenQueryBuilder, UUID>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id

	@Column(nullable = false,
			name = "ProductTypeXClassificationSecurityTokenID")@org.hibernate.annotations.Type(type = "uuid-char")
	private UUID id;

	@JoinColumn(name = "ProductTypeXClassificationID",
			referencedColumnName = "ProductTypeXClassificationID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ProductTypeXClassification base;

	public ProductTypeXClassificationSecurityToken()
	{

	}

	public ProductTypeXClassificationSecurityToken(UUID productXClassificationSecurityTokenID)
	{
		this.id = productXClassificationSecurityTokenID;
	}

	public String toString()
	{
		return "ProductTypeXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public UUID getId()
	{
		return this.id;
	}

	public ProductTypeXClassification getBase()
	{
		return this.base;
	}

	public ProductTypeXClassificationSecurityToken setId(UUID id)
	{
		this.id = id;
		return this;
	}

	public ProductTypeXClassificationSecurityToken setBase(ProductTypeXClassification base)
	{
		this.base = base;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof ProductTypeXClassificationSecurityToken))
		{
			return false;
		}
		final ProductTypeXClassificationSecurityToken other = (ProductTypeXClassificationSecurityToken) o;
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
		return other instanceof ProductTypeXClassificationSecurityToken;
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

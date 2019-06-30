package com.armineasy.activitymaster.activitymaster.db.entities.product;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.product.builders.ProductXResourceItemSecurityTokenQueryBuilder;
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
@Table(name = "ProductXResourceItemSecurityToken")
@XmlRootElement
@Accessors(chain = true)
@Access(FIELD)
public class ProductXResourceItemSecurityToken
		extends WarehouseSecurityTable<ProductXResourceItemSecurityToken, ProductXResourceItemSecurityTokenQueryBuilder, Long>
		implements Serializable
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,
			name = "ProductXResourceItemSecurityTokenID")
	private Long id;

	@JoinColumn(name = "ProductXResourceItemID",
			referencedColumnName = "ProductXResourceItemID",
			nullable = false)
	@ManyToOne(optional = false,
			fetch = FetchType.LAZY)

	private ProductXResourceItem base;

	public ProductXResourceItemSecurityToken()
	{

	}

	public ProductXResourceItemSecurityToken(Long productXResourceItemSecurityTokenID)
	{
		this.id = productXResourceItemSecurityTokenID;
	}

	public String toString()
	{
		return "ProductXResourceItemSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}

	public Long getId()
	{
		return this.id;
	}

	public ProductXResourceItem getBase()
	{
		return this.base;
	}

	public ProductXResourceItemSecurityToken setId(Long id)
	{
		this.id = id;
		return this;
	}

	public ProductXResourceItemSecurityToken setBase(ProductXResourceItem base)
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
		if (!(o instanceof ProductXResourceItemSecurityToken))
		{
			return false;
		}
		final ProductXResourceItemSecurityToken other = (ProductXResourceItemSecurityToken) o;
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
		return other instanceof ProductXResourceItemSecurityToken;
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

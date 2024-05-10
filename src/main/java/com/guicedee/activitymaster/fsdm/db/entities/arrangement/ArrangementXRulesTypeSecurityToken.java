package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementXRulesTypeSecurityTokenQueryBuilder;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Marc Magon
 * @version 1.0
 * @since 07 Dec 2016
 */
@Getter
@Entity
@Table(schema = "Arrangement", name = "ArrangementXRulesTypeSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementXRulesTypeSecurityToken
		extends IWarehouseSecurityTable<ArrangementXRulesTypeSecurityToken, ArrangementXRulesTypeSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementXRulesTypeSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ArrangementXRulesTypeID",
	            referencedColumnName = "ArrangementXRulesTypeID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementXRulesType base;
	
	public ArrangementXRulesTypeSecurityToken()
	{
	
	}
	
	public ArrangementXRulesTypeSecurityToken(java.lang.String arrangementXRulesSecurityTokenID)
	{
		this.id = arrangementXRulesSecurityTokenID;
	}
	
	public String toString()
	{
		return "ArrangementXRulesTypeSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ArrangementXRulesTypeSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementXRulesTypeSecurityToken setBase(ArrangementXRulesType base)
	{
		this.base = base;
		return this;
	}
}

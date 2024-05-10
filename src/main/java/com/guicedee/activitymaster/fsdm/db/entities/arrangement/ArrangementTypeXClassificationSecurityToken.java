package com.guicedee.activitymaster.fsdm.db.entities.arrangement;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.ArrangementTypeXClassificationSecurityTokenQueryBuilder;
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
@Entity
@Table(schema = "Arrangement",
       name = "ArrangementTypeXClassificationSecurityToken")
@XmlRootElement

@Access(AccessType.FIELD)
@EqualsAndHashCode(of = "id", callSuper = false)
public class ArrangementTypeXClassificationSecurityToken
		extends IWarehouseSecurityTable<ArrangementTypeXClassificationSecurityToken, ArrangementTypeXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ArrangementTypeXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@Getter
	@JoinColumn(name = "ArrangementTypeXClassificationID",
	            referencedColumnName = "ArrangementTypeXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	
	private ArrangementTypeXClassification base;
	
	public ArrangementTypeXClassificationSecurityToken()
	{
	
	}
	
	public ArrangementTypeXClassificationSecurityToken(java.lang.String arrangementXClassificationSecurityTokenID)
	{
		this.id = arrangementXClassificationSecurityTokenID;
	}
	
	@Override
	public String toString()
	{
		return "ArrangementTypeXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	@Override
	public java.lang.String getId()
	{
		return this.id;
	}
	
	@Override
	public ArrangementTypeXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ArrangementTypeXClassificationSecurityToken setBase(ArrangementTypeXClassification base)
	{
		this.base = base;
		return this;
	}
	
}

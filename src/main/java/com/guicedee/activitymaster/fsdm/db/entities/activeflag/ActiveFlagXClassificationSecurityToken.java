package com.guicedee.activitymaster.fsdm.db.entities.activeflag;

import com.guicedee.activitymaster.fsdm.db.abstraction.IWarehouseSecurityTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagXClassificationSecurityTokenQueryBuilder;
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
@EqualsAndHashCode(of = {"id"},callSuper = false)
@Entity
@Table(name = "ActiveFlagXClassificationSecurityToken",
       schema = "dbo")
@XmlRootElement

@Access(AccessType.FIELD)
public class ActiveFlagXClassificationSecurityToken
		extends IWarehouseSecurityTable<ActiveFlagXClassificationSecurityToken, ActiveFlagXClassificationSecurityTokenQueryBuilder, String>
		implements Serializable
{
	
	@Serial
	private static final long serialVersionUID = 1L;
	@Id
	
	@Column(nullable = false,
	        name = "ActiveFlagXClassificationSecurityTokenID")
	@org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
	private java.lang.String id;
	
	@JoinColumn(name = "ActiveFlagXClassificationID",
	            referencedColumnName = "ActiveFlagXClassificationID",
	            nullable = false)
	@ManyToOne(optional = false,
	           fetch = FetchType.LAZY)
	private ActiveFlagXClassification base;
	
	public ActiveFlagXClassificationSecurityToken()
	{
	
	}
	
	public ActiveFlagXClassificationSecurityToken(java.lang.String activeFlagXClassificationSecurityTokenID)
	{
		this.id = activeFlagXClassificationSecurityTokenID;
	}
	
	public String toString()
	{
		return "ActiveFlagXClassificationSecurityToken(id=" + this.getId() + ", base=" + this.getBase() + ")";
	}
	
	public ActiveFlagXClassificationSecurityToken setId(java.lang.String id)
	{
		this.id = id;
		return this;
	}
	
	public ActiveFlagXClassificationSecurityToken setBase(ActiveFlagXClassification base)
	{
		this.base = base;
		return this;
	}
}

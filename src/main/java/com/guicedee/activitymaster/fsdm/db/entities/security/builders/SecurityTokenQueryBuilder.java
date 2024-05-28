/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.fsdm.db.entities.security.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityTokenQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

/**
 * @author Marc Magon
 * @since 30 Apr 2017
 */
public class SecurityTokenQueryBuilder
		extends QueryBuilderSCD<SecurityTokenQueryBuilder, SecurityToken, String,SecurityTokensSecurityTokenQueryBuilder>
		implements ISecurityTokenQueryBuilder<SecurityTokenQueryBuilder, SecurityToken>
{
	public SecurityTokenQueryBuilder findFolder(String securityTokenClassification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		SecurityTokenXSecurityToken hierarchySystem = new SecurityTokenXSecurityToken();
		SecurityTokenXSecurityTokenQueryBuilder hierarchyBuilder = hierarchySystem.builder();
		hierarchyBuilder.withClassification(securityTokenClassification, system);
		inActiveRange();
		hierarchyBuilder.inDateRange();
		if (getEntity().getSecurityToken() != null)
		{
			hierarchyBuilder.withValue(getEntity().getSecurityToken());
		}
		
		join(SecurityToken_.securityTokenXSecurityTokenChildList, hierarchyBuilder, JoinType.INNER);
		return this;
	}
	
	//@Override
	public Attribute findSecuritiesAttribute()
	{
		return getAttribute("securityTokenAccessList");
	}
	
	/**
	 * Returns an identity for the given security token name and system
	 *
	 * @param system
	 * @param name
	 * @return
	 */
	public SecurityToken getIdentity(Systems system, UUID name)
	{
		return withName(name.toString()).inActiveRange()
		                                .inDateRange()
		                                .get()
		                                .get();
	}
	
	public SecurityTokenQueryBuilder findBySecurityToken(String identityToken, IEnterprise<?, ?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, identityToken);
		return this;
	}
	
	public SecurityTokenQueryBuilder findBySecurityTokenActive(String identityToken, IEnterprise<?, ?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, identityToken);
		inActiveRange();
		inDateRange();
		return this;
	}
	
	public SecurityTokenQueryBuilder findBySecurityToken(String identityToken)
	{
		where(getAttribute("securityToken"), InList, identityToken);
		return this;
	}
	
	public SecurityTokenQueryBuilder findBySecurityTokenActive(String identityToken)
	{
		where(getAttribute("securityToken"), InList, identityToken);
		inDateRange();
		return this;
	}
	
	public SecurityTokenQueryBuilder findBySecurityTokenVisibleRange(String identityToken)
	{
		where(getAttribute("securityToken"), InList, identityToken);
		inDateRange();
		return this;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.security.ISecurityTokenQueryBuilder;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.ClassificationService;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.*;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

/**
 * @author Marc Magon
 * @since 30 Apr 2017
 */
public class SecurityTokenQueryBuilder
		extends QueryBuilderTable<SecurityTokenQueryBuilder, SecurityToken, UUID>
		implements ISecurityTokenQueryBuilder<SecurityTokenQueryBuilder,SecurityToken>
{
	public SecurityTokenQueryBuilder findFolder(String securityTokenClassification, ISystems<?,?> system, UUID... identityToken)
	{
		SecurityTokenXSecurityToken hierarchySystem = new SecurityTokenXSecurityToken();
		SecurityTokenXSecurityTokenQueryBuilder hierarchyBuilder = hierarchySystem.builder();

		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = (Classification) classificationService.find(securityTokenClassification, system, identityToken);

		hierarchyBuilder.withClassification(securityTokenClassification,system);
		hierarchyBuilder.inActiveRange(classification.getEnterpriseID());
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
	 *
	 * @return
	 */
	public SecurityToken getIdentity(Systems system, UUID name)
	{
		return withName(name.toString()).inActiveRange(system.getEnterpriseID())
		                                .inDateRange()
		                                .get()
		                                .get();
	}

	public SecurityTokenQueryBuilder findBySecurityToken(String token, IEnterprise<?,?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, token);
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityTokenActive(String token, IEnterprise<?,?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, token);
		inActiveRange(enterprise);
		inDateRange();
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityTokenVisibleRange(String token, IEnterprise<?,?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, token);
		inVisibleRange(enterprise);
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityToken(String token)
	{
		where(getAttribute("securityToken"), InList, token);
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityTokenActive(String token)
	{
		where(getAttribute("securityToken"), InList, token);
		inDateRange();
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityTokenVisibleRange(String token)
	{
		where(getAttribute("securityToken"), InList, token);
		inDateRange();
		return this;
	}
}

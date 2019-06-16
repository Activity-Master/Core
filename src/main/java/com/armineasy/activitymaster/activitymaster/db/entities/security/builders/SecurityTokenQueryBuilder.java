/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.entities.security.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken_;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokensSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.classifications.securitytokens.ISecurityTokenClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.Attribute;
import java.util.UUID;

import static com.jwebmp.entityassist.enumerations.Operand.*;

/**
 * @author Marc Magon
 * @since 30 Apr 2017
 */
public class SecurityTokenQueryBuilder
		extends QueryBuilderSCDNameDescription<SecurityTokenQueryBuilder, SecurityToken, Long, SecurityTokensSecurityToken>
{
	public SecurityTokenQueryBuilder findFolder(ISecurityTokenClassification<?> securityTokenClassification, IEnterprise<?> enterprise, UUID... identityToken)
	{
		SecurityTokenXSecurityToken hierarchySystem = new SecurityTokenXSecurityToken();
		SecurityTokenXSecurityTokenQueryBuilder hierarchyBuilder = hierarchySystem.builder();

		ClassificationService classificationService = GuiceContext.get(ClassificationService.class);
		Classification classification = (Classification) classificationService.find(securityTokenClassification, enterprise, identityToken);

		hierarchyBuilder.withClassification(classification);
		hierarchyBuilder.inActiveRange(classification.getEnterpriseID());
		hierarchyBuilder.inDateRange();
		if (getEntity().getSecurityToken() != null)
		{
			hierarchyBuilder.withValue(getEntity().getSecurityToken());
		}

		join(SecurityToken_.securityTokenXSecurityTokenChildList, hierarchyBuilder, JoinType.INNER);
		return this;
	}

	@Override
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
		return findByName(name.toString()).inActiveRange(system.getEnterpriseID())
		                                  .inDateRange()
		                                  .get()
		                                  .get();
	}

	public SecurityTokenQueryBuilder findBySecurityToken(String token, IEnterprise<?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, token);
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityTokenActive(String token, IEnterprise<?> enterprise)
	{
		where(getAttribute("securityToken"), Equals, token);
		inActiveRange(enterprise);
		inDateRange();
		return this;
	}

	public SecurityTokenQueryBuilder findBySecurityTokenVisibleRange(String token, IEnterprise<?> enterprise)
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

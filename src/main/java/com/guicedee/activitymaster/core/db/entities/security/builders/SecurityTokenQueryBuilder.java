/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guicedee.activitymaster.core.db.entities.security.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXSecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken_;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokensSecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.classifications.securitytokens.ISecurityTokenClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.Attribute;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

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

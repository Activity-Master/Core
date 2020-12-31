package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class RulesXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Product, RulesXProductQueryBuilder,
		RulesXProduct, java.util.UUID, RulesXProductSecurityToken>
{
	@Override
	public SingularAttribute<RulesXProduct, Rules> getPrimaryAttribute()
	{
		return RulesXProduct_.rulesID;
	}

	@Override
	public SingularAttribute<RulesXProduct, Product> getSecondaryAttribute()
	{
		return RulesXProduct_.productID;
	}
	
	
	@jakarta.validation.constraints.NotNull
	public RulesXProductQueryBuilder withClassification(String classification, String value, ISystems<?> system, UUID...identityToken)
	{
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification1 = classificationService.find(classification, system, identityToken);
		JoinExpression joinExpression = new JoinExpression();
		RulesXProductQueryBuilder builder =
				new RulesXProduct()
						.builder()
						.withClassification((Classification) classification1,value)
						.inActiveRange(system.getEnterpriseID())
						.inDateRange()
						.withEnterprise(system.getEnterpriseID())
						.where(RulesXProduct_.classificationID, Equals, (Classification)classification1);
		
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(RulesXProduct_.value, Equals, value);
		}
		
		join(Rules_.products,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
}

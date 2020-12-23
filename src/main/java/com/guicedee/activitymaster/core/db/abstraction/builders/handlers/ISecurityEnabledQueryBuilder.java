package com.guicedee.activitymaster.core.db.abstraction.builders.handlers;

import com.entityassist.enumerations.Operand;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderDefault;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyView;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyView_;
import com.guicedee.activitymaster.core.implementations.SecurityTokenService;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.exceptions.SecurityAccessException;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

import static jakarta.persistence.criteria.JoinType.*;

public interface ISecurityEnabledQueryBuilder<J extends QueryBuilderDefault<J, E, I>, E extends WarehouseBaseTable<E, J, I>, I extends Serializable
		, S extends WarehouseSecurityTable>
		extends IQueryBuilderDefault<J, E, I>
{
	
	@NotNull
	default J canRead(IEnterprise<?> enterprise, UUID... identityToken) throws SecurityAccessException
	{
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}
	
	@NotNull
	default J canRead(IEnterprise<?> enterprise, boolean overrideActiveFlag, UUID... identityToken) throws SecurityAccessException
	{
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}
	
	@NotNull
	default J getSecurityBuilderConfig(IEnterprise<?> enterprise, String fieldCheck, UUID[] identityToken)
	{
		return getSecurityBuilderConfig(enterprise, fieldCheck, false, identityToken);
	}
	
	@SuppressWarnings("unchecked")
	@NotNull
	default J getSecurityBuilderConfig(IEnterprise<?> enterprise, String fieldToCheck, boolean overrideActiveFlag, UUID[] identityToken)
	{
		if (!GuiceContext.get(ActivityMasterConfiguration.class)
		                 .isSecurityEnabled() || true)
		{
			return (J) this;
		}
		
		if (identityToken == null || identityToken.length == 0)
		{
			SecurityToken systemToken = (SecurityToken) GuiceContext.get(ActivityMasterConfiguration.class)
			                                                        .getToken();
			ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
			if (systemToken == null || systemToken.isFake())
			{
				throw new SecurityAccessException("Must provide a UUID to read from once security is turned on, and Default Token is not set");
			}
			identityToken = new UUID[]{UUID.fromString(systemToken.getSecurityToken())};
		}
		
		SecurityToken myToken = (SecurityToken) GuiceContext.get(SecurityTokenService.class)
		                                                    .getSecurityToken(identityToken[0], overrideActiveFlag, enterprise);
		
		if (myToken == null)
		{
			throw new SecurityAccessException("The UUID is not a token of the Security Identity required type.");
		}
		
		Class<S> securityBuilderClass = findSecurityClass();
		
		QueryBuilderSecurities sec = (QueryBuilderSecurities) GuiceContext.get(securityBuilderClass)
		                                                                  .builder();
		JoinExpression expression = new JoinExpression();
		join(findSecuritiesAttribute(), sec, INNER, expression);
		
		Join genRoot = expression.getGeneratedRoot();
		sec.inDateRange();
		sec.inActiveRange(enterprise, identityToken);
		getFilters().add(getCriteriaBuilder().equal(genRoot.get(fieldToCheck), true));
		
		SingularAttribute p = (SingularAttribute) getSecurityTokenAttribute();
		Path securityTableJoinSecurityID = genRoot.get(p);
		
		Root<SecurityHierarchyView> hierarchyCrossJoinRoot = getCriteriaQuery().from(SecurityHierarchyView.class);
		Join join = hierarchyCrossJoinRoot.join("parents", INNER);
		join.on(getCriteriaBuilder().equal(securityTableJoinSecurityID, join.get("value")));
		
		Predicate pred = getCriteriaBuilder().equal(hierarchyCrossJoinRoot.get(SecurityHierarchyView_.id)
				, myToken.getId());
		getFilters().add(pred);
		
		return (J) this;
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default J whereNoSecurityIsApplied()
	{
		Class<S> securityBuilderClass = findSecurityClass();
		QueryBuilderSecurities<?, ?, ?> builder = (QueryBuilderSecurities) GuiceContext.get(securityBuilderClass)
		                                                                               .builder();
		
		JoinExpression<?, ?, ?> expression = new JoinExpression<>();
		join(findSecuritiesAttribute(), builder, JoinType.LEFT, expression);
		builder.where(builder.getAttribute("id"), Operand.Null, (Object) null);
		return (J) this;
	}
	
	
	@NotNull
	default Class<S> findSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}
	
	default Attribute<E, ?> findSecuritiesAttribute()
	{
		return this.getAttribute("securities");
	}
	
	default Attribute<E, ?> getSecurityTokenAttribute()
	{
		return getAttribute("securityTokenID");
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	default J canCreate(IEnterprise<?> enterprise, UUID... identityToken)
	{
		return (J) this;
	}
	
	@NotNull
	default J canUpdate(IEnterprise<?> enterprise, UUID... identityToken)
	{
		return getSecurityBuilderConfig(enterprise, "updateAllowed", identityToken);
	}
	
	@NotNull
	default J canDelete(IEnterprise<?> enterprise, UUID... identityToken)
	{
		return getSecurityBuilderConfig(enterprise, "deleteAllowed", identityToken);
	}
}

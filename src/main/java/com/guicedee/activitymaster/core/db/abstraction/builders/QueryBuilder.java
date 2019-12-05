package com.guicedee.activitymaster.core.db.abstraction.builders;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSecurityTable;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyView;
import com.guicedee.activitymaster.core.db.hierarchies.SecurityHierarchyView_;
import com.guicedee.activitymaster.core.implementations.SecurityTokenService;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.exceptions.SecurityAccessException;
import com.entityassist.enumerations.Operand;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.UUID;

import static javax.persistence.criteria.JoinType.*;

public abstract class QueryBuilder<J extends QueryBuilder<J, E, I, S>,
		                                  E extends WarehouseCoreTable<E, J, I, S>,
		                                  I extends Serializable,
		                                  S extends WarehouseSecurityTable>
		extends QueryBuilderDefault<J, E, I>
{

	@NotNull
	@SuppressWarnings("unchecked")
	public J canRead(IEnterprise<?> enterprise, UUID... identityToken) throws SecurityAccessException
	{
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public J canRead(IEnterprise<?> enterprise, boolean overrideActiveFlag, UUID... identityToken) throws SecurityAccessException
	{
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	private J getSecurityBuilderConfig(IEnterprise<?> enterprise, String readAllowed, UUID[] identityToken)
	{
		return getSecurityBuilderConfig(enterprise, readAllowed, false, identityToken);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	private J getSecurityBuilderConfig(IEnterprise<?> enterprise, String readAllowed, boolean overrideActiveFlag, UUID[] identityToken)
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
		sec.inActiveRange(enterprise,identityToken);
		getFilters().add(getCriteriaBuilder().equal(genRoot.get(readAllowed), true));

		SingularAttribute p = (SingularAttribute) sec.getSecurityTokenAttribute();
		Path securityTableJoinSecurityID = genRoot.get(p);

		Root<SecurityHierarchyView> hierarchyCrossJoinRoot = getCriteriaQuery().from(SecurityHierarchyView.class);
		Join join = hierarchyCrossJoinRoot.join("parents",INNER);
		join.on(getCriteriaBuilder().equal(securityTableJoinSecurityID, join.get("value")));

		Predicate pred = getCriteriaBuilder().equal(hierarchyCrossJoinRoot.get(SecurityHierarchyView_.id)
				, myToken.getId());
		getFilters().add(pred);

		return (J) this;
	}

	// public

	@NotNull
	@SuppressWarnings("unchecked")
	public J whereNoSecurityIsApplied(IEnterprise<?> enterprise)
	{

		Class<S> securityBuilderClass = findSecurityClass();
		QueryBuilderSecurities<?, ?, ?> builder = (QueryBuilderSecurities) GuiceContext.get(securityBuilderClass)
		                                                                               .builder();

		JoinExpression expression = new JoinExpression();
		join(findSecuritiesAttribute(), builder, JoinType.LEFT, expression);
		builder.where(builder.getAttribute("id"), Operand.Null, (Object) null);
		return (J) this;
	}


	@NotNull
	@SuppressWarnings("unchecked")
	public Class<S> findSecurityClass()
	{
		return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
	}

	public Attribute findSecuritiesAttribute()
	{
		return getAttribute("securities");
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public J canCreate(IEnterprise<?> enterprise, UUID... identityToken)
	{
		return (J) this;
	}

	@NotNull
	public J canUpdate(IEnterprise<?> enterprise, UUID... identityToken)
	{
		return getSecurityBuilderConfig(enterprise, "updateAllowed", identityToken);
	}

	@NotNull
	public J canDelete(IEnterprise<?> enterprise, UUID... identityToken)
	{
		return getSecurityBuilderConfig(enterprise, "deleteAllowed", identityToken);
	}
}

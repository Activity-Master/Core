package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.*;
import com.armineasy.activitymaster.activitymaster.db.abstraction.*;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.*;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.*;
import com.armineasy.activitymaster.activitymaster.implementations.*;
import com.armineasy.activitymaster.activitymaster.services.exceptions.*;
import com.jwebmp.entityassist.enumerations.*;
import com.jwebmp.entityassist.querybuilder.builders.*;
import com.jwebmp.guicedinjection.*;
import lombok.Singular;
import org.hibernate.query.criteria.internal.path.PluralAttributePath;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import javax.validation.constraints.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import static javax.persistence.criteria.JoinType.*;

public abstract class QueryBuilder<J extends QueryBuilder<J, E, I, S>,
		                                  E extends WarehouseCoreTable<E, J, I, S>,
		                                  I extends Serializable,
		                                  S extends WarehouseSecurityTable>
		extends QueryBuilderDefault<J, E, I>
{

	@NotNull
	@SuppressWarnings("unchecked")
	public J canRead(Enterprise enterprise, UUID... identityToken) throws SecurityAccessException
	{
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public J canRead(Enterprise enterprise, boolean overrideActiveFlag, UUID... identityToken) throws SecurityAccessException
	{
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	private J getSecurityBuilderConfig(Enterprise enterprise, String readAllowed, UUID[] identityToken)
	{
		return getSecurityBuilderConfig(enterprise, readAllowed, false, identityToken);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	private J getSecurityBuilderConfig(Enterprise enterprise, String readAllowed, boolean overrideActiveFlag, UUID[] identityToken)
	{
		if (!GuiceContext.get(ActivityMasterConfiguration.class)
		                 .isSecurityEnabled())
		{
			return (J) this;
		}

		if (identityToken == null || identityToken.length == 0)
		{
			SecurityToken systemToken = GuiceContext.get(ActivityMasterConfiguration.class)
			                                        .getToken()
			                                        .get();
			ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
			if (systemToken == null || systemToken.isFake())
			{
				throw new SecurityAccessException("Must provide a UUID to read from once security is turned on, and Default Token is not set");
			}
			identityToken = new UUID[]{UUID.fromString(systemToken.getSecurityToken())};
		}

		SecurityToken myToken = GuiceContext.get(SecurityTokenService.class)
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
		SingularAttributePath securityTableJoinSecurityID = (SingularAttributePath) genRoot.get(p);

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
	public J whereNoSecurityIsApplied(Enterprise enterprise)
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
	public J canCreate(Enterprise enterprise, UUID... identityToken)
	{
		return (J) this;
	}

	@NotNull
	public J canUpdate(Enterprise enterprise, UUID... identityToken)
	{
		return getSecurityBuilderConfig(enterprise, "updateAllowed", identityToken);
	}

	@NotNull
	public J canDelete(Enterprise enterprise, UUID... identityToken)
	{
		return getSecurityBuilderConfig(enterprise, "deleteAllowed", identityToken);
	}
}

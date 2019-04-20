package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyXInvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.builders.InvolvedPartySecurityTokenQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.SecurityHierarchyView_;
import com.armineasy.activitymaster.activitymaster.implementations.SecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.exceptions.SecurityAccessException;
import com.jwebmp.entityassist.enumerations.Operand;
import com.jwebmp.entityassist.querybuilder.builders.JoinExpression;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.jwebmp.entityassist.enumerations.Operand.*;
import static javax.persistence.criteria.JoinType.*;

public abstract class QueryBuilder<J extends QueryBuilder<J, E, I, S>,
        E extends WarehouseCoreTable<E, J, I, S>,
        I extends Serializable,
        S extends WarehouseSecurityTable>
        extends QueryBuilderDefault<J, E, I> {

	@NotNull
    @SuppressWarnings("unchecked")
    public J canRead(Enterprise enterprise, UUID... identityToken) throws SecurityAccessException {
        return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
    }

	@NotNull
	@SuppressWarnings("unchecked")
	public J canRead(Enterprise enterprise, boolean overrideActiveFlag, UUID... identityToken) throws SecurityAccessException {
		return getSecurityBuilderConfig(enterprise, "readAllowed", identityToken);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	private J getSecurityBuilderConfig(Enterprise enterprise, String readAllowed, UUID[] identityToken) {
		return getSecurityBuilderConfig(enterprise, readAllowed, false, identityToken);
	}

    @SuppressWarnings("unchecked")
    @NotNull
    private J getSecurityBuilderConfig(Enterprise enterprise, String readAllowed, boolean overrideActiveFlag, UUID[] identityToken) {
        if (!GuiceContext.get(ActivityMasterConfiguration.class)
                .isSecurityEnabled()) {
            return (J) this;
        }

        if (identityToken == null || identityToken.length == 0) {
            SecurityToken systemToken = GuiceContext.get(ActivityMasterConfiguration.class)
                    .getToken().get();
	        ActivityMasterConfiguration config = GuiceContext.get(ActivityMasterConfiguration.class);
            if(systemToken == null || systemToken.isFake())
            {
                throw new SecurityAccessException("Must provide a UUID to read from once security is turned on, and Default Token is not set");
            }
            identityToken = new UUID[]{UUID.fromString(systemToken.getSecurityToken())};
        }

        SecurityToken myToken = GuiceContext.get(SecurityTokenService.class)
                .getSecurityToken(identityToken[0],overrideActiveFlag, enterprise);

        if (myToken == null) {
            throw new SecurityAccessException("The UUID is not a token of the Security Identity required type.");
        }

	    Class<S> securityBuilderClass = findSecurityClass();
	  /*  Root<S> securityRoot = getCriteriaQuery().from(securityBuilderClass);
	    Root<SecurityHierarchyView> shvRoot = getCriteriaQuery().from(SecurityHierarchyView.class);




	    getFilters().add(getCriteriaBuilder().equal(securityRoot.get("enterpriseID"), enterprise));
	    getFilters().add(getCriteriaBuilder().greaterThanOrEqualTo(securityRoot.get("effectiveToDate"), LocalDateTime.now()));
	    getFilters().add(getCriteriaBuilder().lessThanOrEqualTo(securityRoot.get("effectiveFromDate"), LocalDateTime.now()));
	    getFilters().add(getCriteriaBuilder().equal(securityRoot.get(readAllowed), true));

	    //inDateRange();

	    if(!overrideActiveFlag)
	    inActiveRange(enterprise);

	    Expression securityJoinRoot = securityRoot.get("base");
	    Expression securities = getRoot().get("id");

	    getFilters().add(getCriteriaBuilder().equal(securities,securityJoinRoot));

	    Predicate pred = getCriteriaBuilder().like(shvRoot.get(SecurityHierarchyView_.path)
			    , "%" + myToken.getId());

	    */

	    QueryBuilderSecurities sec = (QueryBuilderSecurities) GuiceContext.get(securityBuilderClass).builder();
	    JoinExpression expression = new JoinExpression();
	    join(findSecuritiesAttribute(), sec, LEFT, expression);
	    Join genRoot = expression.getGeneratedRoot();
	    //genRoot.on(getCriteriaBuilder().equal(genRoot.get(readAllowed),true));
	    sec.inDateRange();
	    sec.inActiveRange(enterprise);
	    getFilters().add(getCriteriaBuilder().equal(genRoot.get(readAllowed), true));


	    Root<SecurityHierarchyView> shvRoot = getCriteriaQuery().from(SecurityHierarchyView.class);
	    Predicate pred = getCriteriaBuilder().like(shvRoot.get(SecurityHierarchyView_.path)
			    , "%" + myToken.getId());
	    getFilters().add(pred);
/*
	    getFilters().add(pred);

	    Expression column1Expression = shvRoot.get("path");
	    Expression column2Expression = securityRoot.get("securityTokenID");*/
/*
	    Expression concat =  getCriteriaBuilder().concat("%", getCriteriaBuilder().trim(column2Expression));
	    concat = getCriteriaBuilder().concat(concat,"%");

	    //Predicate likePredicate = getCriteriaBuilder().like(column1Expression, getCriteriaBuilder().concat("%", getCriteriaBuilder().concat(column2Expression, "%")));
	    Predicate likePredicate = getCriteriaBuilder().like(column1Expression, concat);
	    getFilters().add(likePredicate);*/

        return (J) this;
    }

	@NotNull
	@SuppressWarnings("unchecked")
    public J whereNoSecurityIsApplied(Enterprise enterprise)
    {

	    Class<S> securityBuilderClass = findSecurityClass();
	   // Root<S> securityRoot = getCriteriaQuery().from(securityBuilderClass);
	 //   getFilters().add(getCriteriaBuilder().equal(securityRoot.get("enterpriseID"), enterprise));
	 //   getFilters().add(getCriteriaBuilder().greaterThanOrEqualTo(securityRoot.get("effectiveToDate"), LocalDateTime.now()));
	  //  getFilters().add(getCriteriaBuilder().lessThanOrEqualTo(securityRoot.get("effectiveFromDate"), LocalDateTime.now()));
	  //  inDateRange();

	  //  Expression securityJoinRoot = securityRoot.get("base");
	  //  Expression securities = getRoot().get("id");

	    //getFilters().add(getCriteriaBuilder().equal(securities,securityJoinRoot));

	   QueryBuilderSecurities<?,?,?> builder = (QueryBuilderSecurities) GuiceContext.get(securityBuilderClass).builder();

	    JoinExpression expression = new JoinExpression();
	    join(findSecuritiesAttribute(),builder,JoinType.LEFT,expression);
	    builder.where(builder.getAttribute("id"), Operand.Null, (Object)null);
    	return (J)this;
    }


    @NotNull
    @SuppressWarnings("unchecked")
    public Class<S> findSecurityClass() {
        return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    }

    public Attribute findSecuritiesAttribute() {
        return getAttribute("securities");
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public J canCreate(Enterprise enterprise, UUID... identityToken) {
        return (J) this;
    }

    @NotNull
    public J canUpdate(Enterprise enterprise, UUID... identityToken) {
        return getSecurityBuilderConfig(enterprise, "updateAllowed", identityToken);
    }

    @NotNull
    public J canDelete(Enterprise enterprise, UUID... identityToken) {
        return getSecurityBuilderConfig(enterprise, "deleteAllowed", identityToken);
    }
}

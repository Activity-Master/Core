package com.armineasy.activitymaster.activitymaster.db.hierarchies.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.InvolvedPartyHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.InvolvedPartyHierarchyView_;
import com.jwebmp.entityassist.querybuilder.QueryBuilder;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class InvolvedPartyHierarchyViewQueryBuilder
		extends QueryBuilderHierarchyView<InvolvedPartyHierarchyViewQueryBuilder, InvolvedPartyHierarchyView, Long>
{

}

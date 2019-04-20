package com.armineasy.activitymaster.activitymaster.db.hierarchies.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.ClassificationHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.ClassificationHierarchyView_;
import com.jwebmp.entityassist.querybuilder.QueryBuilder;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;

import static com.jwebmp.entityassist.enumerations.Operand.Like;

public class ClassificationHierarchyViewQueryBuilder
		extends QueryBuilderHierarchyView<ClassificationHierarchyViewQueryBuilder, ClassificationHierarchyView, Long>
{

}

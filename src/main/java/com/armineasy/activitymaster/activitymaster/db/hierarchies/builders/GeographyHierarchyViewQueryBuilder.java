package com.armineasy.activitymaster.activitymaster.db.hierarchies.builders;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.GeographyHierarchyView;
import com.armineasy.activitymaster.activitymaster.db.hierarchies.GeographyHierarchyView_;
import com.jwebmp.entityassist.querybuilder.QueryBuilder;
import com.jwebmp.guicedinjection.GuiceContext;

import javax.persistence.EntityManager;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class GeographyHierarchyViewQueryBuilder
		extends QueryBuilderHierarchyView<GeographyHierarchyViewQueryBuilder, GeographyHierarchyView, Long>
{

}

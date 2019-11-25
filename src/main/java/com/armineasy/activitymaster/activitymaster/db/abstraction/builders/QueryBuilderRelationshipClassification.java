/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.armineasy.activitymaster.activitymaster.db.abstraction.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseSecurityTable;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.dto.IClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.guicedee.guicedinjection.GuiceContext;

import java.io.Serializable;

import static com.entityassist.enumerations.Operand.*;

/**
 * @param <P>
 * @param <S>
 * @param <J>
 *
 * @author Marc Magon
 * @since 01 May 2017
 */
public abstract class QueryBuilderRelationshipClassification<P extends WarehouseCoreTable, S extends WarehouseCoreTable,
		                                                            J extends QueryBuilderRelationshipClassification<P, S, J, E, I, ST>,
		                                                            E extends WarehouseClassificationRelationshipTable<P, S, E, J, I, ST,?,?>,
		                                                            I extends Serializable, ST extends WarehouseSecurityTable>
		extends QueryBuilderRelationship<P, S, J, E, I, ST>
{

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J findHierarchyLink(P parent, S child, IEnterprise<?> enterprise)
	{
		findLink(parent, child);
		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.getHierarchyType(enterprise);

		withClassification(hierarchyType);
		return (J) this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public J withClassification(IClassification<?> classification)
	{
		if (classification != null)
		{
			where(getAttribute("classificationID"), Equals, classification);
		}
		return (J) this;
	}
}

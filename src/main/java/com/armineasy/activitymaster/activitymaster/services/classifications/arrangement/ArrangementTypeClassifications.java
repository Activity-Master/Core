package com.armineasy.activitymaster.activitymaster.services.classifications.arrangement;

import com.armineasy.activitymaster.activitymaster.services.IDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseDataConcepts.*;

public enum ArrangementTypeClassifications
		implements IArrangementClassification<ArrangementTypeClassifications>
{
	ArrangementProductTypes("The arrangement is of a product type",Arrangement),
	ProductPurchase("This arrangement is a product purchase", Arrangement),
	ProductQuote("This arrangement is a product quote", Arrangement),
	ProductLead("This arrangement is a product lead", Arrangement),
	;
	private String classificationValue;
	private IDataConceptValue<?> dataConceptValue;

	ArrangementTypeClassifications(String classificationValue, IDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	ArrangementTypeClassifications(String classificationValue)
	{
		this.classificationValue = classificationValue;
	}

	@Override
	public String classificationDescription()
	{
		return classificationValue;
	}
	@Override
	public IDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}

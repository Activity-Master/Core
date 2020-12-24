package com.guicedee.activitymaster.core.services.classifications.arrangement;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum ArrangementTypeClassifications
		implements IArrangementClassification<ArrangementTypeClassifications>
{
	ArrangementProductTypes("The arrangement is of a product type",Arrangement),
	ProductPurchase("This arrangement is a product purchase", Arrangement),
	ProductQuote("This arrangement is a product quote", Arrangement),
	ProductBid("This arrangement is a product bid", Arrangement),
	ProductInterest("This arrangement link is interest from the arrangement in the product", Arrangement),
	ProductLead("This arrangement is a product lead", Arrangement),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	ArrangementTypeClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
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
	public IClassificationDataConceptValue<?> concept()
	{
		return dataConceptValue;
	}
}

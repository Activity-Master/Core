package com.armineasy.activitymaster.activitymaster.services.classifications.product;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum ProductClassifications
		implements IProductClassification<ProductClassifications>
{
	Products("Product Details", ProductXClassification),
	ProductGroup("The group of a product", ProductXClassification),
	ProductTypeName("The type of a product",ProductXClassification),
	ProductPremiumType("If this product is a premium product or not",ProductXClassification),
	ProductBaseCost("The base cost of this product excluding VAT",ProductXClassification),
	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	ProductClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	ProductClassifications(String classificationValue)
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

package com.armineasy.activitymaster.activitymaster.services.classifications.arrangement;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum ArrangementProductClassifications
		implements IArrangementClassification<ArrangementProductClassifications>
{
	ArrangementPurchase("An Arrangement that is a product purchase",ArrangementXProduct),

	PurchaseName("The name of a purchase",ArrangementXProduct),
	PurchaseInvoiceName("The name of a purchase to be displayed on the invoice",ArrangementXProduct),
	PurchaseCost("The cost of a purchase",ArrangementXProduct),
	PurchaseVat("The VAT applied on a purchase",ArrangementXProduct),
	PurchaseTotalCost("The total cost for a purchase",ArrangementXProduct),
	PurchaseInvoiceDate("The date the purchase was invoiced",ArrangementXProduct),
	PurchasePaidDate("The date the purchase was paid",ArrangementXProduct),
	PurchasePromotionCode("The promotion code used on a purchase",ArrangementXProduct),
	PurchaseStatus("The current stage of a purchase",ArrangementXProduct),


	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	ArrangementProductClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	ArrangementProductClassifications(String classificationValue)
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

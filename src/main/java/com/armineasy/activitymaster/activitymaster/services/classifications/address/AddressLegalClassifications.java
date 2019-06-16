package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressLegalClassifications
		implements IAddressClassification<AddressLegalClassifications>
{

	LegalAddress("Legal address is used for official purposes such as for serving a notice or for tax reporting. A legal address is used to determine one’s state of legal residence and the state laws to calculate tax. A legal address may include a property’s lot number, block number or district number.", Address),
	LegalDistrictNumber("The district number of an address", Address),
	LegalLotNumber("The Lot number of an address", Address),
	LegalBlockNumber("The block number of an address", Address),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressLegalClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressLegalClassifications(String classificationValue)
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

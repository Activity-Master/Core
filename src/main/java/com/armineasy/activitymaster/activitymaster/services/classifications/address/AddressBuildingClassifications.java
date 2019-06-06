package com.armineasy.activitymaster.activitymaster.services.classifications.address;

import com.armineasy.activitymaster.activitymaster.services.IClassificationDataConceptValue;

import static com.armineasy.activitymaster.activitymaster.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum AddressBuildingClassifications
		implements IAddressClassification<AddressBuildingClassifications>
{
	BuildingAddress("The address of a building", Address),
	BuildingDesk("The Desk Identifier", Address),
	BuildingIsle("The isle a desk may be located in", Address),
	BuildingFloor("The floor identifier of a building", Address),
	BuildingWindow("The building window identifier", Address),
	BuildingIdentifer("A building identifier", Address),
	BuildingNumber("A building identifier", Address),
	BuildingStreet("A building street identifier", Address),
	BuildingStreetType("A building street type identifier", Address),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	AddressBuildingClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	AddressBuildingClassifications(String classificationValue)
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

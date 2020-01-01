package com.guicedee.activitymaster.core.services.classifications.involvedparty;

import com.guicedee.activitymaster.core.services.enumtypes.IClassificationDataConceptValue;

import static com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts.*;

public enum InvolvedPartyClassifications
		implements IInvolvedPartyClassification<InvolvedPartyClassifications>
{
	SecurityPassword("Defines a password if any is required for a security token", InvolvedPartyXClassification),
	SecurityPasswordSalt("Defines the salt used for the password", InvolvedPartyXClassification),

	Languages("Defines Languages as their ISO counterpart", GlobalClassificationsDataConceptName),
	ISO639_1("ISO 639 is a set of international standards that lists short codes for language names.", InvolvedPartyXClassification),
	ISO639_2("ISO 639 is a set of international standards that lists short codes for language names.", InvolvedPartyXClassification),
	ISO6392EnglishName("The english name of a language.", InvolvedPartyXClassification),
	ISO6392FrenchName("ISO 639 is a set of international standards that lists short codes for language names.", InvolvedPartyXClassification),
	ISO6392GermanName("ISO 639 is a set of international standards that lists short codes for language names.", InvolvedPartyXClassification),

	;
	private String classificationValue;
	private IClassificationDataConceptValue<?> dataConceptValue;

	InvolvedPartyClassifications(String classificationValue, IClassificationDataConceptValue<?> dataConceptValue)
	{
		this.classificationValue = classificationValue;
		this.dataConceptValue = dataConceptValue;
	}

	InvolvedPartyClassifications(String classificationValue)
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

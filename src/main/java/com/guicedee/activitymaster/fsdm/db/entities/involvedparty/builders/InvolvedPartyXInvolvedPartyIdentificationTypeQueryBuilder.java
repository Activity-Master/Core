package com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders;

import com.entityassist.enumerations.Operand;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.api.EncryptedValuePredicate;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<InvolvedParty,
		InvolvedPartyIdentificationType,
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder,
		InvolvedPartyXInvolvedPartyIdentificationType,
		UUID,
		InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokenQueryBuilder>
{
	private UUID encryptionEnterprise;

	@Override
	public InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withEnterprise(IEnterprise<?, ?> enterprise)
	{
		if (enterprise != null && enterprise.getId() != null)
		{
			encryptionEnterprise = enterprise.getId();
			where(getAttribute("enterpriseID"), Equals, enterprise);
		}
		return this;
	}

	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyIdentificationType, InvolvedParty> getPrimaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID;
	}
	
	@Override
	public SingularAttribute<InvolvedPartyXInvolvedPartyIdentificationType, InvolvedPartyIdentificationType> getSecondaryAttribute()
	{
		return InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID;
	}
	
	@Override
	public  InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withValue(Operand operand, String value)
	{
		if (ColumnEncryption.enterpriseReads() && encryptionEnterprise == null)
			throw new IllegalStateException("Call withEnterprise before withValue for enterprise encryption");
		if (ColumnEncryption.searchableEncryption())
		{
			getFilters().add(EncryptedValuePredicate.create(getCriteriaBuilder(), getRoot().get("value"),
					operand, value, ColumnEncryption.IDENTIFICATION, encryptionEnterprise));
			return this;
		}
		if (Strings.isNullOrEmpty(value))
		{
			return this;
		}
		where(InvolvedPartyXInvolvedPartyIdentificationType_.value, operand, ColumnEncryption.legacySearchValue(value));
		return this;
	}
	
	@Override
	public InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
			join(getAttribute(InvolvedPartyXInvolvedPartyIdentificationType_.INVOLVED_PARTY_IDENTIFICATION_TYPE_ID), JoinType.INNER, joinExpression);
			var nameFilter = joinExpression.getFilter(InvolvedPartyIdentificationType_.NAME, Equals, typeValue);
			getFilters().add(nameFilter);
			inActiveRange();
			inDateRange();
		}
		return this;
	}
}

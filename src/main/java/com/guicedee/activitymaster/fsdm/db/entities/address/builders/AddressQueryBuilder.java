package com.guicedee.activitymaster.fsdm.db.entities.address.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.api.ColumnEncryption;
import com.guicedee.activitymaster.fsdm.api.EncryptedValuePredicate;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.address.IAddressQueryBuilder;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address;
import com.guicedee.activitymaster.fsdm.db.entities.address.Address_;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AddressQueryBuilder
		extends QueryBuilderSCD<AddressQueryBuilder, Address, UUID,AddressSecurityTokenQueryBuilder>
		implements IAddressQueryBuilder<AddressQueryBuilder, Address>
{
	private UUID encryptionEnterprise;

	@Override
	public AddressQueryBuilder withEnterprise(IEnterprise<?, ?> enterprise)
	{
		if (enterprise != null && enterprise.getId() != null)
		{
			encryptionEnterprise = enterprise.getId();
			where(getAttribute("enterpriseID"), Operand.Equals, enterprise);
		}
		return this;
	}

	@Override
	public @NotNull AddressQueryBuilder withValue(Operand operand, String value)
	{
		if (ColumnEncryption.enterpriseReads() && encryptionEnterprise == null)
			throw new IllegalStateException("Call withEnterprise before withValue for enterprise encryption");
		if (ColumnEncryption.searchableEncryption())
		{
			getFilters().add(EncryptedValuePredicate.create(getCriteriaBuilder(), getRoot().get("value"),
					operand, value, ColumnEncryption.ADDRESS, encryptionEnterprise));
		}
		else
		{
			where(Address_.value, operand, ColumnEncryption.legacySearchValue(value));
		}
		return this;
	}
}

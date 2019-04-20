package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import static com.jwebmp.guicedinjection.GuiceContext.*;

public interface IContainsAddresses<P extends WarehouseCoreTable,
		                                   S extends WarehouseCoreTable,
		                                   J extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?>>
{
	@SuppressWarnings("unchecked")
	default Optional<J> findAddress(@CacheKey Address address, @CacheKey UUID... identityToken)
	{
		J activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) activityMasterIdentity.builder()
		                                                         .findLink((P) this, (S) address, address.getEnterpriseID())
		                                                         .inActiveRange(address.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(address.getEnterpriseID(), identityToken)
		                                                         .get();
		return exists;
	}

	@NotNull
	@SuppressWarnings("unchecked")
	default Class<J> findAddressQueryRelationshipTableType()
	{
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces)
		{
			String clazz = genericInterface.getTypeName();
			if (genericInterface instanceof ParameterizedType && clazz.contains(IContainsAddresses.class.getCanonicalName()))
			{
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				return (Class<J>) genericTypes[2];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	default boolean hasAddress(IAddressClassification<?> addressClassification, String value, Enterprise enterprise, UUID... identityToken)
	{
		J activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		Systems activityMasterSystem = get(ISystemsService.class)
				                               .getActivityMaster(enterprise);
		Classification classification = get(ClassificationService.class).find(addressClassification,
		                                                                      activityMasterSystem.getEnterpriseID(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, classification.getEnterpriseID())
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J addAddress(IAddressClassification<?> addressClassification, Systems originatingSystem, String value, UUID... identifyingToken)
	{
		Systems activityMasterSystem = get(ISystemsService.class)
				                               .getActivityMaster(originatingSystem.getEnterpriseID());

		Classification classification = get(ClassificationService.class).find(addressClassification,
		                                                                      originatingSystem.getEnterpriseID(), identifyingToken);

		Address addy = new Address();
		Optional<Address> addressExists = addy.builder().withClassification(classification)
		                                               .withEnterprise(originatingSystem.getEnterpriseID())
		                                               .withValue(value)
		                                               .inDateRange()
		                                               .get();
		if (addressExists.isEmpty())
		{
			addy.setEnterpriseID(classification.getEnterpriseID());
			addy.setClassification(classification);
			addy.setValue(value);
			addy.setSystemID(activityMasterSystem);
			addy.setOriginalSourceSystemID(activityMasterSystem);
			addy.setActiveFlagID(classification.getActiveFlagID());
			addy.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				addy.createDefaultSecurity(activityMasterSystem);
			}
		}
		else
		{
			addy = addressExists.get();
		}

		J tableForClassification = get(findAddressQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) classification, classification.getEnterpriseID())
		                                                         .inActiveRange(classification.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(classification.getEnterpriseID());
			tableForClassification.setClassificationID(classification);
			tableForClassification.setValue(value);
			tableForClassification.setSystemID(activityMasterSystem);
			tableForClassification.setOriginalSourceSystemID(activityMasterSystem);
			tableForClassification.setActiveFlagID(classification.getActiveFlagID());

			setMyAddressLinkValue(tableForClassification, (S) addy, classification.getEnterpriseID());

			tableForClassification.persist();
			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(activityMasterSystem);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}


	@SuppressWarnings("unchecked")
	default J add(Address addy, Systems originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = get(findAddressQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy, originatingSystem.getEnterpriseID())
		                                                         .inActiveRange(addy.getClassification().getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(),identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(addy.getClassification().getEnterpriseID());
			tableForClassification.setClassificationID(addy.getClassification());
			tableForClassification.setSystemID(originatingSystem);
			tableForClassification.setValue("");
			tableForClassification.setOriginalSourceSystemID(originatingSystem);
			tableForClassification.setActiveFlagID(addy.getClassification().getActiveFlagID());
			setMyAddressLinkValue(tableForClassification, (S) addy, addy.getClassification().getEnterpriseID());
			tableForClassification.persist();

			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}



	void setMyAddressLinkValue(J classificationLink, S geography, Enterprise enterprise);
}

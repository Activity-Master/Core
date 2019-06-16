package com.armineasy.activitymaster.activitymaster.services.capabilities;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseClassificationRelationshipTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.WarehouseCoreTable;
import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.system.IAddressService;
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
		                                                         .findLink((P) this, (S) address, null)
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
	default boolean hasAddress(IAddressClassification<?> addressClassification, String value, IEnterprise<?> enterprise, UUID... identityToken)
	{
		J activityMasterIdentity = get(findAddressQueryRelationshipTableType());
		ISystems activityMasterSystem = get(ISystemsService.class)
				                                .getActivityMaster(enterprise);
		Classification classification = (Classification) get(ClassificationService.class).find(addressClassification,
		                                                                                       activityMasterSystem.getEnterpriseID(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J add(IAddressClassification<?> addressClassification, Systems originatingSystem, String value, UUID... identifyingToken)
	{
		ISystems activityMasterSystem = get(ISystemsService.class)
				                                .getActivityMaster(originatingSystem.getEnterpriseID());

		Classification classification = (Classification) get(ClassificationService.class).find(addressClassification,
		                                                                                       originatingSystem.getEnterpriseID(), identifyingToken);
		
		IAddressService addressService = get(IAddressService.class);
		Address address = addressService.create(addressClassification, originatingSystem, value, identifyingToken);
		J tableForClassification = (J) address.addOrReuse(addressClassification, value, originatingSystem, identifyingToken);
		return tableForClassification;
	}


	@SuppressWarnings("unchecked")
	default J add(Address addy, ISystems<?> originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = get(findAddressQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy,null)
		                                                         .inActiveRange(addy.getClassification()
		                                                                            .getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID(addy.getClassification()
			                                           .getEnterpriseID());
			tableForClassification.setClassificationID(addy.getClassification());
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setValue("");
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setActiveFlagID(addy.getClassification()
			                                           .getActiveFlagID());
			setMyAddressLinkValue(tableForClassification, (S) addy, addy.getClassification()
			                                                            .getEnterpriseID());
			tableForClassification.persist();

			if (get(ActivityMasterConfiguration.class).isSecurityEnabled())
			{
				tableForClassification.createDefaultSecurity(originatingSystem, identifyingToken);
			}
		}
		else
		{
			tableForClassification = exists.get();
		}
		return tableForClassification;
	}



	void setMyAddressLinkValue(J classificationLink, S geography, IEnterprise<?> enterprise);
}

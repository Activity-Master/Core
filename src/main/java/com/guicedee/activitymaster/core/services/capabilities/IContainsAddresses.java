package com.guicedee.activitymaster.core.services.capabilities;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.dto.IAddress;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IAddressService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.cache.annotation.CacheKey;
import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import static com.guicedee.guicedinjection.GuiceContext.*;

public interface IContainsAddresses<P extends WarehouseCoreTable,
		                                   S extends WarehouseCoreTable,
		                                   J extends WarehouseClassificationRelationshipTable<P, S, ?, ? extends QueryBuilderRelationshipClassification, ?, ?,?,?>>
{
	@SuppressWarnings("unchecked")
	default Optional<J> findAddress(@CacheKey IAddress<?> address, @CacheKey UUID... identityToken)
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
		Classification classification = (Classification) GuiceContext.get(ClassificationService.class).find(addressClassification,
		                                                                                                    activityMasterSystem.getEnterpriseID(), identityToken);
		return activityMasterIdentity.builder()
		                             .findLink((P) this, (S) classification, null)
		                             .inActiveRange(classification.getEnterpriseID())
		                             .inDateRange()
		                             .canRead(classification.getEnterpriseID(), identityToken)
		                             .getCount() > 0;
	}

	@SuppressWarnings("unchecked")
	default J add(IAddressClassification<?> addressClassification, ISystems<?> originatingSystem, String value, UUID... identifyingToken)
	{
		ISystems activityMasterSystem = get(ISystemsService.class)
				                                .getActivityMaster(originatingSystem.getEnterpriseID());

		Classification classification = (Classification) get(ClassificationService.class).find(addressClassification,
		                                                                                       originatingSystem.getEnterpriseID(), identifyingToken);

		IAddressService addressService = get(IAddressService.class);
		IAddress<?> address = addressService.create(addressClassification, originatingSystem, value, identifyingToken);
		J tableForClassification = (J) address.addOrReuse(addressClassification, value, originatingSystem, identifyingToken);
		return tableForClassification;
	}


	@SuppressWarnings("unchecked")
	default J add(IAddress<?> addy, ISystems<?> originatingSystem, UUID... identifyingToken)
	{
		J tableForClassification = get(findAddressQueryRelationshipTableType());
		Optional<J> exists = (Optional<J>) tableForClassification.builder()
		                                                         .findLink((P) this, (S) addy,null)
		                                                         .inActiveRange(addy.getEnterpriseID())
		                                                         .inDateRange()
		                                                         .canRead(originatingSystem.getEnterpriseID(), identifyingToken)
		                                                         .get();
		if (exists.isEmpty())
		{
			tableForClassification.setEnterpriseID((Enterprise) addy.getEnterpriseID());
			tableForClassification.setClassificationID(((Address)addy).getClassification());
			tableForClassification.setSystemID((Systems) originatingSystem);
			tableForClassification.setValue("");
			tableForClassification.setOriginalSourceSystemID((Systems) originatingSystem);
			tableForClassification.setActiveFlagID((ActiveFlag) addy.getActiveFlagID());
			setMyAddressLinkValue(tableForClassification, (S) addy, addy.getEnterpriseID());
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

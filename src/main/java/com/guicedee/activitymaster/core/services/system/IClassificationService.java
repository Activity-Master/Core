package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.UUID;

public interface IClassificationService<J extends IClassificationService<J>>
{
	String ClassificationSystemName = "Classifications System";
	
	IClassification<?> create(IClassificationValue<?> name,
	                          ISystems<?> system, UUID... identityToken);
	
	IClassification<?> create(IClassificationValue<?> name,
	                          ISystems<?> system, IClassificationValue<?> parentName, UUID... identityToken);
	
	IClassification<?> create(String name, String description, String concept,
	                          ISystems<?> system, Integer sequenceOrder, String parentName, UUID... identityToken);
	
	IClassification<?> create(IClassificationValue<?> name,
	                          ISystems<?> system, Integer sequenceOrder, IClassificationValue<?> parentName, UUID... identityToken);
	
	IClassification<?> create(IClassificationValue<?> name,
	                          ISystems<?> system, String parentName, UUID... identityToken);
	
	IClassification<?> create(IClassificationValue<?> name,
	                          ISystems<?> system, Integer sequenceNumber, UUID... identityToken);
	
	IClassification<?> create(String name,
	                          ISystems<?> system, UUID... identityToken);
	
	IClassification<?> create(String name, String description,
	                          ISystems<?> system, UUID... identityToken);
	
	IClassification<?> create(String name, String description, String conceptName,
	                          ISystems<?> system, UUID... identityToken);
	
	IClassification<?> create(String classificationName, String classificationDescription, String conceptName, ISystems<?> system, Integer sequenceNumber, IClassificationValue<?> parentClass, UUID... identityToken);
	
	IClassification<?> create(String name, String description, String conceptName,
	                          ISystems<?> system,
	                          Integer sequenceNumber, UUID... identityToken);
	
	/*IClassification<?> create(IClassificationValue<?> concept,
								  ISystems<?> system, IClassificationValue<?> parent,
								  UUID... identityToken);
	
		IClassification<?> create(IClassificationValue<?> concept,
								  ISystems<?> system,
								  UUID... identityToken);
	
		IClassification<?> create(IClassificationValue<?> concept,
								  ISystems<?> system,
								  Short sequenceNumber, UUID... identityToken);
	
		IClassification<?> create(IClassificationValue<?> concept,
								  ISystems<?> system,
								  Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken);
	
		IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
								  ISystems<?> system,
								  Short sequenceNumber, IClassificationValue<?> parent, UUID... identityToken);
	
		IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
								  ISystems<?> system,
								  Short sequenceNumber, String parent, UUID... identityToken);
	*/
	IClassification<?> create(String name, String description, String conceptName,
	                          ISystems<?> system,
	                          Integer sequenceNumber, IClassification<?> parent, UUID... identityToken);

	
/*
	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
	                          IEnterprise<?> system,
	                          Short sequenceNumber, IClassification<?> parent, UUID... identityToken);
*/

/*	IClassification<?> create(String name, String description, IClassificationDataConceptValue<?> conceptName,
							  ISystems<?> system,
							  Short sequenceNumber, UUID... identityToken);
	*/

//	IClassification<?> findOrCreate( String name, ISystems<?> system, UUID... identityToken);
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	IClassification<?> find(@CacheKey String name, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken);
	
	@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	IClassification<?> find(@CacheKey IClassificationValue<?> name, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken);
	
	IClassification<?> find(String name, IClassificationDataConcept<?> concept, ISystems<?> system, UUID... identityToken);
	
	IClassification<?> find(String name, String concept, ISystems<?> system, UUID... identityToken);
	
	IClassification<?> getHierarchyType(ISystems<?> system, UUID... identityToken);
	
	IClassification<?> getNoClassification(ISystems<?> system, UUID... identityToken);
	
	IClassification<?> getIdentityType(ISystems<?> system, UUID... identityToken);
}

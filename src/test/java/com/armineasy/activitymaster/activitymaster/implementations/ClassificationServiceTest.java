package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activity.configs.DefaultTestConfig;
import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.armineasy.activitymaster.activitymaster.services.classifications.classification.Classifications.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DefaultTestConfig.class)
@Log
class ClassificationServiceTest
{

	@Test
	void getHierarchyType()
	{
		GuiceContext.get(ActivityMasterConfiguration.class).setSecurityEnabled(false);
		Enterprise enterprise = new Enterprise().builder()
		                                        .getAll()
		                                        .get(0);

		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification hierarchyType = service.find(HierarchyTypeClassification,
		                                            enterprise);
		assertNotNull(hierarchyType);

	}

	@Test
	void getIdentityType()
	{
	}
}
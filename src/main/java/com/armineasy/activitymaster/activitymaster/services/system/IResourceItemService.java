package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IResourceTypeValue;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.google.inject.Singleton;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;
import lombok.extern.java.Log;

import java.util.UUID;


public interface IResourceItemService
{

	@Transactional(entityManagerAnnotation =  ActivityMasterDB.class)
	ResourceItem create(IResourceTypeValue<?> identityResourceType,
	                    Systems system, UUID... identityToken);
}

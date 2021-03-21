package com.guicedee.activitymaster.core.threads;

import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemData;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.UUID;

public class StoreResourceItemThread
		extends Thread
{
	private IResourceItem<?,?> item;
	private byte[] data;
	private ISystems<?,?> originatingSystem;
	private UUID[] identifyingToken;

	public StoreResourceItemThread()
	{
	}

	public StoreResourceItemThread(IResourceItem<?,?> item, byte[] data, ISystems<?,?> originatingSystem, UUID[] identifyingToken)
	{
		this.item = item;
		this.data = data;
		this.originatingSystem = originatingSystem;
		this.identifyingToken = identifyingToken;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void run()
	{
		ResourceItemData itemData = new ResourceItemData();
		itemData.setResource((ResourceItem) item);
		itemData.setResourceItemData(data);

		itemData.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		itemData.setSystemID((Systems) originatingSystem);
		itemData.setOriginalSourceSystemID((Systems) originatingSystem);

		itemData.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		itemData.persist();
			itemData.createDefaultSecurity(originatingSystem, identifyingToken);
	}

	public IResourceItem<?,?> getItem()
	{
		return item;
	}

	public StoreResourceItemThread setItem(IResourceItem<?,?> item)
	{
		this.item = item;
		return this;
	}

	public byte[] getData()
	{
		return data;
	}

	public StoreResourceItemThread setData(byte[] data)
	{
		this.data = data;
		return this;
	}

	public ISystems<?,?> getOriginatingSystem()
	{
		return originatingSystem;
	}

	public StoreResourceItemThread setOriginatingSystem(ISystems<?,?> originatingSystem)
	{
		this.originatingSystem = originatingSystem;
		return this;
	}

	public UUID[] getIdentifyingToken()
	{
		return identifyingToken;
	}

	public StoreResourceItemThread setIdentifyingToken(UUID[] identifyingToken)
	{
		this.identifyingToken = identifyingToken;
		return this;
	}
}

package com.armineasy.activitymaster.activitymaster.services.threads;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemData;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.dto.IResourceItem;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.threads.TransactionalIdentifiedThread;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.UUID;

public class StoreResourceItemThread
		extends TransactionalIdentifiedThread
{
	private IResourceItem<?> item;
	private byte[] data;
	private ISystems<?> originatingSystem;
	private UUID[] identifyingToken;

	public StoreResourceItemThread()
	{
	}

	public StoreResourceItemThread(IResourceItem<?> item, byte[] data, ISystems<?> originatingSystem, UUID[] identifyingToken)
	{
		this.item = item;
		this.data = data;
		this.originatingSystem = originatingSystem;
		this.identifyingToken = identifyingToken;
	}

	@Override
	public void perform()
	{
		ResourceItemData itemData = new ResourceItemData();
		itemData.setResource((ResourceItem) item);
		itemData.setResourceItemData(data);

		itemData.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		itemData.setSystemID((Systems) originatingSystem);
		itemData.setOriginalSourceSystemID((Systems) originatingSystem);

		itemData.setActiveFlagID(((Systems) originatingSystem).getActiveFlagID());
		itemData.persist();

		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			itemData.createDefaultSecurity(originatingSystem, identifyingToken);
		}
	}

	public IResourceItem<?> getItem()
	{
		return item;
	}

	public StoreResourceItemThread setItem(IResourceItem<?> item)
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

	public ISystems<?> getOriginatingSystem()
	{
		return originatingSystem;
	}

	public StoreResourceItemThread setOriginatingSystem(ISystems<?> originatingSystem)
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

package com.guicedee.activitymaster.fsdm.encryption;

import java.util.UUID;

/** Administrative key metadata, not a warehouse record; access through trusted service only. */
public class EnterpriseDataKey
{
    private String id;
    private UUID enterpriseId;
    private String keyReference;
    private String wrappedKey;
    private boolean active;

    protected EnterpriseDataKey() { }

    public EnterpriseDataKey(String id, UUID enterprise, String reference, String wrapped, boolean active)
    {
        this.id = id;
        enterpriseId = enterprise;
        keyReference = reference;
        wrappedKey = wrapped;
        this.active = active;
    }

    EnterpriseDataKey(UUID enterprise, String reference, String wrapped)
    {
        id = UUID.randomUUID().toString().replace("-", "");
        enterpriseId = enterprise;
        keyReference = reference;
        wrappedKey = wrapped;
        active = true;
    }

    public String getId() { return id; }
    public UUID getEnterpriseId() { return enterpriseId; }
    public String getKeyReference() { return keyReference; }
    public String getWrappedKey() { return wrappedKey; }
    public boolean isActive() { return active; }
}


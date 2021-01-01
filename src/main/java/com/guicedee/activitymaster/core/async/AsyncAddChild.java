package com.guicedee.activitymaster.core.async;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.capabilities.IContainsHierarchy;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public class AsyncAddChild implements Runnable {
    @Getter@Setter
    private IContainsHierarchy  parent;
    @Getter@Setter
    private IContainsHierarchy child;
    @Getter@Setter
    private String value;
    @Getter@Setter
    private ISystems<?> system;
    @Getter@Setter
    private UUID[] identityTokens;

    @Getter@Setter
    private Consumer<Throwable> onException;

    AsyncAddChild(){}

    @Override
    public void run() {
        persist();
    }

    public static AsyncAddChild getInstance(IContainsHierarchy<?,?,?,?,?> parent, IContainsHierarchy<?,?,?,?,?> child, String hierarchyValue, ISystems<?> system, UUID[] identityToken, Consumer<Throwable> throwableConsumer)
    {
        AsyncAddChild ap = GuiceContext.get(AsyncAddChild.class);
        ap.setParent(parent);
        ap.setChild(child);
        ap.setValue(hierarchyValue);
        ap.setSystem(system);
        ap.setIdentityTokens(identityToken);
        ap.setOnException(throwableConsumer);
        return ap;
    }

    @SuppressWarnings("unchecked")
    @Transactional(entityManagerAnnotation = ActivityMasterDB.class)
    void persist()
    {
        try {
            parent.addChild(child,value, system,identityTokens);
        }catch (Throwable T)
        {
            if(onException != null)
            {
                onException.accept(T);
            }
        }
    }
}

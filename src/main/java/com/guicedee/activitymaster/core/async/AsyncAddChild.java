package com.guicedee.activitymaster.core.async;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.capabilities.IContainsHierarchy;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
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
    private IEnterprise<?> enterprise;
    @Getter@Setter
    private UUID[] identityTokens;

    @Getter@Setter
    private Consumer<Throwable> onException;

    AsyncAddChild(){}

    @Override
    public void run() {
        persist();
    }

    public static AsyncAddChild getInstance(IContainsHierarchy<?,?,?,?,?> parent,IContainsHierarchy<?,?,?,?,?> child,String hierarchyValue, IEnterprise<?> enterprise,UUID[] identityToken,  Consumer<Throwable> throwableConsumer)
    {
        AsyncAddChild ap = GuiceContext.get(AsyncAddChild.class);
        ap.setParent(parent);
        ap.setChild(child);
        ap.setValue(hierarchyValue);
        ap.setEnterprise(enterprise);
        ap.setIdentityTokens(identityToken);
        ap.setOnException(throwableConsumer);
        return ap;
    }

    @SuppressWarnings("unchecked")
    @Transactional(entityManagerAnnotation = ActivityMasterDB.class)
    void persist()
    {
        try {
            parent.addChild(child,value,enterprise,identityTokens);
        }catch (Throwable T)
        {
            if(onException != null)
            {
                onException.accept(T);
            }
        }
    }
}

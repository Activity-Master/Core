package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.client.services.config.IGuiceScanModuleInclusions;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ActivityMasterModuleInclusion implements IGuiceScanModuleInclusions<ActivityMasterModuleInclusion> {
    @Override
    public @NotNull Set<String> includeModules() {
        Set<String> set = new HashSet<>();
        set.add("com.guicedee.activitymaster.fsdm");
        return set;
    }
}

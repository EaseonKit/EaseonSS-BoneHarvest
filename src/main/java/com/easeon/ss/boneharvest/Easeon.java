// Easeon.java
package com.easeon.ss.boneharvest;

import com.easeon.ss.core.api.common.base.BaseToggleModule;
import com.easeon.ss.core.api.definitions.enums.EventPhase;
import com.easeon.ss.core.api.events.EaseonBlockUse;
import com.easeon.ss.core.api.events.EaseonBlockUse.BlockUseTask;
import net.fabricmc.api.ModInitializer;

public class Easeon extends BaseToggleModule implements ModInitializer {
    private BlockUseTask task;
    public static Easeon instance;

    public Easeon() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        logger.info("Initialized!");
    }

    public void updateTask() {
        if (config.enabled && task == null) {
            task = EaseonBlockUse.register(EventPhase.BEFORE, EaseonBlockUseHandler::useBlock);
        }
        if (!config.enabled && task != null) {
            EaseonBlockUse.unregister(task);
            task = null;
        }
    }
}
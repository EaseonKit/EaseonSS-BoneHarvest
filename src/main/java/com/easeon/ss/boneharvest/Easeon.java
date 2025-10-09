// Easeon.java
package com.easeon.ss.boneharvest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Easeon implements ModInitializer {
    public static final String MOD_ID = "easeon-ss-boneharvest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ConfigManager CONFIG = new ConfigManager();

    @Override
    public void onInitialize() {
        LOGGER.info("Bone Harvest Mod Initializing...");

        // Config 로드
        CONFIG.load();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            EaseonCommand.register(dispatcher);
            LOGGER.info("Commands registered!");
        });

        UseBlockCallback.EVENT.register(ItemBoneHarvestHandler::useBlockCallback);

        LOGGER.info("Bone Harvest Mod Initialized!");
    }
}
package com.random789.iron_testing_2_0;

import com.mojang.logging.LogUtils;
import com.random789.iron_testing_2_0.command.DisplaySpawnMessagesCommand;
import com.random789.iron_testing_2_0.command.LogSpawnAttemptsCommand;
import com.random789.iron_testing_2_0.command.MonitorCommand;
import com.random789.iron_testing_2_0.command.RenderSpawnAttemptsCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;

public class Iron_testing_2_0 implements ModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MonitorCommand.register(dispatcher);
			DisplaySpawnMessagesCommand.register(dispatcher);
			RenderSpawnAttemptsCommand.register(dispatcher);
			LogSpawnAttemptsCommand.register(dispatcher);
		});
	}
}

package com.random789.iron_testing_2_0;

import com.mojang.logging.LogUtils;
import com.random789.iron_testing_2_0.command.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import static com.random789.iron_testing_2_0.command.EfficiencyTesterCommand.numGolems;

public class Iron_testing_2_0 implements ModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static int spawns = 0;

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MonitorCommand.register(dispatcher);
			DisplaySpawnMessagesCommand.register(dispatcher);
			RenderSpawnAttemptsCommand.register(dispatcher);
			LogSpawnAttemptsCommand.register(dispatcher);
            EfficiencyTesterCommand.register(dispatcher);
		});
		ServerTickEvents.END_SERVER_TICK.register((server) -> {

			if (spawns > 0) {
				if (numGolems > 0) {
					float efficiency = (float) spawns / numGolems;
					for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
						player.sendMessageToClient(Text.literal(String.format("Spawned %d iron golems at %f percent efficiency", spawns, efficiency)), false);
					}
					Iron_testing_2_0.LOGGER.info("Spawned {} iron golems at {} percent efficiency", spawns, efficiency);
					spawns = 0;
				}
			}
		});
	}

}

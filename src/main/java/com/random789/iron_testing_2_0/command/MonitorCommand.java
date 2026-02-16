package com.random789.iron_testing_2_0.command;

import com.mojang.brigadier.CommandDispatcher;
import com.random789.iron_testing_2_0.Settings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MonitorCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("monitor")
				.then(
					CommandManager.literal("on")
						.executes(context -> {
							Settings.shouldMonitorSpawnAttempts = true;
							context.getSource().sendFeedback(() -> Text.literal("Monitoring set to on"), true);
							return 1;
						})
				)
				.then(
					CommandManager.literal("off")
						.executes(context -> {
							Settings.shouldMonitorSpawnAttempts = false;
							context.getSource().sendFeedback(() -> Text.literal("Monitoring set to off"), true);
							return 1;
						})
				)
		);
	}
}

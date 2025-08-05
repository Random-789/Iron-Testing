package com.random789.iron_testing_2_0.command;

import com.mojang.brigadier.CommandDispatcher;
import com.random789.iron_testing_2_0.Settings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class LogSpawnAttemptsCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("logspawnattempts")
				.requires(CommandManager.requirePermissionLevel(2))
				.then(
					CommandManager.literal("on")
						.executes(context -> {
							Settings.shouldLogSpawnAttempts = true;
							context.getSource().sendFeedback(() -> Text.literal("Logging spawn attempts set to on"), true);
							return 1;
						})
				)
				.then(
					CommandManager.literal("off")
						.executes(context -> {
							Settings.shouldLogSpawnAttempts = false;
							context.getSource().sendFeedback(() -> Text.literal("Logging spawn attempts set to off"), true);
							return 1;
						})
				)
		);
	}
}

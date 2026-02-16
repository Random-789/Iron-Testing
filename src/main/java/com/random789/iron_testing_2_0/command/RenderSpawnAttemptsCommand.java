package com.random789.iron_testing_2_0.command;

import com.mojang.brigadier.CommandDispatcher;
import com.random789.iron_testing_2_0.Settings;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class RenderSpawnAttemptsCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("renderspawnattempts")
				.then(
					CommandManager.literal("on")
						.executes(context -> {
							Settings.shouldRenderSpawnAttempts = true;
							context.getSource().sendFeedback(() -> Text.literal("Rendering spawn messages set to on"), true);
							return 1;
						})
				)
				.then(
					CommandManager.literal("off")
						.executes(context -> {
							Settings.shouldRenderSpawnAttempts = false;
							context.getSource().sendFeedback(() -> Text.literal("Rendering spawn messages set to off"), true);
							return 1;
						})
				)
		);
	}
}

package com.random789.iron_testing_2_0.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class EfficiencyTesterCommand {

	public static int numGolems = 0;

	public static int register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("efficiencytester")
				.requires(source -> source.hasPermissionLevel(2))
				.then(
					CommandManager.literal("Golems")
						.then(
							CommandManager.argument("numGolems", IntegerArgumentType.integer(0))
								.executes(
									context -> {
										numGolems = execute(IntegerArgumentType.getInteger(context, "numGolems"));
										context.getSource().sendFeedback(() -> Text.literal("Number of Golems set to " + numGolems), true);
										return numGolems;
									}

								)
						)
				)
		);
		return 0;
	};

	private static int execute(int numGolems) {
		if (numGolems < 0) {
			return 0;
		} else {
			return numGolems;
		}
	}
}

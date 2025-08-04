package com.random789.iron_testing_2_0.mixin;

import com.nettakrim.client_execution.ExecutionNetwork;
import com.random789.iron_testing_2_0.Iron_testing_2_0;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(LargeEntitySpawnHelper.class)
public abstract class LargeEntitySpawnHelperMixin {
	@Shadow
	protected static boolean findSpawnPos(ServerWorld world, int verticalRange, BlockPos.Mutable pos, LargeEntitySpawnHelper.Requirements requirements) {
		return false;
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public static <T extends MobEntity> Optional<T> trySpawnAt(
		EntityType<T> entityType,
		SpawnReason reason,
		ServerWorld world,
		BlockPos pos,
		int tries,
		int horizontalRange,
		int verticalRange,
		LargeEntitySpawnHelper.Requirements requirements,
		boolean requireEmptySpace
	) {
		BlockPos.Mutable spawnPos = pos.mutableCopy();

		for (int failures = 0; failures < tries; failures++) {
			int j = MathHelper.nextBetween(world.random, -horizontalRange, horizontalRange);
			int k = MathHelper.nextBetween(world.random, -horizontalRange, horizontalRange);
			spawnPos.set(pos, j, verticalRange, k);
			if (world.getWorldBorder().contains(spawnPos)
				&& findSpawnPos(world, verticalRange, spawnPos, requirements)
				&& (!requireEmptySpace || world.isSpaceEmpty(entityType.getSpawnBox(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5)))) {
				T mobEntity = (T) entityType.create(world, null, spawnPos, reason, false, false);
				if (mobEntity != null) {
					if (mobEntity.canSpawn(world, reason) && mobEntity.canSpawn(world)) {
						world.spawnEntityAndPassengers(mobEntity);
						mobEntity.playAmbientSound();
						Iron_testing_2_0.LOGGER.info("Successful spawn at {}", spawnPos);
						for (ServerPlayerEntity player : world.getPlayers()) {
							ExecutionNetwork.onExecuteClient(
								player, String.format("cglow block %d %d %d 30 color green", spawnPos.getX(),spawnPos.getY(), spawnPos.getZ()));
							player.sendMessageToClient(Text.literal(String.format("Successful spawn. Failed %d attempts", failures)).styled(style -> style.withColor(Formatting.GREEN)), false);
						}

						return Optional.of(mobEntity);
					}

					mobEntity.discard();
				}
			}
			Iron_testing_2_0.LOGGER.info("Failed spawn at {}", spawnPos);
			for (ServerPlayerEntity player : world.getPlayers()) {
				ExecutionNetwork.onExecuteClient(
					player, String.format("cglow block %d %d %d 30 color red", spawnPos.getX(),spawnPos.getY(), spawnPos.getZ()));
			}


		}
		for (ServerPlayerEntity player : world.getPlayers()) {
			player.sendMessageToClient(Text.literal(String.format("Failed %d attempts", tries)).styled(style -> style.withColor(Formatting.RED)), false);
		}

		return Optional.empty();
	}
}

package com.random789.iron_testing_2_0.iron_testing_2_0Mixin;

import com.nettakrim.client_execution.ExecutionNetwork;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

		for (int i = 0; i < tries; i++) {
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
						System.out.println("Successful spawn at " + spawnPos);
						for (ServerPlayerEntity player : world.getPlayers()) {
							ExecutionNetwork.onExecuteClient(
								player, String.format("cglow block %d %d %d 300 color green", spawnPos.getX(),spawnPos.getY(), spawnPos.getZ()));
						}
						return Optional.of(mobEntity);
					}

					mobEntity.discard();
				}
			}
			System.out.println("Failed spawn at " + spawnPos);
			for (ServerPlayerEntity player : world.getPlayers()) {
				ExecutionNetwork.onExecuteClient(
					player, String.format("cglow block %d %d %d 300 color red", spawnPos.getX(),spawnPos.getY(), spawnPos.getZ()));
			}
		}

		return Optional.empty();
	}
}

package xyz.mocoder.mite.mixin.guardian;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DeepFrozenOceanBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeepFrozenOceanBiome.class)
public abstract class GuardianSpawnInDeepFrozenOceanMixin extends Biome{

	protected GuardianSpawnInDeepFrozenOceanMixin(Settings settings) {
		super(settings);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	private void initInj(CallbackInfo info) {
		this.addSpawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.GUARDIAN, 1, 4, 4));
	}
}

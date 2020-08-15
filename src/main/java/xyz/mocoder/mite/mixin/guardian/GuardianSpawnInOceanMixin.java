package xyz.mocoder.mite.mixin.guardian;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DefaultBiomeCreator.class)
public class GuardianSpawnInOceanMixin {
    @Inject(method="createOcean",at=@At("HEAD"),locals= LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void guardianSpawn(SpawnSettings.Builder builder, int waterColor, int waterFogColor, boolean deep, GenerationSettings.Builder builder2, CallbackInfoReturnable<Biome> cir) {
        builder.spawn(SpawnGroup.MONSTER,new SpawnSettings.SpawnEntry(EntityType.GUARDIAN,1,8,12));
    }
}

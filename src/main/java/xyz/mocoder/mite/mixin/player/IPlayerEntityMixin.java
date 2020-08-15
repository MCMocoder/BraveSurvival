package xyz.mocoder.mite.mixin.player;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface IPlayerEntityMixin {
    @Invoker
    SoundEvent callGetFallSound(int distance);

    @Invoker
    void callPlayBlockFallSound();
}

package xyz.mocoder.mite.mixin.player;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    //抗火I级不抗岩浆
    @Redirect(method="damage",at=@At(value="INVOKE",target="Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private boolean noProtectionInLava(LivingEntity livingEntity, StatusEffect effect) {
        if(livingEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)&&effect==StatusEffects.FIRE_RESISTANCE) {
            return livingEntity.getStatusEffect(StatusEffects.FIRE_RESISTANCE).getAmplifier() > 0 || livingEntity.getType() != EntityType.PLAYER;
        } else {
            return livingEntity.hasStatusEffect(effect);
        }
    }
}

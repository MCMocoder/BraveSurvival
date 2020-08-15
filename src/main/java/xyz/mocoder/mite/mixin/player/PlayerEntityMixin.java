package xyz.mocoder.mite.mixin.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerEntityMixin{


    //增加摔落伤害
    @Redirect(method="handleFallDamage",at=@At(value="INVOKE",target="Lnet/minecraft/entity/LivingEntity;handleFallDamage(FF)Z"))
    private boolean bonusFallDamage(LivingEntity livingEntity, float fallDistance, float damageMultiplier) {
        boolean bl = ((Entity)livingEntity).handleFallDamage(fallDistance, damageMultiplier);
        int i = computeFallDamage(livingEntity,fallDistance, damageMultiplier);
        if (i > 0) {
            livingEntity.playSound(((IPlayerEntityMixin)livingEntity).callGetFallSound(i), 1.0F, 1.0F);
            ((IPlayerEntityMixin)livingEntity).callPlayBlockFallSound();
            livingEntity.damage(DamageSource.FALL, (float)i);
            return true;
        } else {
            return bl;
        }
    }

    private int computeFallDamage(LivingEntity entity,float fallDistance,float damageMultiplier) {
        StatusEffectInstance statusEffectInstance = entity.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f = statusEffectInstance == null ? 0.0F : (float)(statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance + 13.0F - f) * damageMultiplier);
    }

    //摔落后获得debuff
    @Inject(method="handleFallDamage",at=@At(value="RETURN"))
    private void getDebuff(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir) {
        ((PlayerEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,10,0,false,false,false));
        ((PlayerEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,10,0,false,false,false));
    }
}

package xyz.mocoder.mite.mixin.piglin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinEntity.class)
public class PiglinEntityMixin {
    //增加伤害
    @Inject(method="createPiglinAttributes",at=@At("HEAD"),cancellable = true)
    private static void bonusDamage(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir){
        cir.setReturnValue(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 9.0D));
        cir.cancel();
    }

    //增加射速
    @Inject(method="attack",at=@At("HEAD"),cancellable = true)
    private void bonusSpeed(LivingEntity target, float pullProgress, CallbackInfo ci) {
        ((PiglinEntity)(Object)this).shoot(target,3.2F);
    }
}

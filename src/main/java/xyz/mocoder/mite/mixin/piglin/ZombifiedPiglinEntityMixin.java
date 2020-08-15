package xyz.mocoder.mite.mixin.piglin;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombifiedPiglinEntity.class)
public class ZombifiedPiglinEntityMixin {
    //猪人之怒也，手持金剑，追击千里
    @Inject(method="mobTick",at=@At(value="INVOKE",target="Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;tickAngerLogic(Lnet/minecraft/server/world/ServerWorld;Z)V",shift= At.Shift.AFTER))
    private void angerAtClosestPlayer(CallbackInfo ci) {
        ZombifiedPiglinEntity thi=((ZombifiedPiglinEntity)(Object)this);
        PlayerEntity closestPlayer=thi.world.getClosestPlayer(thi,20);
        if(closestPlayer!=null) {
            thi.setTarget(closestPlayer);
        }
    }

    //增加伤害
    @Inject(method="createZombifiedPiglinAttributes",at=@At("HEAD"),cancellable = true)
    private static void bonusDamage(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(ZombieEntity.createZombieAttributes().add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D));
        cir.cancel();
    }
}

package xyz.mocoder.mite.mixin.irongolem;

import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin {
    @Inject(method="tickMovement",at=@At(value="INVOKE",target="Lnet/minecraft/entity/passive/IronGolemEntity;tickAngerLogic(Lnet/minecraft/server/world/ServerWorld;Z)V"))
    private void angerAtClosestPlayer(CallbackInfo ci) {
        IronGolemEntity thi=((IronGolemEntity)(Object)this);
        PlayerEntity closestPlayer=thi.world.getClosestPlayer(thi,12);
        if(closestPlayer!=null) {
            thi.setTarget(closestPlayer);
        }
    }
}

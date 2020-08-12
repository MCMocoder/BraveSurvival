package xyz.mocoder.mite.mixin.boat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//玩家乘坐600tick后沉船
@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin {
    public int tickOn;

    @Inject(method="tick",at=@At(value="INVOKE",target="Lnet/minecraft/entity/vehicle/BoatEntity;updateVelocity()V",shift= At.Shift.AFTER))
    private void sink(CallbackInfo ci) {
        Entity primaryPassenger=((BoatEntity)(Object)this).getPrimaryPassenger();
        if(primaryPassenger instanceof PlayerEntity) {
            ++tickOn;
            if(tickOn>=600&&!((PlayerEntity) primaryPassenger).abilities.creativeMode) {
                Vec3d velocity=((BoatEntity)(Object)this).getVelocity();
                double extraVelocity=(((BoatEntity)(Object)this).getPassengerList().size())/20.0;
                if(((BoatEntity)(Object)this).getPassengerList().size()!=0) {
                    ((BoatEntity) (Object) this).setVelocity(velocity.x, 0 - extraVelocity, velocity.z);
                }
            }
        } else {
            tickOn=0;
        }
    }

    @Inject(method="<init>(Lnet/minecraft/world/World;DDD)V",at=@At("RETURN"))
    private void injInit(CallbackInfo ci) {
        tickOn=0;
    }

    @Redirect(method="tick",at=@At(value="INVOKE",target="Lnet/minecraft/entity/vehicle/BoatEntity;removeAllPassengers()V"))
    private void noRemoveWhenUnderWater(BoatEntity boatEntity) {
        ;
    }
}

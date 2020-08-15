package xyz.mocoder.mite.mixin.piglin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.PiglinSpecificSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {

    //猪灵持续敌对玩家
    @Redirect(method="sense",at=@At(value="INVOKE",target="Lnet/minecraft/entity/mob/PiglinBrain;wearsGoldArmor(Lnet/minecraft/entity/LivingEntity;)Z"))
    private boolean alwaysAngry(LivingEntity entity) {
        return false;
    }

}

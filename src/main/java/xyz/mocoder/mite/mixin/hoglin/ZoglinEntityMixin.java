package xyz.mocoder.mite.mixin.hoglin;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZoglinEntity.class)
public class ZoglinEntityMixin {
    @Inject(method="createZoglinAttributes",at=@At("HEAD"),cancellable = true)
    private static void bonusDamage(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6000000238418579D).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D));
    }
}

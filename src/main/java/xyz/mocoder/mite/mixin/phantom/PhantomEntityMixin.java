package xyz.mocoder.mite.mixin.phantom;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomEntity.class)
public class PhantomEntityMixin {
    @Inject(method="onSizeChanged",at=@At(value="HEAD",shift= At.Shift.AFTER),cancellable = true)
    private void bonusDamage(CallbackInfo ci) {
        ((PhantomEntity)(Object)this).getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(9.0D+((PhantomEntity)(Object)this).getPhantomSize());
        ci.cancel();
    }
}

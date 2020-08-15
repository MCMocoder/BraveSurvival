package xyz.mocoder.mite.mixin.creeper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends Entity {
    @Mutable
    @Final
    @Shadow
    private static final TrackedData<Boolean> CHARGED;

    //苦力怕爆炸延时，设为1，使其瞬间爆炸
    @Shadow
    private int fuseTime=1;

    @Shadow
    private int explosionRadius=6;

    static {
        CHARGED = DataTracker.registerData(CreeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    public CreeperEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //使生成的苦力怕均为高压苦力怕
    @Inject(method="initDataTracker",at=@At("RETURN"))
    private void allCharging(CallbackInfo ci) {
        this.dataTracker.set(CHARGED,true);
    }

    //使苦力怕本体隐形
    @Inject(method="<init>",at=@At("RETURN"))
    private void hideOnSpawn(CallbackInfo ci) {
        ((CreeperEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY,10,0,false,false,false));
    }

    @Inject(method="tick",at=@At("HEAD"))
    private void hidePerTick(CallbackInfo ci) {
        ((CreeperEntity)(Object)this).addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY,10,0,false,false,false));
    }

}

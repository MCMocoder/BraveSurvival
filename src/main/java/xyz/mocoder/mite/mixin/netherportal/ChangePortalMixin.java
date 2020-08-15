package xyz.mocoder.mite.mixin.netherportal;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ChangePortalMixin {
    //使下界传送门有几率破掉
    @Inject(method="tickNetherPortal",at=@At(value="INVOKE",target = "Lnet/minecraft/entity/Entity;moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;",shift= At.Shift.AFTER))
    private void breakPortal(CallbackInfo ci) {
        Entity thi=((Entity)(Object)this);
        if(thi.world.random.nextInt(5)==0&&thi.getType()== EntityType.PLAYER) {
            thi.world.setBlockState(thi.getBlockPos(), Blocks.AIR.getDefaultState());
        }
    }
}

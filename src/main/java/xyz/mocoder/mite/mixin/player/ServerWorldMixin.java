package xyz.mocoder.mite.mixin.player;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    //睡觉生成幻翼
    @Inject(method="tick",at=@At(value="INVOKE",target="Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    private void generatePhantom(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld thi=((ServerWorld)(Object)this);
        List<ServerPlayerEntity> players=thi.getPlayers();
        for(ServerPlayerEntity player:players) {
            int phantomNum=thi.random.nextInt(3)+1;
            for(int i=0;i<phantomNum;++i) {
                PhantomEntity phantom=new PhantomEntity(EntityType.PHANTOM,thi);
                phantom.updatePosition(player.getX()+thi.random.nextFloat()*10,player.getY()+20+thi.random.nextFloat()*5,player.getZ()+thi.random.nextFloat()*10);
            }
        }
    }
}

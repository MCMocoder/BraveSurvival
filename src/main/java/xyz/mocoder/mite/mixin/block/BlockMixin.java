package xyz.mocoder.mite.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Block.class)
public abstract class BlockMixin {

    //挖石头爆蠹虫
    @Inject(method="onBroken",at=@At("HEAD"),locals= LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onBrokenInj(WorldAccess world, BlockPos pos, BlockState state,CallbackInfo ci) throws Exception{
        Block blblock=state.getBlock();
        if(blblock == Blocks.STONE||blblock==Blocks.SANDSTONE||blblock==Blocks.COBBLESTONE||blblock==Blocks.ANDESITE||blblock==Blocks.GRANITE||blblock==Blocks.BASALT||blblock==Blocks.BLACKSTONE) {
            if(!world.isClient()&&world.getRandom().nextInt(16)%16==0) {
                SilverfishEntity silverfish = new SilverfishEntity(EntityType.SILVERFISH, (World) world);
                silverfish.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                world.spawnEntity(silverfish);
            }
        }
    }
}

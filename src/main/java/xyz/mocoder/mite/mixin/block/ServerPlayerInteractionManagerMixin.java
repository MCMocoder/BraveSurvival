package xyz.mocoder.mite.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    //挖矿不掉东西
    @Inject(method="tryBreakBlock",at=@At(value="INVOKE",target="Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V"),cancellable = true,locals=LocalCapture.CAPTURE_FAILEXCEPTION)
    private void noFallItem(BlockPos pos, CallbackInfoReturnable<Boolean> cir,BlockState blockState) {
        ServerPlayerInteractionManager thi=((ServerPlayerInteractionManager)(Object)this);
        Block blblock=blockState.getBlock();
        if(blblock == Blocks.STONE||blblock==Blocks.SANDSTONE||blblock==Blocks.COBBLESTONE||blblock==Blocks.ANDESITE||blblock==Blocks.GRANITE||blblock==Blocks.BASALT||blblock==Blocks.BLACKSTONE) {
            if(thi.world.random.nextInt(8)!=0){
                cir.setReturnValue(true);
                cir.cancel();
            }
        } else if(blblock== Blocks.REDSTONE_ORE||blblock==Blocks.DIAMOND_ORE||blblock==Blocks.NETHER_QUARTZ_ORE||blblock==Blocks.COAL_ORE||blblock==Blocks.EMERALD_ORE||blblock==Blocks.NETHER_GOLD_ORE) {
            int randlvl=8 - EnchantmentHelper.getLevel(Enchantments.FORTUNE,thi.player.getMainHandStack());
            System.out.println(randlvl);
            if(randlvl>0) {
                if (thi.world.random.nextInt(randlvl) > 0) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }
}

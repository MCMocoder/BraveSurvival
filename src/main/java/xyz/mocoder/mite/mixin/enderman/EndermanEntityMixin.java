package xyz.mocoder.mite.mixin.enderman;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends LivingEntity {

    protected EndermanEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //生成末影螨
    public void onDeath(DamageSource source){
        super.onDeath(source);
        int bound=((EndermanEntity)(Object)this).world.getRandom().nextInt(4);
        for(int i=0;i<bound;++i){
            EndermiteEntity endermite=new EndermiteEntity(EntityType.ENDERMITE,((EndermanEntity)(Object)this).world);
            Vec3d pos=((EndermanEntity)(Object)this).getPos();
            endermite.updatePosition(pos.x,pos.y,pos.z);
            ((EndermanEntity)(Object)this).world.spawnEntity(endermite);
        }
    }

    //破坏方块
    @Inject(method="teleportRandomly",at=@At(value="INVOKE",target="Lnet/minecraft/entity/mob/EndermanEntity;teleportTo(DDD)Z"))
    private void destroyBlock(CallbackInfoReturnable<Boolean> cir) {
        EndermanEntity thi=((EndermanEntity)(Object)this);
        Vec3d pos=thi.getPos();
        Vec3d finalPos;
        if(thi.yaw>=-45&&thi.yaw<45){
            finalPos=new Vec3d(pos.x,pos.y,pos.z+1);
        } else if(thi.yaw>=45&&thi.yaw<135){
            finalPos=new Vec3d(pos.x-1,pos.y,pos.z);
        } else if((thi.yaw>=135&&thi.yaw<=180)||(thi.yaw>=-180&&thi.yaw<-135)) {
            finalPos=new Vec3d(pos.x,pos.y,pos.z-1);
        } else {
            finalPos=new Vec3d(pos.x+1,pos.y,pos.z);
        }
        for(int i=0;i<=2;++i) {
            BlockPos ps=new BlockPos(new Vec3d(finalPos.x,finalPos.y+i,finalPos.z));
            BlockState state=thi.world.getBlockState(ps);
            if(state.getBlock()!= Blocks.AIR||state.getBlock()!=Blocks.OBSIDIAN||state.getBlock()!=Blocks.BEDROCK||
            state.getBlock()!=Blocks.RESPAWN_ANCHOR) {
                thi.world.removeBlock(new BlockPos(new Vec3d(finalPos.x,finalPos.y+i,finalPos.z)),false);
                Block.dropStacks(state,thi.world,ps);
            }
        }
    }

    //伤害增加
    @Inject(method="createEndermanAttributes",at=@At("HEAD"),cancellable = true)
    private static void bonusDamage(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir){
        cir.setReturnValue(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.75D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D));
        cir.cancel();
    }

}

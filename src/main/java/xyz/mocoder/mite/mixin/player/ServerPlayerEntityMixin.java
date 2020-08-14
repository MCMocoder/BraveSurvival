package xyz.mocoder.mite.mixin.player;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    private boolean randomDest=false;

    //挖掘疲劳，在船上时获得饥饿5
    @Inject(method="tick",at=@At("RETURN"))
    private void effects(CallbackInfo ci) {
        if(!((ServerPlayerEntity)(Object)this).abilities.creativeMode) {
            ((ServerPlayerEntity) (Object) this).addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 10, 0, false, false, false));
            Entity vehic = ((ServerPlayerEntity) (Object) this).getVehicle();
            if (vehic instanceof BoatEntity) {
                ((ServerPlayerEntity) (Object) this).addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 10, 4, false, false, false));
            } else {
                ((ServerPlayerEntity) (Object) this).removeStatusEffect(StatusEffects.HUNGER);
            }
        }
    }

    //僵尸系列
    @Inject(method="onDeath",at=@At("RETURN"))
    private void summonZombieOnDeath(CallbackInfo ci) {
        ServerPlayerEntity thi=((ServerPlayerEntity)(Object)this);
        if(thi.isTouchingWater()) {
            DrownedEntity drowned = new DrownedEntity(EntityType.DROWNED,thi.world);
            drowned.equipStack(EquipmentSlot.MAINHAND,new ItemStack(Items.TRIDENT));
            drowned.setCustomName(Text.method_30163("Grave Zombie"));
            drowned.setMovementSpeed(0.6F);
            Vec3d pos = ((ServerPlayerEntity) (Object) this).getPos();
            drowned.updatePosition(pos.x, pos.y, pos.z);
            thi.world.spawnEntity(drowned);
        } else {
            ZombieEntity zombie = new ZombieEntity(thi.world);
            zombie.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
            zombie.setCustomName(Text.method_30163("Grave Zombie"));
            zombie.setMovementSpeed(0.6F);
            Vec3d pos = ((ServerPlayerEntity) (Object) this).getPos();
            zombie.updatePosition(pos.x, pos.y, pos.z);
            thi.world.spawnEntity(zombie);
        }
    }

    //穿戴护甲：获得缓慢效果
    @Inject(method="tick",at=@At("RETURN"))
    private void slowdownWithArmor(CallbackInfo ci) {
        ServerPlayerEntity thi=((ServerPlayerEntity)(Object)this);
        Item helmet=thi.getEquippedStack(EquipmentSlot.HEAD).getItem();
        Item chestplate=thi.getEquippedStack(EquipmentSlot.CHEST).getItem();
        Item leggings=thi.getEquippedStack(EquipmentSlot.LEGS).getItem();
        Item boot=thi.getEquippedStack(EquipmentSlot.FEET).getItem();

        int slowDownIndex=0;

        if(helmet==Items.CHAINMAIL_HELMET){
            slowDownIndex+=2;
        } else if(helmet==Items.GOLDEN_HELMET||helmet==Items.IRON_HELMET){
            slowDownIndex+=2;
        } else if(helmet==Items.DIAMOND_HELMET||helmet==Items.NETHERITE_HELMET){
            slowDownIndex+=3;
        }

        if(chestplate==Items.CHAINMAIL_CHESTPLATE){
            slowDownIndex+=5;
        } else if(chestplate==Items.GOLDEN_CHESTPLATE||chestplate==Items.IRON_CHESTPLATE){
            slowDownIndex+=6;
        } else if(chestplate==Items.DIAMOND_CHESTPLATE||chestplate==Items.NETHERITE_CHESTPLATE){
            slowDownIndex+=8;
        }

        if(leggings==Items.CHAINMAIL_LEGGINGS){
            slowDownIndex+=4;
        } else if(leggings==Items.GOLDEN_LEGGINGS||leggings==Items.IRON_LEGGINGS){
            slowDownIndex+=5;
        } else if(leggings==Items.DIAMOND_LEGGINGS||leggings==Items.NETHERITE_LEGGINGS){
            slowDownIndex+=6;
        }

        if(boot==Items.CHAINMAIL_BOOTS){
            slowDownIndex+=1;
        } else if(boot==Items.GOLDEN_BOOTS||boot==Items.IRON_BOOTS){
            slowDownIndex+=2;
        } else if(boot==Items.DIAMOND_BOOTS||boot==Items.NETHERITE_BOOTS){
            slowDownIndex+=3;
        }

        slowDownIndex=(int)Math.ceil(((double)slowDownIndex)/10)-1;
        if(!(slowDownIndex<0)) {
            thi.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, slowDownIndex, false, false, false));
        }
    }

    //多喝岩浆&你火了
    @Inject(method="tick",at=@At("RETURN"))
    private void fireAndLava(CallbackInfo ci) {
        ServerPlayerEntity thi=((ServerPlayerEntity)(Object)this);
        if(thi.inventory.getSlotWithStack(new ItemStack(Items.LAVA_BUCKET))!=-1||isLavaNear(thi.getPos())) {
            thi.setOnFireFor(10);
        }
        if(thi.isOnFire()) {
            thi.world.setBlockState(new BlockPos(thi.getPos()),Blocks.FIRE.getDefaultState());
        }
    }

    private boolean isLavaNear(Vec3d pos) {
        ServerPlayerEntity thi=((ServerPlayerEntity)(Object)this);
        int posx=(int)pos.x;
        int posy=(int)pos.y;
        int posz=(int)pos.z;
        for(int i=-2;i<=2;++i) {
            for(int k=-2;k<=2;++k) {
                for(int j=0;j<=2;++j){
                    if(thi.world.getBlockState(new BlockPos(new Vec3d(posx+i,posy+j,posz+k))).getBlock()==Blocks.LAVA) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //四川美食生成器,疣猪兽生成器
    @Inject(method="tick",at=@At("RETURN"))
    private void transRabbit(CallbackInfo ci) {
        ServerPlayerEntity thi=((ServerPlayerEntity)(Object)this);
        Entity entity=getTargetedEntity();
        if (entity != null) {
            if(entity.getType()==EntityType.COW||entity.getType()==EntityType.SHEEP||entity.getType()==EntityType.CHICKEN) {
                RabbitEntity rabbit=new RabbitEntity(EntityType.RABBIT,thi.world);
                rabbit.updatePosition(entity.getX(),entity.getY(),entity.getZ());
                entity.remove();
                thi.world.spawnEntity(rabbit);
            } else if(entity.getType()==EntityType.PIG) {
                HoglinEntity hoglin=new HoglinEntity(EntityType.HOGLIN,thi.world);
                hoglin.updatePosition(entity.getX(),entity.getY(),entity.getZ());
                entity.remove();
                thi.world.spawnEntity(hoglin);
            }
        }
    }

    //检测看向的实体，根据GameRenderer类中updateTargetedEntity函数改写
    private Entity getTargetedEntity() {
        ServerPlayerEntity thi=((ServerPlayerEntity)(Object)this);
        double d=8.0D;
        HitResult res=thi.rayTrace(d,1.0F,false);
        Vec3d vec3d=thi.getCameraPosVec(1.0F);
        double e=d*d;
        if(res!=null) {
            e=res.getPos().squaredDistanceTo(vec3d);
        }
        Vec3d vec3d2=thi.getRotationVec(1.0F);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        Box box=thi.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.rayTrace(thi, vec3d, vec3d3, box, (entityx) -> !entityx.isSpectator() && entityx.collides(), e);
        if (entityHitResult != null) {
            Entity entity2 = entityHitResult.getEntity();
            Vec3d vec3d4 = entityHitResult.getPos();
            double g = vec3d.squaredDistanceTo(vec3d4);
            if (g < e || res == null) {
                if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                    return entity2;
                }
            }
        }
        return null;
    }

    //一定概率随机传送
    @Redirect(method="changeDimension",at=@At(value="INVOKE",target="Lnet/minecraft/server/network/ServerPlayerEntity;getX()D"))
    private double randomDestinationX(ServerPlayerEntity serverPlayerEntity) {
        if(serverPlayerEntity.getRandom().nextInt(1000)==0) {
            randomDest = true;
            return serverPlayerEntity.getX() + serverPlayerEntity.getRandom().nextDouble() * 256;
        } else {
            return serverPlayerEntity.getX();
        }
    }

    @Redirect(method="changeDimension",at=@At(value="INVOKE",target="Lnet/minecraft/server/network/ServerPlayerEntity;getZ()D"))
    private double randomDestinationZ(ServerPlayerEntity serverPlayerEntity) {
        if(randomDest) {
            randomDest=false;
            return serverPlayerEntity.getZ() + serverPlayerEntity.getRandom().nextDouble() * 256;
        } else {
            return serverPlayerEntity.getZ();
        }
    }
}

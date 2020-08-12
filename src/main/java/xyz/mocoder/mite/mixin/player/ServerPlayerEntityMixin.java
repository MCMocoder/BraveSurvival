package xyz.mocoder.mite.mixin.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    public long tickOnBoat=0;


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
        ZombieEntity zombie=new ZombieEntity(((ServerPlayerEntity)(Object)this).world);
        zombie.equipStack(EquipmentSlot.HEAD,new ItemStack(Items.LEATHER_HELMET));
        zombie.setCustomName(Text.method_30163("Grave Zombie"));
        Vec3d pos=((ServerPlayerEntity)(Object)this).getPos();
        zombie.updatePosition(pos.x,pos.y,pos.z);
        ((ServerPlayerEntity)(Object)this).world.spawnEntity(zombie);
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
}

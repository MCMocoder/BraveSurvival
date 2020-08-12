package xyz.mocoder.mite.mixin.zombie;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MobEntity {

    protected ZombieEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    //护甲：提高铁甲和钻石甲生成率，添加下界合金甲生成，提高高级附魔生成率
    @Redirect(method="initEquipment",at=@At(value="INVOKE",target="Lnet/minecraft/entity/mob/HostileEntity;initEquipment(Lnet/minecraft/world/LocalDifficulty;)V"))
    private void initequ(HostileEntity hostileEntity, LocalDifficulty difficulty){
        if (hostileEntity.getRandom().nextFloat() < 0.99F * difficulty.getClampedLocalDifficulty()) {
            int i = hostileEntity.getRandom().nextInt(2);
            float f = hostileEntity.world.getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
            if (hostileEntity.getRandom().nextFloat() < 0.9F) {
                ++i;
            }

            if (hostileEntity.getRandom().nextFloat() < 0.8F) {
                ++i;
            }

            if (hostileEntity.getRandom().nextFloat() < 0.7F) {
                ++i;
            }

            boolean bl = true;
            EquipmentSlot[] var5 = EquipmentSlot.values();

            for (EquipmentSlot equipmentSlot : var5) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack itemStack = hostileEntity.getEquippedStack(equipmentSlot);
                    if (!bl && hostileEntity.getRandom().nextFloat() < f) {
                        break;
                    }

                    bl = false;
                    if (itemStack.isEmpty()) {
                        Item item = getEquFS(equipmentSlot, i);
                        if (item != null) {
                            hostileEntity.equipStack(equipmentSlot, new ItemStack(item));
                        }
                    }
                }
            }
        }
    }

    private static Item getEquFS(EquipmentSlot equipmentSlot, int equipmentLevel){
        switch(equipmentSlot) {
            case HEAD:
                if (equipmentLevel == 0) {
                    return Items.GOLDEN_HELMET;
                } else if (equipmentLevel == 1) {
                    return Items.CHAINMAIL_HELMET;
                } else if (equipmentLevel == 2) {
                    return Items.IRON_HELMET;
                } else if (equipmentLevel == 3) {
                    return Items.DIAMOND_HELMET;
                } else if (equipmentLevel == 4) {
                    return Items.NETHERITE_HELMET;
                }
            case CHEST:
                if (equipmentLevel == 0) {
                    return Items.GOLDEN_CHESTPLATE;
                } else if (equipmentLevel == 1) {
                    return Items.CHAINMAIL_CHESTPLATE;
                } else if (equipmentLevel == 2) {
                    return Items.IRON_CHESTPLATE;
                } else if (equipmentLevel == 3) {
                    return Items.DIAMOND_CHESTPLATE;
                } else if (equipmentLevel == 4) {
                    return Items.NETHERITE_CHESTPLATE;
                }
            case LEGS:
                if (equipmentLevel == 0) {
                    return Items.GOLDEN_LEGGINGS;
                } else if (equipmentLevel == 1) {
                    return Items.CHAINMAIL_LEGGINGS;
                } else if (equipmentLevel == 2) {
                    return Items.IRON_LEGGINGS;
                } else if (equipmentLevel == 3) {
                    return Items.DIAMOND_LEGGINGS;
                } else if (equipmentLevel == 4) {
                    return Items.NETHERITE_LEGGINGS;
                }
            case FEET:
                if (equipmentLevel == 0) {
                    return Items.GOLDEN_BOOTS;
                } else if (equipmentLevel == 1) {
                    return Items.CHAINMAIL_BOOTS;
                } else if (equipmentLevel == 2) {
                    return Items.IRON_BOOTS;
                } else if (equipmentLevel == 3) {
                    return Items.DIAMOND_BOOTS;
                } else if (equipmentLevel == 4) {
                    return Items.NETHERITE_BOOTS;
                }
            default:
                return null;
        }
    }

    @Redirect(method="initialize",at=@At(value="INVOKE",target="Lnet/minecraft/entity/mob/ZombieEntity;updateEnchantments(Lnet/minecraft/world/LocalDifficulty;)V"))
    private void updateEnch(ZombieEntity zombieEntity, LocalDifficulty difficulty){
        float f = difficulty.getClampedLocalDifficulty();
        if (!zombieEntity.getMainHandStack().isEmpty() && zombieEntity.getRandom().nextFloat() < 0.97F * f) {
            this.equipStack(EquipmentSlot.MAINHAND, EnchantmentHelper.enchant(zombieEntity.getRandom(), zombieEntity.getMainHandStack(), (int)(25.0F + f * (float)zombieEntity.getRandom().nextInt(18)), false));
        }

        EquipmentSlot[] var3 = EquipmentSlot.values();

        for (EquipmentSlot equipmentSlot : var3) {
            if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack itemStack = zombieEntity.getEquippedStack(equipmentSlot);
                if (!itemStack.isEmpty() && zombieEntity.getRandom().nextFloat() < 1.2F * f) {
                    zombieEntity.equipStack(equipmentSlot, EnchantmentHelper.enchant(zombieEntity.getRandom(), itemStack, (int) (25.0F + f * (float) zombieEntity.getRandom().nextInt(18)), false));
                }
            }
        }
    }
}

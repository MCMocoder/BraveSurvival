package xyz.mocoder.mite.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ShieldMixin {
    /*@Final
    @Shadow
    public static final Item SHIELD= Registry.set(Registry.ITEM,new Identifier("shield"),new ShieldItem((new Item.Settings()).maxDamage(50).group(ItemGroup.COMBAT)));*/
}

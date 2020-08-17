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
    @Inject(method="register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",at=@At(value="HEAD"),cancellable = true)
    private static void changeItemSetting(Identifier id, Item item, CallbackInfoReturnable<Item> cir) {
        if(item instanceof ShieldItem) {
            cir.setReturnValue((Item)Registry.register(Registry.ITEM,id,(Item)(new ShieldItem((new Item.Settings()).maxDamage(200).group(ItemGroup.COMBAT)))));
            cir.cancel();
        }
    }
}

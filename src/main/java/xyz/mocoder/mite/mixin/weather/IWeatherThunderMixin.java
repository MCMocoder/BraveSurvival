package xyz.mocoder.mite.mixin.weather;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWorld.class)
public interface IWeatherThunderMixin {
    @Invoker
    BlockPos callGetSurface(BlockPos pos);
}

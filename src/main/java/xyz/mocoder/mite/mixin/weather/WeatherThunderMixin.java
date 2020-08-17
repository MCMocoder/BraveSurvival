package xyz.mocoder.mite.mixin.weather;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xyz.mocoder.mite.math.FunctionalRandom;

import java.util.List;
import java.util.Random;

//高密度雷，在玩家附近正态分布生成；提高骷髅马生成率
@Mixin(ServerWorld.class)
public abstract class WeatherThunderMixin implements IWeatherThunderMixin{



    /**
     * @author MCMocoder
     * @reason Need change
     */
    @Overwrite
    public void tickChunk(WorldChunk chunk, int randomTickSpeed) throws Exception{
        ChunkPos chunkPos = chunk.getPos();
        boolean bl = ((ServerWorld) (Object) this).isRaining();
        int i = chunkPos.getStartX();
        int j = chunkPos.getStartZ();
        Profiler profiler = ((ServerWorld) (Object) this).getProfiler();
        profiler.push("thunder");
        BlockPos blockPos2;
        double[] dists={4.0D,8.0D,16.0D};

        if (bl && ((ServerWorld) (Object) this).isThundering() && ((ServerWorld) (Object) this).random.nextInt(500) == 0) {
            for(double dist:dists) {
                List<ServerPlayerEntity> players=((ServerWorld)(Object)this).getPlayers();
                ServerPlayerEntity randomPlayer=players.get(((ServerWorld)(Object)this).random.nextInt(players.size()));
                BlockPos movePos=genPos(((ServerWorld)(Object)this).random,dist);
                BlockPos blockPos3=new BlockPos(new Vec3d(movePos.getX()+randomPlayer.getX(),0,movePos.getZ()+randomPlayer.getZ()));
                blockPos2 = callGetSurface(blockPos3);
                if (((ServerWorld) (Object) this).hasRain(blockPos2)) {
                    LocalDifficulty localDifficulty = ((ServerWorld) (Object) this).getLocalDifficulty(blockPos2);
                    boolean bl2 = ((ServerWorld) (Object) this).getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && ((ServerWorld) (Object) this).random.nextDouble() < (double) localDifficulty.getLocalDifficulty() * 0.01D;
                    if (bl2) {
                        SkeletonHorseEntity skeletonHorseEntity = (SkeletonHorseEntity) EntityType.SKELETON_HORSE.create(((ServerWorld) (Object) this));
                        assert skeletonHorseEntity != null;
                        skeletonHorseEntity.setTrapped(true);
                        skeletonHorseEntity.setBreedingAge(0);
                        skeletonHorseEntity.updatePosition((double) blockPos2.getX(), (double) blockPos2.getY(), (double) blockPos2.getZ());
                        ((ServerWorld) (Object) this).spawnEntity(skeletonHorseEntity);
                    }

                    LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(((ServerWorld) (Object) this));
                    assert lightningEntity != null;
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos2));
                    lightningEntity.setCosmetic(bl2);
                    ((ServerWorld) (Object) this).spawnEntity(lightningEntity);
                }
            }
        }
    }

    private BlockPos genPos(Random random,double distIndex) {
        return new BlockPos(new Vec3d(FunctionalRandom.generateRandom(random)*distIndex,0,FunctionalRandom.generateRandom(random)*distIndex));
    }
}

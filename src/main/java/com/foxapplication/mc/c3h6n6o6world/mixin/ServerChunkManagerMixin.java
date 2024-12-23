package com.foxapplication.mc.c3h6n6o6world.mixin;

import com.foxapplication.mc.c3h6n6o6world.C3H6N6O6World;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.OptionalChunk;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(value = ServerChunkManager.class, priority = 1500)
public abstract class ServerChunkManagerMixin extends ChunkManager {

    @Shadow
    @Final
    @Mutable
    public ServerChunkManager.MainThreadExecutor mainThreadExecutor;
    @Shadow
    @Final
    @Mutable
    private ServerWorld world;
    @Shadow
    @Final
    @Mutable
    private ChunkStatus[] chunkStatusCache;
    @Shadow
    @Final
    @Mutable
    private long[] chunkPosCache;
    @Shadow
    @Final
    @Mutable
    private Chunk[] chunkCache;



    @Shadow
    protected abstract void putInCache(long pos, Chunk chunk, ChunkStatus status);

    @Shadow
    protected abstract CompletableFuture<OptionalChunk<Chunk>> getChunkFuture(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create);


    @Redirect(method = /*{"getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;",*/ "getWorldChunk"/*}*/, at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerChunkManager;serverThread:Ljava/lang/Thread;", opcode = Opcodes.GETFIELD))
    private Thread overwriteServerThread(ServerChunkManager mgr) {
        return Thread.currentThread();
    }

    //TODO: 彻底修复线程读取问题，现在只是临时解决方案
    @Redirect(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"))
    private Thread overwriteGetChunkServerThread() {
        if (Thread.currentThread().getName().startsWith("C3H6N6O6")) {
            return C3H6N6O6World.ServerThread;
        }
        return Thread.currentThread();
    }
    /*
    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;",at = @At("HEAD"),cancellable = true)
    private void fixThread(int x, int z, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<Chunk> cir){
        //serverThread = CalculationController.getServer().getThread();
        if (Thread.currentThread().getName().startsWith("C3H6N6O6")) {
            Chunk chunk2;
            Profiler profiler = this.world.getProfiler();
            profiler.visit("getChunk");
            long l = ChunkPos.toLong(x, z);
            for (int i = 0; i < 4; ++i) {
                if (l != this.chunkPosCache[i] || leastStatus != this.chunkStatusCache[i] || (chunk2 = this.chunkCache[i]) == null && create) continue;
                cir.setReturnValue(chunk2);
                cir.cancel();
            }
            profiler.visit("getChunkCacheMiss");
            CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> completableFuture = this.getChunkFuture(x, z, leastStatus, create);
            this.mainThreadExecutor.runTasks(completableFuture::isDone);
            chunk2 = completableFuture.join().map(chunk -> chunk, unloaded -> {
                if (create) {
                    throw Util.throwOrPause(new IllegalStateException("Chunk not there when requested: " + unloaded));
                }
                return null;
            });
            this.putInCache(l, chunk2, leastStatus);
            cir.setReturnValue(chunk2);
            cir.cancel();
        }
    }

     */
    /*
    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;",at = @At("RETURN"))
    private void fixThreadEnd(int x, int z, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<Chunk> cir){
        serverThread = C3H6N6O6.ServerThread;
    }

    /*
        @Redirect(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;visit(Ljava/lang/String;)V"))
        private void overwriteProfilerVisit(Profiler instance, String s) {
            if (ParallelProcessor.shouldThreadChunks())
                return;
            else instance.visit("getChunkCacheMiss");
        }



        @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager$MainThreadExecutor;runTasks(Ljava/util/function/BooleanSupplier;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
        private void callCompletableFutureHook(int x, int z, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<Chunk> cir, Profiler profiler, long chunkPos, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> i) {
            DebugHookTerminator.chunkLoadDrive(this.mainThreadExecutor, i::isDone, (ServerChunkManager) (Object) this, i, chunkPos);

        }

     */

    /*
    @Nullable
    @Override
    public synchronized Chunk getChunk(int x, int z, ChunkStatus leastStatus, boolean create) {
        Chunk chunk2;
        Profiler profiler = this.world.getProfiler();
        profiler.visit("getChunk");
        long l = ChunkPos.toLong(x, z);
        for (int i = 0; i < 4; ++i) {
            if (l != this.chunkPosCache[i] || leastStatus != this.chunkStatusCache[i] || (chunk2 = this.chunkCache[i]) == null && create) continue;
            return chunk2;
        }
        profiler.visit("getChunkCacheMiss");
        CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>> completableFuture = this.getChunkFuture(x, z, leastStatus, create);
        this.mainThreadExecutor.runTasks(completableFuture::isDone);
        chunk2 = completableFuture.join().map(chunk -> chunk, unloaded -> {
            if (create) {
                throw Util.throwOrPause(new IllegalStateException("Chunk not there when requested: " + unloaded));
            }
            return null;
        });
        this.putInCache(l, chunk2, leastStatus);
        return chunk2;
    }

     */
}
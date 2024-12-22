package com.foxapplication.mc.c3h6n6o6world.mixin;

import com.foxapplication.mc.c3h6n6o6world.fastutil.ConcurrentCollections;
import com.foxapplication.mc.c3h6n6o6world.fastutil.ConcurrentLongLinkedOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ChunkTicketManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

/**
 * 数据结构改造用Mixin
 */
@Mixin(ChunkTicketManager.class)
public abstract class ChunkTicketManagerMixin {
    /**
     */
    @Shadow
    @Final
    @Mutable
    Set<ChunkHolder> chunkHoldersWithPendingUpdates = ConcurrentCollections.newHashSet();
    /**
     */
    @Shadow
    @Final
    @Mutable
    LongSet freshPlayerTicketPositions = new ConcurrentLongLinkedOpenHashSet();
}

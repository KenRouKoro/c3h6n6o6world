package com.foxapplication.mc.c3h6n6o6world.mixin;

import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * 数据结构改造用Mixin
 */
@Mixin(DamageTracker.class)
public class DamageTrackerMixin {
    /**
     */
    @Shadow
    @Final
    @Mutable
    private List<DamageRecord> recentDamage = new CopyOnWriteArrayList<>();
}

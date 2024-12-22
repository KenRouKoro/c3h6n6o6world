package com.foxapplication.mc.c3h6n6o6world.mixin;

import net.minecraft.entity.ai.pathing.PathMinHeap;
import net.minecraft.entity.ai.pathing.PathNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 能避免一些溢出问题（（（
 */
@Mixin(PathMinHeap.class)
public class PathMinHeapMixin {
    /**
     * 溢出修复.png
     */
    @Inject(method = "push", at = @At("HEAD"),cancellable = true)
    private void injected(PathNode node, CallbackInfoReturnable<PathNode> cir) {
        if (node.heapIndex >= 0) {
            cir.setReturnValue(node);
            cir.cancel();
        }
    }

    /**
     * 溢出修复.png
     */
    @Inject(method = "shiftUp",at = @At("HEAD"),cancellable = true)
    private void shiftUPFix(int index, CallbackInfo ci){
            if(index ==-1){
                ci.cancel();
            }
    }

    /**
     * 溢出修复.png
     */
    @Inject(method = "shiftDown",at = @At("HEAD"),cancellable = true)
    private void shiftDownFix(int index, CallbackInfo ci){
        if(index ==-1){
            ci.cancel();
        }
    }
}

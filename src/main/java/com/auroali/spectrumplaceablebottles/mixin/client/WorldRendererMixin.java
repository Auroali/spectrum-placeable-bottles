package com.auroali.spectrumplaceablebottles.mixin.client;

import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @WrapWithCondition(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addBlockBreakParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    public boolean spectrumplaceablebottles$preventBreakParticles(ClientWorld instance, BlockPos pos, BlockState state) {
        return !(state.getBlock() instanceof TriPlacementBlock);
    }
}

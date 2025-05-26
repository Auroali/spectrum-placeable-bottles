package com.auroali.spectrumplaceablebottles;

import com.auroali.spectrumplaceablebottles.client.SPBModelPlugin;
import com.auroali.spectrumplaceablebottles.client.colors.TriPlacementColorProvider;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;

public class SpectrumPlaceableBottlesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new SPBModelPlugin());

        ColorProviderRegistry.BLOCK.register(
          TriPlacementColorProvider.PROVIDER,
          SPBBlocks.BOTTLES
        );

        BlockRenderLayerMap.INSTANCE.putBlock(SPBBlocks.BOTTLES, RenderLayer.getTranslucent());
    }
}

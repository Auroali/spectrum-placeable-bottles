package com.auroali.spectrumplaceablebottles;

import com.auroali.spectrumplaceablebottles.client.SPBModelPlugin;
import com.auroali.spectrumplaceablebottles.client.colors.TriPlacementColorProvider;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlocks;
import com.auroali.spectrumplaceablebottles.common.registry.SPBPlaceableItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SpectrumPlaceableBottlesClient implements ClientModInitializer {
    private static final Text TOOLTIP_TEXT = Text.translatable("item.spectrumplaceablebottles.tooltip.placeable").formatted(Formatting.GRAY);

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new SPBModelPlugin());

        ColorProviderRegistry.BLOCK.register(
          TriPlacementColorProvider.PROVIDER,
          SPBBlocks.BOTTLES
        );

        BlockRenderLayerMap.INSTANCE.putBlock(SPBBlocks.BOTTLES, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SPBBlocks.MUGS, RenderLayer.getCutout());

        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, list) -> {
            if (SPBPlaceableItems.ALL.contains(itemStack))
                list.add(TOOLTIP_TEXT);
        });
    }
}

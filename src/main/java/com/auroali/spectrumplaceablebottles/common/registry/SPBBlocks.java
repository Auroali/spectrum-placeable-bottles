package com.auroali.spectrumplaceablebottles.common.registry;

import com.auroali.spectrumplaceablebottles.SpectrumPlaceableBottles;
import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class SPBBlocks {
    public static final Block BOTTLES = new TriPlacementBlock(AbstractBlock.Settings
      .create()
      .sounds(BlockSoundGroup.GLASS)
      .nonOpaque()
      .strength(0.1f),
      SPBPlaceableItems.BOTTLES
    );

    public static void register() {
        Registry.register(Registries.BLOCK, SpectrumPlaceableBottles.id("bottles"), BOTTLES);
    }
}

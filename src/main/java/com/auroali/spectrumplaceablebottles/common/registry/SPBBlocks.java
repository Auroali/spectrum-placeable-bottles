package com.auroali.spectrumplaceablebottles.common.registry;

import com.auroali.spectrumplaceablebottles.SpectrumPlaceableBottles;
import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.shape.VoxelShapes;

public class SPBBlocks {
    public static final Block BOTTLES = new TriPlacementBlock(AbstractBlock.Settings
      .create()
      .sounds(BlockSoundGroup.GLASS)
      .nonOpaque()
      .strength(0.1f),
      SPBPlaceableItems.BOTTLES,
      VoxelShapes.cuboid(
        0.25f, 0, 0.25f,
        0.75f, 0.75f, 0.75f
      ),
      VoxelShapes.cuboid(
        0.1875f, 0, 0.1875f,
        0.8125f, 0.75f, 0.8125f
      ),
      VoxelShapes.cuboid(
        0.1875f, 0, 0.1875f,
        0.8125f, 0.75f, 0.8125f
      )
    );

    public static final Block MUGS = new TriPlacementBlock(AbstractBlock.Settings
      .create()
      .sounds(BlockSoundGroup.DEEPSLATE_TILES)
      .nonOpaque()
      .strength(0.1f),
      SPBPlaceableItems.MUGS,
      VoxelShapes.cuboid(
        0.375f, 0, 0.375f,
        0.625f, 0.3125f, 0.625f
      ),
      VoxelShapes.cuboid(
        0.1875f, 0, 0.1875f,
        0.8125f, 0.3125f, 0.8125f
      ),
      VoxelShapes.cuboid(
        0.1875f, 0, 0.1875f,
        0.8125f, 0.3125f, 0.8125f
      )
    );

    public static final Block[] ALL_PLACEABLES = {
      BOTTLES,
      MUGS
    };

    public static void register() {
        Registry.register(Registries.BLOCK, SpectrumPlaceableBottles.id("bottles"), BOTTLES);
        Registry.register(Registries.BLOCK, SpectrumPlaceableBottles.id("mugs"), MUGS);
    }
}

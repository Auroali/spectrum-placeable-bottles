package com.auroali.spectrumplaceablebottles.common.registry;

import com.auroali.spectrumplaceablebottles.SpectrumPlaceableBottles;
import com.auroali.spectrumplaceablebottles.common.blockentities.TriPlacementBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SPBBlockEntities {
    public static final BlockEntityType<TriPlacementBlockEntity> TRI_PLACEMENT_ENTITY = BlockEntityType.Builder.create(
      TriPlacementBlockEntity::new,
      SPBBlocks.BOTTLES, SPBBlocks.MUGS
    ).build(null);

    public static void register() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, SpectrumPlaceableBottles.id("tri_placement"), TRI_PLACEMENT_ENTITY);
    }
}

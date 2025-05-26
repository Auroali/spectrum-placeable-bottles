package com.auroali.spectrumplaceablebottles.datagen;

import com.auroali.spectrumplaceablebottles.SpectrumPlaceableBottles;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SPBModelProvider extends FabricModelProvider {
    private static final Model EMPTY = new Model(
      Optional.of(SpectrumPlaceableBottles.id("block/templates/empty")),
      Optional.empty()
    );

    public SPBModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        this.registerTriPlacement(blockStateModelGenerator, SPBBlocks.BOTTLES);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }

    private void registerTriPlacement(BlockStateModelGenerator generator, Block block) {
        generator.excludeFromSimpleItemModelGeneration(block);
        Identifier model = EMPTY.upload(block, new TextureMap(), generator.modelCollector);
        generator.blockStateCollector.accept(
          VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, model))
        );
    }
}

package com.auroali.spectrumplaceablebottles.datagen;

import com.auroali.spectrumplaceablebottles.common.registry.SPBPlaceableItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.SimpleModelSupplier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class SPBResourcePackModelProvider extends FabricModelProvider {
    public SPBResourcePackModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Item item : SPBPlaceableItems.ALL) {
            Identifier parent = Registries.ITEM.getId(item).withPrefixedPath("placeablebottles/");
            itemModelGenerator.writer.accept(
              ModelIds.getItemModelId(item),
              new SimpleModelSupplier(parent)
            );
        }
    }
}

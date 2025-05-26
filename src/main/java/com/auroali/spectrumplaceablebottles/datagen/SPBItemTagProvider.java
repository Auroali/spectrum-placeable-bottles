package com.auroali.spectrumplaceablebottles.datagen;

import com.auroali.spectrumplaceablebottles.SpectrumPlaceableBottles;
import com.auroali.spectrumplaceablebottles.common.registry.SPBPlaceableItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class SPBItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public SPBItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // these aren't used internally, and only exist for datapack use
        // so its ok to only reference them here
        // ideally tho placeables would be controlled via tag, but tags aren't available at
        // model bake time so it has to be hardcoded
        TagKey<Item> bottles = TagKey.of(RegistryKeys.ITEM, SpectrumPlaceableBottles.id("bottles"));
        TagKey<Item> mugs = TagKey.of(RegistryKeys.ITEM, SpectrumPlaceableBottles.id("mugs"));
        TagKey<Item> all = TagKey.of(RegistryKeys.ITEM, SpectrumPlaceableBottles.id("placeable"));

        this.getOrCreateTagBuilder(bottles)
          .add(SPBPlaceableItems.BOTTLES.toArray(Item[]::new));
        this.getOrCreateTagBuilder(mugs)
          .add(SPBPlaceableItems.MUGS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(all)
          .addTag(bottles)
          .addTag(mugs);
    }
}

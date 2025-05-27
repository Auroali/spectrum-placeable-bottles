package com.auroali.spectrumplaceablebottles;

import com.auroali.spectrumplaceablebottles.datagen.SPBItemTagProvider;
import com.auroali.spectrumplaceablebottles.datagen.SPBLangProvider;
import com.auroali.spectrumplaceablebottles.datagen.SPBResourcePackModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SpectrumPlaceableBottlesDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SPBLangProvider::new);
        pack.addProvider(SPBItemTagProvider::new);

        // the builtin 3d items pack
        FabricDataGenerator.Pack builtinPack = fabricDataGenerator.createBuiltinResourcePack(SpectrumPlaceableBottles.id("3d_items"));
        builtinPack.addProvider(SPBResourcePackModelProvider::new);
    }
}

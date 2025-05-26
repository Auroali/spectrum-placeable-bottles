package com.auroali.spectrumplaceablebottles;

import com.auroali.spectrumplaceablebottles.datagen.SPBLangProvider;
import com.auroali.spectrumplaceablebottles.datagen.SPBModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SpectrumPlaceableBottlesDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SPBLangProvider::new);
        pack.addProvider(SPBModelProvider::new);
    }
}

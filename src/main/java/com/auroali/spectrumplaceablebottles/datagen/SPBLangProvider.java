package com.auroali.spectrumplaceablebottles.datagen;

import com.auroali.spectrumplaceablebottles.common.registry.SPBBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class SPBLangProvider extends FabricLanguageProvider {
    public SPBLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(SPBBlocks.BOTTLES, "Bottles");
    }
}

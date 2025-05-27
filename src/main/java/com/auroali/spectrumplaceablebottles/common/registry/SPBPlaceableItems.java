package com.auroali.spectrumplaceablebottles.common.registry;

import com.auroali.spectrumplaceablebottles.common.blocks.AcceptableItemSet;
import de.dafuqs.spectrum.registries.SpectrumItems;

import java.util.Set;

public class SPBPlaceableItems {
    public static final AcceptableItemSet BOTTLES = AcceptableItemSet.of(() -> Set.of(
      SpectrumItems.INFUSED_BEVERAGE,
      SpectrumItems.CHRYSOCOLLA,
      SpectrumItems.JADE_WINE,
      SpectrumItems.AQUA_REGIA,
      SpectrumItems.NECTERED_VIOGNIER,
      SpectrumItems.EVERNECTAR,
      SpectrumItems.FREIGEIST,
      SpectrumItems.MORCHELLA,
      SpectrumItems.PURE_ALCOHOL,
      SpectrumItems.SUSPICIOUS_BREW,
      SpectrumItems.REPRISE,
      SpectrumItems.BRISTLE_MEAD,
      SpectrumItems.BITTER_OILS
    ));

    public static final AcceptableItemSet MUGS = AcceptableItemSet.of(() -> Set.of(
      SpectrumItems.HOT_CHOCOLATE,
      SpectrumItems.RESTORATION_TEA,
      SpectrumItems.AZALEA_TEA,
      SpectrumItems.DEMON_TEA,
      SpectrumItems.GLISTERING_JELLY_TEA,
      SpectrumItems.GOLDEN_BRISTLE_TEA,
      SpectrumItems.KARAK_CHAI,
      SpectrumItems.SLUSHSLIDE,
      SpectrumItems.PEACH_CREAM,
      SpectrumItems.JUNKET
    ));

    public static final AcceptableItemSet ALL = AcceptableItemSet.composite(
      BOTTLES,
      MUGS
    );
}

package com.auroali.spectrumplaceablebottles.client.models;

import com.auroali.spectrumplaceablebottles.client.SPBModelPlugin;
import com.auroali.spectrumplaceablebottles.common.blocks.AcceptableItemSet;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Represents an unbaked {@link TriPlacementModel}. Generates the item model map when baked
 */
public class UnbakedTriPlacementModel implements UnbakedModel {
    private final AcceptableItemSet items;
    private final List<Identifier> dependencies;

    public UnbakedTriPlacementModel(AcceptableItemSet set) {
        this.items = set;
        this.dependencies = new ArrayList<>();
        SPBModelPlugin.forEachPlaceableIdentifier(set, (item, id, count, index) -> this.dependencies.add(id));
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return this.dependencies;
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Override
    public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        HashMap<TriPlacementModel.ModelKey, BakedModel> baked = SPBModelPlugin.bakeModels(baker, this.items, rotationContainer);

        return new TriPlacementModel(
          baked,
          // todo: find a way to disable the particle?
          textureGetter.apply(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("spectrum", "item/infused_beverage")))
        );
    }
}

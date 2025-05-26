package com.auroali.spectrumplaceablebottles.client;

import com.auroali.spectrumplaceablebottles.SpectrumPlaceableBottles;
import com.auroali.spectrumplaceablebottles.client.models.TriPlacementModel;
import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SPBModelPlugin implements ModelLoadingPlugin {
    private static final Int2ObjectMap<List<AffineTransformation>> TRANSFORMS = new Int2ObjectOpenHashMap<>() {{
        this.put(1, List.of(
          AffineTransformation.identity()
        ));
        this.put(2, List.of(
          new AffineTransformation(
            new Vector3f(0.15f, 0.f, 0.17f),
            new Quaternionf().rotationY(MathHelper.HALF_PI / 1.84f),
            new Vector3f(1.f),
            null
          ),
          new AffineTransformation(
            new Vector3f(-0.13f, 0.f, -0.15f),
            new Quaternionf().rotationY(MathHelper.HALF_PI / 2.25f),
            new Vector3f(1.f),
            null
          )
        ));
        this.put(3, List.of(
          new AffineTransformation(
            new Vector3f(0.18f, 0.f, 0.16f),
            new Quaternionf().rotationY(MathHelper.HALF_PI / 1.84f),
            new Vector3f(1.f),
            null
          ),
          new AffineTransformation(
            new Vector3f(0.0f, 0.f, -0.17f),
            new Quaternionf().rotationY(MathHelper.HALF_PI / 2.25f),
            new Vector3f(1.f),
            null
          ),
          new AffineTransformation(
            new Vector3f(-0.15f, 0.f, 0.10f),
            new Quaternionf().rotationY(MathHelper.HALF_PI / 2.05f),
            new Vector3f(1.f),
            null
          )
        ));
    }};

    @Override
    public void onInitializeModelLoader(Context ctx) {
        ctx.addModels();
        ctx.modifyModelAfterBake().register((model, context) -> {
            // don't override other mods models
            if (!context.id().getNamespace().equals(SpectrumPlaceableBottles.MOD_ID))
                return model;
            Set<Item> items = this.getItemsFrom(context.id());
            if (items.isEmpty())
                return model;
            HashMap<TriPlacementModel.ModelKey, BakedModel> models = this.bakeModels(context.baker(), items);
            return new TriPlacementModel(
              models,
              context.textureGetter().apply(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("spectrum", "item/infused_beverage")))
            );
        });
    }

    private HashMap<TriPlacementModel.ModelKey, BakedModel> bakeModels(Baker baker, Set<Item> items) {
        HashMap<TriPlacementModel.ModelKey, BakedModel> models = new HashMap<>(items.size());
        for (Item item : items) {
            Identifier id = Registries.ITEM.getId(item);
            for (int count = 1; count <= 3; count++) {
                for (int i = 0; i < count; i++) {
                    ModelIdentifier modelId = new ModelIdentifier(id, "spectrumplaceablebottles{index=" + i + ",count=" + count + "}");
                    this.setParents(modelId, baker);
                    BakedModel model = baker.bake(
                      modelId,
                      PlacementBakeSettings.with(TRANSFORMS.get(count).get(i))
                    );

                    if (model == baker.getOrLoadModel(ModelLoader.MISSING_ID))
                        SpectrumPlaceableBottles.LOGGER.warn("Failed to load model {}", modelId);

                    models.put(new TriPlacementModel.ModelKey(item, count, i), model);
                }
            }
        }
        return models;
    }

    private Set<Item> getItemsFrom(Identifier id) {
        if (id.getPath().startsWith("block/"))
            id = new Identifier(id.getNamespace(), id.getPath().substring(6));

        Block block = Registries.BLOCK.get(new Identifier(id.getNamespace(), id.getPath()));
        if (block instanceof TriPlacementBlock placementBlock)
            return placementBlock.getAcceptableItems();

        return Collections.emptySet();
    }

    private void setParents(ModelIdentifier modelId, Baker baker) {
        UnbakedModel model = baker.getOrLoadModel(modelId);
        if (model instanceof JsonUnbakedModel json) {
            json.setParents(baker::getOrLoadModel);
        }
    }

    protected static class PlacementBakeSettings implements ModelBakeSettings {
        private final AffineTransformation transform;

        protected PlacementBakeSettings(AffineTransformation transform) {
            this.transform = transform;
        }

        public static PlacementBakeSettings with(AffineTransformation transformation) {
            return new PlacementBakeSettings(transformation);
        }

        public static PlacementBakeSettings create() {
            return new PlacementBakeSettings(AffineTransformation.identity());
        }

        @Override
        public AffineTransformation getRotation() {
            return this.transform;
        }
    }
}

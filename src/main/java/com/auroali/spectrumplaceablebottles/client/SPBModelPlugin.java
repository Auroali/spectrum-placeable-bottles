package com.auroali.spectrumplaceablebottles.client;

import com.auroali.spectrumplaceablebottles.client.models.TriPlacementModel;
import com.auroali.spectrumplaceablebottles.client.models.UnbakedTriPlacementModel;
import com.auroali.spectrumplaceablebottles.common.blocks.AcceptableItemSet;
import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlocks;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
            new Quaternionf().rotationY(-MathHelper.HALF_PI / 2.25f),
            new Vector3f(1.f),
            null
          )
        ));
        this.put(3, List.of(
          new AffineTransformation(
            new Vector3f(0.20f, 0.f, 0.18f),
            new Quaternionf().rotationY(MathHelper.HALF_PI / 1.84f),
            new Vector3f(1.f),
            null
          ),
          new AffineTransformation(
            new Vector3f(0.0f, 0.f, -0.19f),
            new Quaternionf().rotationY(MathHelper.PI),
            new Vector3f(1.f),
            null
          ),
          new AffineTransformation(
            new Vector3f(-0.17f, 0.f, 0.12f),
            new Quaternionf().rotationY(-MathHelper.HALF_PI / 2.05f),
            new Vector3f(1.f),
            null
          )
        ));
    }};

    @Override
    public void onInitializeModelLoader(Context ctx) {
        for (Block block : SPBBlocks.ALL_PLACEABLES) {
            if (block instanceof TriPlacementBlock triPlacement) {
                ctx.registerBlockStateResolver(block, context -> {
                    AcceptableItemSet items = triPlacement.getAcceptableItems();
                    UnbakedTriPlacementModel model = new UnbakedTriPlacementModel(items);
                    for (BlockState state : block.getStateManager().getStates()) {
                        context.setModel(state, model);
                    }
                });
            }
        }
    }

    public static HashMap<TriPlacementModel.ModelKey, BakedModel> bakeModels(Baker baker, AcceptableItemSet items, ModelBakeSettings settings) {
        HashMap<TriPlacementModel.ModelKey, BakedModel> models = new HashMap<>(items.size());
        forEachPlaceableIdentifier(items, (item, id, count, i) -> {
            setParents(id, baker);
            BakedModel model = baker.bake(
              id,
              PlacementBakeSettings.with(TRANSFORMS.get(count).get(i).multiply(settings.getRotation()))
            );

            models.put(new TriPlacementModel.ModelKey(item, count, i), model);
        });
        return models;
    }

    public static void forEachPlaceableIdentifier(Collection<Item> items, IdConsumer consumer) {
        for (Item item : items) {
            Identifier id = Registries.ITEM.getId(item);
            for (int count = 1; count <= 3; count++) {
                for (int i = 0; i < count; i++) {
                    ModelIdentifier modelId = new ModelIdentifier(id, "spectrumplaceablebottles{index=" + i + ",count=" + count + "}");
                    consumer.accept(item, modelId, count, i);
                }
            }
        }
    }

    private static void setParents(ModelIdentifier modelId, Baker baker) {
        UnbakedModel model = baker.getOrLoadModel(modelId);
        if (model instanceof JsonUnbakedModel json) {
            json.setParents(baker::getOrLoadModel);
        }
    }

    @FunctionalInterface
    public interface IdConsumer {
        void accept(Item item, ModelIdentifier id, int count, int index);
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

package com.auroali.spectrumplaceablebottles.client.models;

import com.auroali.spectrumplaceablebottles.common.blockentities.TriPlacementBlockEntity;
import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Model that dynamically emits up to 3 other models when rendered, depending
 * on the corresponding {@link TriPlacementBlockEntity}'s contents
 */
public class TriPlacementModel implements FabricBakedModel, BakedModel {
    private final HashMap<ModelKey, BakedModel> models;
    private final Sprite particle;

    /**
     * Constructs a new TriPlacementModel
     *
     * @param models the item to model map
     * @param sprite the sprite to use for particles
     */
    public TriPlacementModel(HashMap<ModelKey, BakedModel> models, Sprite sprite) {
        this.models = models;
        this.particle = sprite;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (blockView.getBlockEntity(pos) instanceof TriPlacementBlockEntity entity) {
            int count = state.get(TriPlacementBlock.COUNT);
            for (int i = 0; i < entity.getItems().size(); i++) {
                ModelKey key = new ModelKey(entity.getItems().get(i).getItem(), count, i);
                BakedModel model = this.models.get(key);
                if (model == null)
                    continue;

                TriPlacementModelEmitter.emitQuads(model, i, state, randomSupplier, context);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        // no need to emit item quads, as this will never be in item form
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return this.particle;
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    public record ModelKey(Item item, int count, int i) {
        @Override
        public String toString() {
            return Registries.ITEM.getId(this.item).toString() + "[i=" + this.i + "]";
        }

        @Override
        public int hashCode() {
            return this.item.hashCode() + 31 * this.i + 7 * this.count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o instanceof ModelKey key)
                return this.item == key.item && this.count == key.count && this.i == key.i;
            return false;
        }
    }
}

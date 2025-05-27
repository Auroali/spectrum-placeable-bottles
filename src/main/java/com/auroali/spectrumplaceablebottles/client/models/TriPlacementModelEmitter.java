package com.auroali.spectrumplaceablebottles.client.models;

import com.auroali.spectrumplaceablebottles.client.colors.TriPlacementColorProvider;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.Supplier;

public class TriPlacementModelEmitter {
    public static final RenderMaterial MATERIAL = RendererAccess.INSTANCE
      .getRenderer()
      .materialById(RenderMaterial.MATERIAL_STANDARD);

    /**
     * Emits a model's quads, adjusting each quad's color index to
     * match up with {@link TriPlacementColorProvider}'s expected values
     *
     * @param model          the model to emit
     * @param index          the index we are currently rendering, for color index calculation
     * @param state          the block state being rendered
     * @param randomSupplier the random supplier
     * @param context        the render context
     * @see TriPlacementColorProvider
     */
    public static void emitQuads(BakedModel model, int index, BlockState state, Supplier<Random> randomSupplier, RenderContext context) {
        final QuadEmitter emitter = context.getEmitter();

        for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
            Direction direction = ModelHelper.faceFromIndex(i);

            if (!context.hasTransform() && context.isFaceCulled(direction))
                continue;

            List<BakedQuad> quads = model.getQuads(state, direction, randomSupplier.get());

            for (BakedQuad quad : quads) {
                emitter.fromVanilla(quad, MATERIAL, direction);
                if (quad.getColorIndex() != -1)
                    emitter.colorIndex(index * TriPlacementColorProvider.TINT_BLOCK_SIZE + quad.getColorIndex());
                emitter.emit();
            }
        }
    }
}

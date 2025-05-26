package com.auroali.spectrumplaceablebottles.client.colors;

import com.auroali.spectrumplaceablebottles.common.blockentities.TriPlacementBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Converts a given tint index to the expected color based off the corresponding item
 * <br>
 * For example, if a {@link TriPlacementBlockEntity} contained 3 items, tints 0 -> 31 would correspond to
 * tints 0 -> 31 on the first item, 32 -> 63 would correspond to 0 -> 31 on the second item, and 64 -> 95
 * would correspond to 0 -> 31 on the last item
 * <br>
 * The width of each range is referred to as the tint block size, and is controlled by {@link TriPlacementColorProvider#TINT_BLOCK_SIZE}
 */
public class TriPlacementColorProvider implements BlockColorProvider {
    public static final TriPlacementColorProvider PROVIDER = new TriPlacementColorProvider();
    // the range of tint indexes each item can use
    // any tint index on the model outside the range of [0, TINT_BLOCK_SIZE) will not work properly
    public static final int TINT_BLOCK_SIZE = 32;

    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null && pos != null && world.getBlockEntity(pos) instanceof TriPlacementBlockEntity entity) {
            List<ItemStack> items = entity.getItems();
            int index = this.itemIndexFromTint(tintIndex);
            // if the item is not contained within the block entity, do nothing
            if (index >= items.size() || index < 0)
                return -1;

            // return the item's tint for the corresponding index
            ItemStack stack = items.get(index);
            ItemColorProvider provider = ColorProviderRegistry.ITEM.get(stack.getItem());
            return provider != null ? provider.getColor(stack, this.tintIndexFromTint(tintIndex)) : -1;
        }
        return -1;
    }

    private int itemIndexFromTint(int tintIndex) {
        return tintIndex / TINT_BLOCK_SIZE;
    }

    private int tintIndexFromTint(int tintIndex) {
        return tintIndex % TINT_BLOCK_SIZE;
    }
}

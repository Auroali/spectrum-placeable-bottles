package com.auroali.spectrumplaceablebottles.common.blockentities;

import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TriPlacementBlockEntity extends BlockEntity implements Clearable {
    private final List<ItemStack> items;

    public TriPlacementBlockEntity(BlockPos pos, BlockState state) {
        super(SPBBlockEntities.TRI_PLACEMENT_ENTITY, pos, state);
        this.items = new ArrayList<>();
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public ItemStack pop() {
        if (this.items.isEmpty())
            return ItemStack.EMPTY;
        ItemStack stack = this.items.remove(this.items.size() - 1);
        this.updateState(this.items.size());
        this.update();
        return stack;
    }

    public boolean push(ItemStack stack) {
        if (this.items.size() >= 3)
            return false;
        this.items.add(stack);
        this.updateState(this.items.size());
        this.update();
        return true;
    }

    protected void updateState(int newcount) {
        if (this.getCachedState().get(TriPlacementBlock.COUNT) == newcount)
            return;
        if (this.getWorld() != null) {
            this.getWorld().setBlockState(
              this.getPos(),
              this.getCachedState()
                .with(TriPlacementBlock.COUNT, MathHelper.clamp(newcount, 1, 3))
            );
        }
    }

    protected void update() {
        this.markDirty();
        if (this.getWorld() != null) {
            this.getWorld().updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        NbtList itemTag = new NbtList();
        for (ItemStack stack : this.items) {
            itemTag.add(stack.writeNbt(new NbtCompound()));
        }
        nbt.put("Items", itemTag);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        NbtList itemTag = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        this.items.clear();
        for (int i = 0; i < itemTag.size(); i++) {
            ItemStack stack = ItemStack.fromNbt(itemTag.getCompound(i));
            this.items.add(stack);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        this.writeNbt(tag);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void clear() {
        this.items.clear();
        this.updateState(1);
        this.update();
    }
}

package com.auroali.spectrumplaceablebottles.common.blocks;

import com.auroali.spectrumplaceablebottles.common.blockentities.TriPlacementBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class TriPlacementBlock extends BlockWithEntity implements Waterloggable {
    public static final IntProperty COUNT = IntProperty.of("count", 1, 3);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final AcceptableItemSet accepts;
    private final VoxelShape[] shapes;

    public TriPlacementBlock(Settings settings, AcceptableItemSet accepts, VoxelShape... shapes) {
        super(settings);
        this.setDefaultState(this.getStateManager()
          .getDefaultState()
          .with(COUNT, 1)
          .with(WATERLOGGED, false)
        );
        this.accepts = accepts;
        this.shapes = shapes;
        if (shapes.length != 3) {
            throw new IllegalArgumentException("Expected 3 shapes, got " + this.shapes.length);
        }
    }

    public static ActionResult place(PlayerEntity playerEntity, World world, Hand hand, ItemStack stack, BlockHitResult result, Block block) {
        ItemPlacementContext context = new ItemPlacementContext(world, playerEntity, hand, stack, result);
        if (!context.canPlace())
            return ActionResult.PASS;

        BlockState state = block.getPlacementState(context);
        if (state == null)
            return ActionResult.FAIL;

        world.setBlockState(context.getBlockPos(), state, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
        BlockSoundGroup group = block.getSoundGroup(state);
        world.playSound(
          playerEntity,
          context.getBlockPos(),
          group.getPlaceSound(),
          SoundCategory.BLOCKS,
          (group.getVolume() + 1.0F) / 2.0F,
          group.getPitch() * 0.8F
        );
        world.emitGameEvent(GameEvent.BLOCK_PLACE, context.getBlockPos(), GameEvent.Emitter.of(playerEntity, state));
        ItemStack toInsert = playerEntity != null && playerEntity.getAbilities().creativeMode
          ? stack.copy().split(1)
          : stack.split(1);
        if (world.getBlockEntity(context.getBlockPos()) instanceof TriPlacementBlockEntity entity) {
            entity.push(toInsert);
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!this.accepts.contains(stack.getItem()))
            return this.tryEmpty(world, pos, player, hand);

        return this.tryFill(player, stack, world, state, pos);
    }

    private ActionResult tryFill(PlayerEntity player, ItemStack stack, World world, BlockState state, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof TriPlacementBlockEntity entity) {
            ItemStack split = player.getAbilities().creativeMode
              ? stack.copy().split(1)
              : stack.split(1);
            if (entity.push(split)) {
                world.playSound(
                  player,
                  pos,
                  this.getSoundGroup(state).getPlaceSound(),
                  SoundCategory.BLOCKS,
                  (this.getSoundGroup(state).getVolume() + 1.0F) / 2.0F,
                  this.getSoundGroup(state).getPitch() * 0.8F
                );
                return ActionResult.success(world.isClient);
            }

            stack.increment(1);
            return ActionResult.FAIL;
        }
        return ActionResult.FAIL;
    }

    private ActionResult tryEmpty(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (world.getBlockEntity(pos) instanceof TriPlacementBlockEntity entity) {
            ItemStack stack = entity.pop();
            if (stack.isEmpty())
                return ActionResult.FAIL;

            if (entity.getItems().isEmpty())
                world.removeBlock(pos, false);

            if (player.getStackInHand(hand).isEmpty()) {
                player.setStackInHand(hand, stack);
                player.getWorld().playSound(
                  player,
                  player.getX(), player.getY(), player.getZ(),
                  SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                  0.2f,
                  (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 1.4F + 2.0F
                );
                return ActionResult.success(world.isClient);
            }

            if (!player.getInventory().insertStack(stack))
                player.dropItem(stack, false, false);

            player.getWorld().playSound(
              player,
              player.getX(), player.getY(), player.getZ(),
              SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
              0.2f,
              (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 1.4F + 2.0F
            );
            return ActionResult.success(world.isClient);
        }
        return ActionResult.FAIL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(COUNT);
        builder.add(WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapes[state.get(COUNT) - 1];
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TriPlacementBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public AcceptableItemSet getAcceptableItems() {
        return this.accepts;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state != null && ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER))
            state = state.with(WATERLOGGED, true);
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED))
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof TriPlacementBlockEntity entity) {
                ItemScatterer.spawn(
                  world,
                  pos,
                  DefaultedList.copyOf(ItemStack.EMPTY, entity.getItems().toArray(ItemStack[]::new))
                );
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}

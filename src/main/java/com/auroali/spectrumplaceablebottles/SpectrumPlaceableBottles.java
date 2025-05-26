package com.auroali.spectrumplaceablebottles;

import com.auroali.spectrumplaceablebottles.common.blocks.TriPlacementBlock;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlockEntities;
import com.auroali.spectrumplaceablebottles.common.registry.SPBBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpectrumPlaceableBottles implements ModInitializer {
    public static final String MOD_ID = "spectrumplaceablebottles";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        SPBBlocks.register();
        SPBBlockEntities.register();

        UseBlockCallback.EVENT.register((playerEntity, world, hand, hitResult) -> {
            ItemStack stack = playerEntity.getStackInHand(hand);
            if (!playerEntity.isSneaking())
                return ActionResult.PASS;
            for (Block block : SPBBlocks.ALL_PLACEABLES) {
                if (block instanceof TriPlacementBlock triPlacement && triPlacement.getAcceptableItems().contains(stack)) {
                    return TriPlacementBlock.place(playerEntity, world, hand, stack, hitResult, block);
                }
            }
            return ActionResult.PASS;
        });

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container ->
          ResourceManagerHelper.registerBuiltinResourcePack(
            id("3d_items"),
            container,
            Text.of("3D Items"),
            ResourcePackActivationType.NORMAL
          )
        );
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}
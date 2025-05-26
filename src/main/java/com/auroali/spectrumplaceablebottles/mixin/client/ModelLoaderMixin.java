package com.auroali.spectrumplaceablebottles.mixin.client;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract JsonUnbakedModel loadModelFromJson(Identifier id) throws IOException;

    @Shadow
    protected abstract void putModel(Identifier id, UnbakedModel unbakedModel);

    @Shadow
    @Final
    private Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow
    public abstract UnbakedModel getOrLoadModel(Identifier id);

    @Inject(method = "loadModel", at = @At(value = "HEAD"), cancellable = true)
    public void spectrumplaceablebottles$loadModelsProperly(Identifier id, CallbackInfo ci) throws Exception {
        if (id instanceof ModelIdentifier modelIdentifier && modelIdentifier.getVariant().startsWith("spectrumplaceablebottles{")) {
            Identifier modelPath = id.withPrefixedPath("placeablebottles/");
            JsonUnbakedModel jsonUnbakedModel = this.loadModelFromJson(modelPath);
            this.putModel(modelIdentifier, jsonUnbakedModel);
            this.unbakedModels.put(modelPath, jsonUnbakedModel);
            ci.cancel();
        }
    }
}

package me.znotchill.marmot.client.mixins;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import kotlin.Pair;
import me.znotchill.marmot.client.shaders.ShaderManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public abstract class ClientRenderIndexMixin {
    @Inject(method = "bindDefaultUniforms", at = @At("HEAD"))
    private static void bindDefaultUniforms(RenderPass pass, CallbackInfo ci) {
        ShaderManager.INSTANCE.createBuffers();
        for (Pair<@NotNull String, @NotNull GpuBufferSlice> stringGpuBufferSlicePair : ShaderManager.INSTANCE.getAllSlicesList()) {
            pass.setUniform(stringGpuBufferSlicePair.component1(), stringGpuBufferSlicePair.component2());
        }
    }
}
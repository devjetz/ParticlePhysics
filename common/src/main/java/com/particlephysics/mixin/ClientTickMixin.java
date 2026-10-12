package com.particlephysics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import com.particlephysics.keybind.ConfigKeyBinding;
import com.particlephysics.client.ParticlePhysicsConfigScreen;

@Mixin(Minecraft.class)
public class ClientTickMixin {
    
    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        Minecraft client = (Minecraft) (Object) this;
        while (ConfigKeyBinding.OPEN_CONFIG.consumeClick()) {
            client.setScreenAndShow(new ParticlePhysicsConfigScreen(null));
        }
    }
}

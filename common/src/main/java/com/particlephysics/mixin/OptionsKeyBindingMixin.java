package com.particlephysics.mixin;

import com.particlephysics.keybind.ConfigKeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsKeyBindingMixin {
    @Shadow
    @Final
    @Mutable
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void particlephysics$registerConfigKey(CallbackInfo ci) {
        KeyMapping[] extended = new KeyMapping[this.keyMappings.length + 1];
        System.arraycopy(this.keyMappings, 0, extended, 0, this.keyMappings.length);
        extended[this.keyMappings.length] = ConfigKeyBinding.OPEN_CONFIG;
        this.keyMappings = extended;
    }
}

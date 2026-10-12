package com.particlephysics.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class ConfigKeyBinding {
    public static final KeyMapping OPEN_CONFIG = new KeyMapping(
        "key.particlephysics.open_config",
        InputConstants.KEY_P,
        KeyMapping.Category.register(Identifier.fromNamespaceAndPath("particlephysics", "main"))
    );

    private ConfigKeyBinding() {
    }
}

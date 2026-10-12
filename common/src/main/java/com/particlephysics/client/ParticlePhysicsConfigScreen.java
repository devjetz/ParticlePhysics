package com.particlephysics.client;

import com.particlephysics.config.ParticlePhysicsConfig;
import java.util.Locale;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ParticlePhysicsConfigScreen extends Screen {
    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 20;
    private final Screen parent;
    private final ParticlePhysicsConfig config;

    public ParticlePhysicsConfigScreen(Screen parent) {
        super(Component.translatable("screen.particlephysics.title"));
        this.parent = parent;
        this.config = ParticlePhysicsConfig.getInstance();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int wideButtonX = centerX - BUTTON_WIDTH / 2;

        this.addRenderableWidget(Button.builder(
            toggleLabel("screen.particlephysics.master", this.config.enablePhysics),
            button -> {
                this.config.enablePhysics = !this.config.enablePhysics;
                this.saveAndRebuild();
            }
        ).bounds(wideButtonX, 32, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.addRenderableWidget(Button.builder(
            toggleLabel("screen.particlephysics.leaves", this.config.enableLeafPhysics),
            button -> {
                this.config.enableLeafPhysics = !this.config.enableLeafPhysics;
                this.saveAndRebuild();
            }
        ).bounds(wideButtonX, 57, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.addRenderableWidget(Button.builder(
            toggleLabel("screen.particlephysics.bubbles", this.config.enableBubblePhysics),
            button -> {
                this.config.enableBubblePhysics = !this.config.enableBubblePhysics;
                this.saveAndRebuild();
            }
        ).bounds(wideButtonX, 82, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.addRenderableWidget(Button.builder(
            toggleLabel("screen.particlephysics.ground_wind", this.config.windAffectsGroundedLeaves),
            button -> {
                this.config.windAffectsGroundedLeaves = !this.config.windAffectsGroundedLeaves;
                this.saveAndRebuild();
            }
        ).bounds(wideButtonX, 107, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        this.addAdjustmentRow(
            centerX, 137,
            "screen.particlephysics.wind",
            Component.literal(String.format(Locale.ROOT, "%.1f", this.config.windMultiplier)),
            () -> this.config.windMultiplier = Math.max(0.0, this.config.windMultiplier - 0.1),
            () -> this.config.windMultiplier = Math.min(2.0, this.config.windMultiplier + 0.1)
        );

        this.addAdjustmentRow(
            centerX, 167,
            "screen.particlephysics.decay",
            Component.translatable("screen.particlephysics.seconds", this.config.leafDecaySeconds),
            () -> this.config.leafDecaySeconds = Math.max(1, this.config.leafDecaySeconds - 10),
            () -> this.config.leafDecaySeconds = Math.min(3600, this.config.leafDecaySeconds + 10)
        );

        this.addRenderableWidget(Button.builder(
            Component.translatable("gui.done"),
            button -> this.onClose()
        ).bounds(centerX - 100, this.height - 28, 200, BUTTON_HEIGHT).build());
    }

    private void addAdjustmentRow(
        int centerX,
        int y,
        String labelKey,
        Component value,
        Runnable decrease,
        Runnable increase
    ) {
        this.addRenderableWidget(Button.builder(
            Component.literal("-"),
            button -> {
                decrease.run();
                this.saveAndRebuild();
            }
        ).bounds(centerX - 104, y, 36, BUTTON_HEIGHT).build());

        this.addRenderableWidget(Button.builder(
            Component.translatable(labelKey, value),
            button -> {
            }
        ).bounds(centerX - 64, y, 128, BUTTON_HEIGHT).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("+"),
            button -> {
                increase.run();
                this.saveAndRebuild();
            }
        ).bounds(centerX + 68, y, 36, BUTTON_HEIGHT).build());
    }

    private static Component toggleLabel(String labelKey, boolean enabled) {
        return Component.translatable(
            "screen.particlephysics.toggle",
            Component.translatable(labelKey),
            Component.translatable(enabled ? "screen.particlephysics.on" : "screen.particlephysics.off")
        );
    }

    private void saveAndRebuild() {
        this.config.save();
        this.rebuildWidgets();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }
}

package com.particlephysics.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.particlephysics.config.ParticlePhysicsConfig;

@Mixin(BubbleParticle.class)
abstract class BubbleParticleMixin {
    @Unique private int particlephysics$phase;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void particlephysics$init(CallbackInfo ci) {
        this.particlephysics$phase = (int) (Math.random() * 360);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void particlephysics$tick(CallbackInfo callback) {
        ParticlePhysicsConfig config = ParticlePhysicsConfig.getInstance();
        
        if (!config.enablePhysics || !config.enableBubblePhysics) {
            callback.cancel();
            return;
        }
        
        ParticlePhysicsParticleAccess particle = (ParticlePhysicsParticleAccess) this;
        ClientLevel level = particle.particlephysics$getLevel();
        double x = particle.particlephysics$getX();
        double y = particle.particlephysics$getY();
        double z = particle.particlephysics$getZ();
        int age = particle.particlephysics$getAge() + 1;
        particle.particlephysics$setAge(age);

        particle.particlephysics$setXo(x);
        particle.particlephysics$setYo(y);
        particle.particlephysics$setZo(z);

        if (age >= particle.particlephysics$getLifetime()) {
            particle.particlephysics$remove();
            callback.cancel();
            return;
        }

        BlockPos bubblePos = BlockPos.containing(x, y, z);
        if (!this.particlephysics$isUnderwater(level, bubblePos)) {
            particle.particlephysics$remove();
            callback.cancel();
            return;
        }

        double xd = particle.particlephysics$getXd();
        double yd = particle.particlephysics$getYd();
        double zd = particle.particlephysics$getZd();

        this.particlephysics$phase = (this.particlephysics$phase + 6) % 360;
        double swirl = this.particlephysics$phase * Math.PI / 180.0;
        double swirlRadius = 0.0045;

        double swirledXd = xd + Math.cos(swirl) * swirlRadius;
        double swirledZd = zd + Math.sin(swirl * 1.45) * swirlRadius;

        particle.particlephysics$setXd(swirledXd * 0.98);
        particle.particlephysics$setYd(Math.min(0.06, yd + 0.0035));
        particle.particlephysics$setZd(swirledZd * 0.98);

        particle.particlephysics$move(swirledXd * 0.98, Math.min(0.06, yd + 0.0035) * 0.985, swirledZd * 0.98);

        callback.cancel();
    }

    @Unique
    private boolean particlephysics$isUnderwater(ClientLevel level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER);
    }
}


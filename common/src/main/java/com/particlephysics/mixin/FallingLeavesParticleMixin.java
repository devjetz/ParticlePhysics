package com.particlephysics.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.particlephysics.config.ParticlePhysicsConfig;

@Mixin(FallingLeavesParticle.class)
abstract class FallingLeavesParticleMixin {
    @Unique private boolean particlephysics$floating;
    @Unique private boolean particlephysics$grounded;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void particlephysics$tick(CallbackInfo callback) {
        ParticlePhysicsConfig config = ParticlePhysicsConfig.getInstance();
        
        if (!config.enablePhysics || !config.enableLeafPhysics) {
            return;
        }
        
        ParticlePhysicsParticleAccess particle = (ParticlePhysicsParticleAccess) this;
        ClientLevel level = particle.particlephysics$getLevel();
        double x = particle.particlephysics$getX();
        double y = particle.particlephysics$getY();
        double z = particle.particlephysics$getZ();
        int decayTicks = Math.max(1, Math.min(3600, config.leafDecaySeconds)) * 20;
        particle.particlephysics$setLifetime(decayTicks);
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

        if (this.particlephysics$floating) {
            BlockPos waterPos = this.particlephysics$waterBlockAt(level, x, y, z);
            if (waterPos != null) {
                double xd = particle.particlephysics$getXd();
                double zd = particle.particlephysics$getZd();
                double phase = age * 0.045 + x * 1.7 + z * 2.3;
                double gust = Math.sin(age * 0.031 + x * 0.7 - z * 0.5);
                double driftX = xd * 0.96
                    + (Math.cos(phase) * 0.00035 + gust * 0.0002) * config.windMultiplier;
                double driftZ = zd * 0.96
                    + (Math.sin(phase * 0.83) * 0.00035 - gust * 0.0002) * config.windMultiplier;
                double nextX = x + driftX;
                double nextZ = z + driftZ;
                BlockPos nextWaterPos = this.particlephysics$waterBlockAt(level, nextX, y, nextZ);
                if (nextWaterPos != null) {
                    particle.particlephysics$setXd(driftX);
                    particle.particlephysics$setZd(driftZ);
                    particle.particlephysics$setYd(0.0);
                    particle.particlephysics$setOnGround(false);
                    particle.particlephysics$setStoppedByCollision(false);
                    particle.particlephysics$setPos(
                        nextX,
                        nextWaterPos.getY()
                            + level.getFluidState(nextWaterPos).getHeight(level, nextWaterPos)
                            + 0.012,
                        nextZ
                    );
                    callback.cancel();
                    return;
                }

                particle.particlephysics$setXd(0.0);
                particle.particlephysics$setYd(0.0);
                particle.particlephysics$setZd(0.0);
                particle.particlephysics$setPos(
                    x,
                    waterPos.getY() + level.getFluidState(waterPos).getHeight(level, waterPos) + 0.012,
                    z
                );
                callback.cancel();
                return;
            }

            this.particlephysics$floating = false;
            particle.particlephysics$setStoppedByCollision(false);
        }

        BlockPos waterPos = this.particlephysics$waterBlockAt(level, x, y, z);
        if (waterPos != null) {
            FluidState fluid = level.getFluidState(waterPos);
            double surface = waterPos.getY() + fluid.getHeight(level, waterPos);
            if (y <= surface + 0.15) {
                this.particlephysics$floating = true;
                particle.particlephysics$setXd(particle.particlephysics$getXd() * 0.18);
                particle.particlephysics$setYd(0.0);
                particle.particlephysics$setZd(particle.particlephysics$getZd() * 0.18);
                particle.particlephysics$setOnGround(false);
                particle.particlephysics$setStoppedByCollision(false);
                particle.particlephysics$setPos(x, surface + 0.012, z);
                callback.cancel();
                return;
            }
        }

        double xd = particle.particlephysics$getXd();
        double yd = particle.particlephysics$getYd();
        double zd = particle.particlephysics$getZd();
        double phase = age * 0.067 + x * 1.9 + z * 1.3;
        double gust = Math.sin(age * 0.031 + x * 0.7 - z * 0.5);

        if (this.particlephysics$grounded && !config.windAffectsGroundedLeaves) {
            particle.particlephysics$setXd(0.0);
            particle.particlephysics$setYd(0.0);
            particle.particlephysics$setZd(0.0);
            particle.particlephysics$setStoppedByCollision(false);
            particle.particlephysics$setOnGround(false);
            particle.particlephysics$move(0.0, -0.02, 0.0);
            this.particlephysics$grounded = Math.abs(particle.particlephysics$getY() - y) < 0.019;
            particle.particlephysics$setStoppedByCollision(false);
            particle.particlephysics$setOnGround(false);
            callback.cancel();
            return;
        }

        double windForce = config.windMultiplier;
        xd += (Math.cos(phase) * 0.0015 + gust * 0.0012) * windForce;
        zd += (Math.sin(phase * 0.79) * 0.0015 - gust * 0.0012) * windForce;

        if (this.particlephysics$grounded) {
            xd *= 0.9;
            zd *= 0.9;
            yd = -0.02;
        } else {
            yd = Math.max(yd - 0.002, -0.012);
        }
        xd = Math.max(-0.045, Math.min(0.045, xd));
        zd = Math.max(-0.045, Math.min(0.045, zd));
        particle.particlephysics$setXd(xd);
        particle.particlephysics$setYd(yd);
        particle.particlephysics$setZd(zd);

        particle.particlephysics$setStoppedByCollision(false);
        particle.particlephysics$setOnGround(false);
        particle.particlephysics$move(xd, yd, zd);
        double movedX = particle.particlephysics$getX() - x;
        double movedY = particle.particlephysics$getY() - y;
        double movedZ = particle.particlephysics$getZ() - z;
        particle.particlephysics$setXd(
            Math.abs(movedX - xd) > 1.0E-6
                ? -xd * 0.55
                : particle.particlephysics$getXd() * 0.992
        );
        boolean blockedBelow = yd < 0.0 && Math.abs(movedY - yd) > 1.0E-6;
        this.particlephysics$grounded = blockedBelow;
        particle.particlephysics$setYd(
            blockedBelow ? 0.0
                : Math.abs(movedY - yd) > 1.0E-6 ? -yd * 0.55
                : particle.particlephysics$getYd() * 0.996
        );
        particle.particlephysics$setZd(
            Math.abs(movedZ - zd) > 1.0E-6
                ? -zd * 0.55
                : particle.particlephysics$getZd() * 0.992
        );

        particle.particlephysics$setOnGround(false);
        particle.particlephysics$setStoppedByCollision(false);

        callback.cancel();
    }

    @Unique
    private BlockPos particlephysics$waterBlockAt(ClientLevel level, double x, double y, double z) {
        BlockPos pos = BlockPos.containing(x, y, z);
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            return pos;
        }

        BlockPos below = pos.below();
        return level.getFluidState(below).is(FluidTags.WATER) ? below : null;
    }
}

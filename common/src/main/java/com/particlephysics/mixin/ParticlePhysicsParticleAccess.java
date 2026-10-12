package com.particlephysics.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.particle.Particle;

@Mixin(Particle.class)
public interface ParticlePhysicsParticleAccess {
    @Accessor("level")
    ClientLevel particlephysics$getLevel();

    @Accessor("x")
    double particlephysics$getX();

    @Accessor("y")
    double particlephysics$getY();

    @Accessor("z")
    double particlephysics$getZ();

    @Accessor("x")
    void particlephysics$setX(double value);

    @Accessor("y")
    void particlephysics$setY(double value);

    @Accessor("z")
    void particlephysics$setZ(double value);

    @Accessor("xo")
    void particlephysics$setXo(double value);

    @Accessor("yo")
    void particlephysics$setYo(double value);

    @Accessor("zo")
    void particlephysics$setZo(double value);

    @Accessor("xd")
    double particlephysics$getXd();

    @Accessor("yd")
    double particlephysics$getYd();

    @Accessor("zd")
    double particlephysics$getZd();

    @Accessor("xd")
    void particlephysics$setXd(double value);

    @Accessor("yd")
    void particlephysics$setYd(double value);

    @Accessor("zd")
    void particlephysics$setZd(double value);

    @Accessor("age")
    int particlephysics$getAge();

    @Accessor("age")
    void particlephysics$setAge(int value);

    @Accessor("lifetime")
    int particlephysics$getLifetime();

    @Accessor("onGround")
    boolean particlephysics$isOnGround();

    @Accessor("onGround")
    void particlephysics$setOnGround(boolean value);

    @Accessor("stoppedByCollision")
    void particlephysics$setStoppedByCollision(boolean value);

    @Accessor("lifetime")
    void particlephysics$setLifetime(int value);

    @Invoker("remove")
    void particlephysics$remove();

    @Invoker("move")
    void particlephysics$move(double xd, double yd, double zd);

    @Invoker("setPos")
    void particlephysics$setPos(double x, double y, double z);
}

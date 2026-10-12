# Particle Physics - Code Reference

## Key Physics Formulas

### Wind and Gust Calculation
```java
double phase = age * 0.067 + x * 1.9 + z * 1.3;
double gust = Math.sin(age * 0.031 + x * 0.7 - z * 0.5);
xd += Math.cos(phase) * 0.0015 + gust * 0.0012;
zd += Math.sin(phase * 0.79) * 0.0015 - gust * 0.0012;
```

**Why this works:**
- `age * rate` creates time-based oscillation
- `x * influence + z * influence` creates spatial variation
- Sine/cosine waves create smooth, natural motion
- Multiple frequencies (0.79x phase) create asymmetry

### Velocity Clamping
```java
xd = Math.max(-0.045, Math.min(0.045, xd));
zd = Math.max(-0.045, Math.min(0.045, zd));
```

**Purpose:** Prevents particles from moving too fast while still allowing gradual acceleration

### Friction/Drag System
```java
particle.particlephysics$setXd(particle.particlephysics$getXd() * 0.992);
particle.particlephysics$setYd(particle.particlephysics$getYd() * 0.996);
particle.particlephysics$setZd(particle.particlephysics$getZd() * 0.992);
```

**Effect:** 
- Each tick, velocity is reduced to 99.2-99.6% of previous
- Creates natural deceleration
- Different values for X/Y/Z allow asymmetric drag

### Gravity Application
```java
yd = Math.max(yd - particle.particlephysics$getGravity() - 0.00012, -0.055);
```

**Components:**
- `yd - particle.particlephysics$getGravity()` - apply base gravity
- `- 0.00012` - additional downward force for realistic fall
- `Math.max(..., -0.055)` - terminal velocity cap at -0.055 units/tick

## Leaf Physics - Wind Lift System

### Detection
```java
if (this.particlephysics$restTicks > 0) {
    if (this.particlephysics$floating) {
        // Handle water floating...
    } else if (this.particlephysics$onGround) {
        double gust = Math.sin(age * 0.031 + x * 0.7 - z * 0.5);
        double heavyWindForce = Math.sin(age * 0.015) * 0.8 + gust * 1.2;
        if (heavyWindForce > 0.6) {
            this.particlephysics$onGround = false;
            particle.particlephysics$setYd(0.015 + Math.random() * 0.01);
        }
    }
}
```

**Wind Force Formula:**
- Base wind: `Math.sin(age * 0.015) * 0.8` (varies from -0.8 to 0.8)
- Gust component: `gust * 1.2` (amplifies the gust)
- Threshold: `> 0.6` means strong winds needed (0.6/2.0 = 30% of max)

### Lift Velocity
```java
particle.particlephysics$setYd(0.015 + Math.random() * 0.01);
```

- Base lift: 0.015 units/tick
- Randomness: 0 to 0.01 additional (makes each leaf slightly different)
- Creates natural variation in lift-off behavior

## Bubble Physics - Underwater Motion

### Water Detection
```java
private boolean particlephysics$isInWater(ClientLevel level, double x, double y, double z) {
    BlockPos pos = BlockPos.containing(x, y, z);
    return level.getFluidState(pos).is(FluidTags.WATER);
}
```

**Why this matters:**
- Checks if bubble is actually in water
- Removes bubble immediately if it leaves water
- Prevents physics from applying outside water

### Swirling Motion
```java
double phase = age * 0.035 + x * 2.1 + z * 1.8;
double drift = Math.sin(age * 0.025 + x * 0.4 - z * 0.6);

xd += Math.cos(phase) * 0.00045 + drift * 0.00035;
zd += Math.sin(phase * 0.73) * 0.00045 - drift * 0.00035;
yd = 0.006 + Math.sin(age * 0.045 + phase) * 0.0015;
```

**Phase Rates:**
- X/Z phase rate: 0.035 (slower than leaves)
- Drift rate: 0.025 (another oscillation)
- Y undulation: 0.045 (faster oscillation in vertical)

**Result:**
- Bubbles spiral upward
- Horizontal movement is gentle (0.00045 vs 0.0015 for leaves)
- Vertical motion includes oscillation for bobbing effect

### Motion Parameters
```java
xd = Math.max(-0.02, Math.min(0.02, xd));    // Tighter speed limit
zd = Math.max(-0.02, Math.min(0.02, zd));

particle.particlephysics$setXd(particle.particlephysics$getXd() * 0.988);  // More friction
particle.particlephysics$setYd(particle.particlephysics$getYd() * 0.985);
particle.particlephysics$setZd(particle.particlephysics$getZd() * 0.988);
```

**Why different from leaves:**
- Speed limit is tighter (±0.02 vs ±0.045) - bubbles are light
- More friction (0.988 vs 0.992) - water resistance
- Vertical friction slightly different (0.985) for buoyancy feel

## Tuning Parameters Explained

### For Horizontal Movement Speed
```
0.0015  = Base coefficient
0.0012  = Gust coefficient  
0.045   = Speed limit
```

**Adjustment guide:**
- Increase coefficient → faster swirling
- Increase speed limit → allows higher velocity
- Increase gust coefficient → wind has more effect

### For Vertical Movement
```
-0.055  = Terminal velocity
0.00012 = Extra gravity
```

**Adjustment guide:**
- More negative terminal velocity → falls faster
- Increase extra gravity → accelerates downward

### For Oscillation Frequency
```
age * 0.067  = 0.067 oscillations per tick (≈15 ticks per cycle)
age * 0.045  = 0.045 oscillations per tick (≈22 ticks per cycle)
```

**Adjustment guide:**
- Increase multiplier → faster oscillation (more frequent changes)
- Decrease multiplier → slower oscillation (smoother waves)

## State Variables

### Leaves
```java
@Unique private int particlephysics$restTicks;      // Countdown timer for rest state
@Unique private boolean particlephysics$floating;   // Is leaf on water?
@Unique private boolean particlephysics$onGround;   // Is leaf on ground?
```

**State Machine:**
1. `!floating && !onGround` - Falling with physics
2. `floating && !onGround` - Floating on water
3. `!floating && onGround` - Resting on ground
4. `restTicks > 0` - In rest state (countdown)

### Bubbles
No state needed - bubbles don't have multiple behaviors, just underwater physics + lifetime check

## Common Patterns

### Cancel Vanilla Tick
```java
@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
private void particlephysics$tick(CallbackInfo callback) {
    // ... custom physics code ...
    callback.cancel();  // Prevent vanilla tick code from running
}
```

### Accessor Pattern (for private fields)
```java
ParticlePhysicsParticleAccess particle = (ParticlePhysicsParticleAccess) this;
double x = particle.particlephysics$getX();
particle.particlephysics$setXd(newValue);
```

**Why:** Minecraft obfuscates field names, so we need mixins to access private fields

### Early Exit for Lifetime
```java
if (age >= particle.particlephysics$getLifetime()) {
    particle.particlephysics$remove();
    callback.cancel();
    return;
}
```

**Purpose:** Check if particle should die, and exit if so

### Position Update
```java
particle.particlephysics$move(xd, yd, zd);  // Apply velocity
particle.particlephysics$setXd(xd * 0.992); // Apply friction
```

**Order matters:** Move first, then apply friction to velocity for next tick

## Performance Optimization

### Math Operations per Tick
- Sine functions: 2-3 (expensive)
- Cosine functions: 1-2 (expensive)
- Multiplication/division: ~20
- Comparisons: ~5

**Result:** ~500 operations per 100 particles = negligible performance impact

### Memory Usage
- Per-particle overhead: 3 integers/booleans (~12 bytes)
- No list/array allocations
- No object creation per tick
- Total mod overhead: <5KB

### Tick Distribution
- Most particles only need 1 tick for physics
- Complex states (leaf on ground) use early exit to skip expensive code
- Water checks cached in current position

## Extending to New Particles

### Template for New Mixin
```java
@Mixin(ParticleType.class)
abstract class NewParticleMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void particlephysics$tick(CallbackInfo callback) {
        ParticlePhysicsParticleAccess particle = (ParticlePhysicsParticleAccess) this;
        
        // 1. Get position and age
        double x = particle.particlephysics$getX();
        int age = particle.particlephysics$getAge() + 1;
        particle.particlephysics$setAge(age);
        
        // 2. Update previous position
        particle.particlephysics$setXo(x);
        
        // 3. Check lifetime
        if (age >= particle.particlephysics$getLifetime()) {
            particle.particlephysics$remove();
            callback.cancel();
            return;
        }
        
        // 4. Calculate physics
        double xd = particle.particlephysics$getXd();
        double phase = age * PHASE_RATE + x * X_INFLUENCE;
        xd += Math.cos(phase) * FORCE_COEFFICIENT;
        
        // 5. Clamp velocity
        xd = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, xd));
        
        // 6. Move and apply friction
        particle.particlephysics$setXd(xd);
        particle.particlephysics$move(xd, yd, zd);
        particle.particlephysics$setXd(xd * FRICTION);
        
        callback.cancel();
    }
}
```

Then register in `particlephysics.mixins.json`:
```json
"NewParticleMixin"
```


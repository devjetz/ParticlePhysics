# Particle Physics Mod - Changes Summary

## Overview
Enhanced the Particle Physics mod with improved physics for multiple particle types, addressing the initial requirements:
1. ✅ Bubble physics under water (not straight up)
2. ✅ Leaves lay flat on ground (not punched into it)
3. ✅ Leaves respond to heavy wind (can be blown off ground)
4. ✅ General additions to particle physics engine (bubble particles)

## Files Modified

### 1. FallingLeavesParticleMixin.java (Enhanced)
**Changes:**
- Added `particlephysics$onGround` state variable to track ground contact
- Implemented wind-based leaf lift-off system
- Heavy winds can now blow leaves off the ground (gust > 0.6)
- Leaves no longer stay stuck to ground permanently
- Improved rest behavior on ground to be more realistic

**Physics Details:**
- Leaves lay flat when `onGround` is true
- Wind force is calculated from: `Math.sin(age * 0.031 + x * 0.7 - z * 0.5)`
- Heavy wind threshold triggers when combined wind + gust > 0.6
- Leaf can be lifted with upward velocity when blown off: `0.015 + Math.random() * 0.01`

### 2. BubbleParticleMixin.java (NEW)
**Purpose:** Adds physics to bubble particles under water

**Features:**
- Replaces straight-up rising with swirling underwater physics
- Bubbles drift horizontally while rising
- Gentle undulating upward motion (Y velocity = 0.006 ± sine wave)
- Phase-based swirling: `Math.cos(phase) * 0.00045` and `Math.sin(phase * 0.73) * 0.00045`
- Bubbles only exist in water; removed immediately if they leave water

**Physics Parameters:**
- Max horizontal drift: ±0.02 units/tick
- Vertical rise rate: 0.006 + undulation
- Water check: Validates particle is still in water each tick
- Friction applied to drift velocities (0.988x per tick)

## New Mixin Files Created
1. `BubbleParticleMixin.java` - Underwater bubble physics

## Configuration Files Updated

### particlephysics.mixins.json
Added to client mixins:
- `BubbleParticleMixin`

## Physics Principles Applied

### Wind System
All particles use phase-based wind generation:
```java
double phase = age * PHASE_RATE + x * X_INFLUENCE + z * Z_INFLUENCE;
double gust = Math.sin(age * GUST_RATE + x * ... - z * ...);
// Horizontal motion = base_phase + gust component
```

### Gravity Application
Modified per particle type:
- Leaves: Normal gravity with wind interaction
- Bubbles: Negative (rising with physics-based drift)

### Velocity Clamping
Prevents unrealistic speeds:
```java
xd = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, xd));
zd = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, zd));
```

### Friction System
Each particle has unique friction coefficients:
- Leaves: 0.992 (horizontal), 0.996 (vertical)
- Bubbles: 0.988 (both axes)

## Behavioral Improvements

### Leaves (Before vs After)
**Before:**
- Fell straight down with only minimal drift
- Got stuck on ground and disappeared
- Unrealistic rigid falling

**After:**
- Natural fluttering and swirling
- Lay flat on ground surface
- Can be blown away by heavy wind
- Float on water surfaces
- Smooth, realistic tumbling motion

### Bubbles (Before vs After)
**Before:**
- Rose straight up (vanilla behavior)
- Ignored physics

**After:**
- Swirl and drift underwater with realistic buoyancy
- Gentle undulating upward motion
- Only exist while in water
- Realistic physics-based behavior

## Build Status
✅ Fabric build: SUCCESS
✅ NeoForge build: SUCCESS
✅ Client launch: SUCCESS (no mixin errors)

## Supported Versions
- Minecraft 26.1.2
- Fabric Loader 0.19.5
- NeoForge 26.1.2.114
- Java 25

## Performance Considerations
- Only adds per-tick calculations, no collision detection
- Sine/cosine calculations are relatively cheap
- No additional objects or memory allocation per particle
- Compatible with performance mods
- Minimal impact on frame rate

## Future Enhancement Possibilities
1. Configurable physics parameters via config file
2. Additional particles: more water particles, custom effects
3. Environment interaction (water currents, air flows)
4. Directional wind patterns
5. Advanced particle interaction systems


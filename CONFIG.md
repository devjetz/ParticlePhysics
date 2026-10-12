# Particle Physics Configuration Guide

## Overview

The Particle Physics mod now includes a JSON-based configuration system that allows you to customize particle behavior without needing to modify code.

## Configuration File

The configuration is stored in:
```
config/particlephysics.json
```

The file is automatically created on first run with default values.

## Configuration Options

### Master Settings

- **enablePhysics** (boolean, default: `true`)
  - Master toggle to enable/disable all particle physics
  - When disabled, particles behave with default Minecraft behavior

### Wind Control

- **windMultiplier** (number, default: `0.5`)
  - Controls the strength of wind effects on particles
  - Range: 0.0 (no wind) to 2.0 (double strength)
  - Lower values = less wind influence

### Particle Behavior

- **enableLeafPhysics** (boolean, default: `true`)
  - Enable physics for falling leaves
  - Leaves never poof on contact, float on water, and live until the leaf lifetime ends
- **windAffectsGroundedLeaves** (boolean, default: `true`)
  - `true`: wind keeps pushing leaves along the ground after landing; `false`: they lie still
- **enableBubblePhysics** (boolean, default: `true`)
  - Enable physics for underwater bubbles
  - Bubbles will swirl and rise naturally

- **leafDecaySeconds** (integer, default: `60`)
  - Total time from spawning until leaves disappear, including time spent floating or on the ground
  - Range: 1-3600 seconds (up to one hour)

## Example Configuration

```json
{
  "enablePhysics": true,
  "windMultiplier": 0.5,
  "enableLeafPhysics": true,
  "enableBubblePhysics": true,
  "windAffectsGroundedLeaves": true,
  "leafDecaySeconds": 60
}
```

## Loading Configuration

The configuration is loaded automatically when the game starts. To reload the configuration after editing:
1. Edit the `config/particlephysics.json` file
2. Restart the game or reload the world

## Future Enhancements

The following features are planned for future versions:
- Cloth Config integration for in-game GUI configuration
- Mod Menu support for easy configuration access
- Per-biome configuration overrides
- Particle fade-out transitions
- Additional particle types with physics support

## Troubleshooting

If the configuration file doesn't exist:
1. Start the game with the mod installed
2. The default configuration will be automatically generated

If the configuration file gets corrupted:
1. Delete the file or rename it
2. Restart the game to regenerate it with defaults

## Default Wind Physics

By default, the mod implements:
- **Leaf Wind System**: Leaves on the ground will lift up with strong wind
  - Wind continues to affect leaves after ground collisions
  - Adjust `windMultiplier` to control this behavior

- **Leaf lifetime**: Leaves last for up to 60 seconds by default
  - Adjust `leafDecaySeconds` to change this duration

- **Bubble Swirling**: Underwater bubbles swirl as they rise
  - Natural spiral motion
  - Cannot be disabled individually (use `enableBubblePhysics: false` to disable all bubble physics)

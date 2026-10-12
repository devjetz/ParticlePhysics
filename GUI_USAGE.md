# Particle Physics Configuration GUI

## Opening the Config Screen

Press **Ctrl+C** in-game to open the Particle Physics configuration GUI.

## Configuration Options

The config screen displays the following options:

### General Settings
- **Enable Physics** - Master toggle for all particle physics effects
- **Wind Multiplier** - Controls wind force magnitude
  - 0.0 = No wind
  - 0.5 = Half wind (default, recommended)
  - 1.0 = Normal wind
  - 2.0 = Double wind

### Particle Settings
- **Enable Leaf Physics** - Toggles whether leaves rest on ground and respond to wind
- **Enable Bubble Physics** - Toggles whether bubbles swirl naturally underwater
- **Leaf Decay Ticks** - How long leaves stay on ground before disappearing
  - Range: 10-300 ticks (20 ticks = 1 second)
  - Default: 90 ticks (~4.5 seconds)

## Using the GUI

1. **Press Ctrl+C** to open the configuration screen
2. **Click any button** to toggle or adjust a setting
3. **Wind/Decay controls** use +/- buttons to adjust values (increment by 0.1 for wind, 10 for decay)
4. **Click "← Back"** or **press ESC** to close the config screen
5. Changes are **saved automatically** to `config/particlephysics.json`

## Configuration File

You can also manually edit `config/particlephysics.json` with these fields:

```json
{
  "enablePhysics": true,
  "enableLeafPhysics": true,
  "enableBubblePhysics": true,
  "windMultiplier": 0.5,
  "leafDecayTicks": 90
}
```

Changes to the JSON file require a game restart to take effect, but GUI changes are applied immediately.

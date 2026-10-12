![Particle Physics](./common/src/main/resources/assets/minecraft_title.png)

<p align="center"><b>Particle Physics</b> is a client-side Minecraft mod that completely reimagines a good amount of particles! Adding new physics and better visuals, to make minecraft feel more at home.</p>

<div align="center"><a href="https://modrinth.com/mod/particle-physics"><img alt="modrinth" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg"></a> <a href="https:///www.curseforge.com/minecraft/mc-mods/the-particle-physics"><img alt="curseforge" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/curseforge_vector.svg"></a> <a href="https://github.com/devjetz/particle_physics"><img alt="github" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/github_vector.svg"></a></div>

---

Particle Physics gives selected vanilla particles more natural movement. Falling leaves flutter in the wind, drift across water, and can keep moving after landing. Underwater bubbles swirl as they rise.

This is a client-side visual mod. It does not add particle types, blocks, items, or gameplay content.

## Features

### Falling leaves

- Flutter and drift as they fall, with changing wind gusts.
- Float and move across water surfaces.
- Do not disappear when they touch the ground or a wall.
- Choose whether wind continues to move leaves after they land.
- Set how long leaves remain, from 1 second to 1 hour.

### Underwater bubbles

- Swirl and drift horizontally as they rise through water.

## Downloads and compatibility

Choose the jar that matches both your Minecraft version and mod loader:

| Minecraft | Loader | Java |
| --- | --- | --- |
| 26.1.2 | Fabric | 25 |
| 26.1.2 | NeoForge | 25 |
| 26.2 | Fabric | 25 |
| 26.2 | NeoForge | 25 |

Install the matching Fabric Loader or NeoForge version, then place the downloaded mod jar in your instance's `mods` folder. No additional mod dependency is required.

## Configuration

Press **P** in-game to open Particle Physics Settings. You can also change the key in Minecraft's **Options → Controls → Key Binds**.

The settings include:

- **Particle physics** — master toggle.
- **Leaf physics** and **Bubble physics** — enable or disable each supported particle behavior.
- **Wind on grounded leaves** — when enabled, wind continues to push landed leaves; when disabled, they stay still on the ground.
- **Wind multiplier** — adjust wind strength from 0 to 2.
- **Leaf lifetime** — set the time before leaves disappear, from 1 to 3600 seconds.

Settings are saved to `config/particlephysics.json` in the game directory. The file is created automatically when the mod first runs.

## For contributors

The Fabric and NeoForge projects use shared implementation code from `common/`. With Java 25 installed, build the loader jars from the project root:

```powershell
.\gradlew.bat -p fabric build
.\gradlew.bat -p neoforge build
```

For Minecraft 26.2, pass the version properties:

```powershell
.\gradlew.bat -p fabric "-Pminecraft_version=26.2" build
.\gradlew.bat -p neoforge "-Pminecraft_version=26.2" "-Pneo_version=26.2.0.90" build
```

Build artifacts are written to `fabric/build/libs/` and `neoforge/build/libs/`.


***This code has been written with AI.***

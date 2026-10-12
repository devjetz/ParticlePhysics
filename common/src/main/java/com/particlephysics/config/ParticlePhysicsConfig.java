package com.particlephysics.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ParticlePhysicsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ParticlePhysicsConfig INSTANCE;
    
    public boolean enablePhysics = true;
    public double windMultiplier = 0.5;
    public boolean enableLeafPhysics = true;
    public boolean enableBubblePhysics = true;
    public boolean windAffectsGroundedLeaves = true;
    public int leafDecaySeconds = 60;
    
    public static ParticlePhysicsConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }
    
    public static void reload() {
        INSTANCE = load();
    }
    
    private static ParticlePhysicsConfig load() {
        File configFile = getConfigFile();
        
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                ParticlePhysicsConfig loaded = GSON.fromJson(reader, ParticlePhysicsConfig.class);
                if (loaded != null) return loaded;
            } catch (IOException e) {
                System.err.println("Failed to load particle physics config: " + e.getMessage());
            }
        }
        
        ParticlePhysicsConfig config = new ParticlePhysicsConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try {
            File configFile = getConfigFile();
            configFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save particle physics config: " + e.getMessage());
        }
    }
    
    private static File getConfigFile() {
        return new File("config/particlephysics.json");
    }
}

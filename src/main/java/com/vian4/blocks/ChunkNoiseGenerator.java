package com.voidcitymc.blocks;

import com.jayfella.fastnoise.FastNoise;
import com.jayfella.fastnoise.LayeredNoise;
import com.jayfella.fastnoise.NoiseLayer;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.rvandoosselaer.blocks.*;
import com.simsilica.mathd.Vec3i;

import java.util.Random;

public class ChunkNoiseGenerator implements ChunkGenerator {

    private final long seed;
    private LayeredNoise layeredNoise;
    private LayeredNoise moistureNoise;
    private float waterHeight = 0f;

    public ChunkNoiseGenerator(long seed) {
        this.seed = seed;
        createWorldNoise();
        createMoistureNoise();
    }

    @Override
    public Chunk generate(Vec3i location) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        Chunk chunk = Chunk.createAt(location);

        for (int x = 0; x < BlocksConfig.getInstance().getChunkSize().x; x++) {
            for (int y = 0; y < BlocksConfig.getInstance().getChunkSize().y; y++) {
                for (int z = 0; z < BlocksConfig.getInstance().getChunkSize().z; z++) {
                    float height = getHeight(getWorldLocation(new Vector3f(x, 0, z), chunk));
                    int worldY = (chunk.getLocation().y * chunkSize.y) + y;
                    float h = Math.max(height, waterHeight);
                    if (worldY <= h) {
                        Block block;
                        if (worldY <= height) {
                            Biome biome = getBiome(getMoistureHeight(getWorldLocation(new Vector3f(x, 0, z), chunk)), height);
                            if (biome == Biome.SNOW) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS_SNOW);
                            } else if (biome == Biome.BARE) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.BEACH) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.GRASSLAND) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
                            } else if (biome == Biome.OCEAN) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRAVEL);
                            } else if (biome == Biome.SCORCHED) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.SHRUBLAND) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.SUBTROPICAL_DESERT) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.TAIGA) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.TEMPERATE_DECIDUOUS_FOREST) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
                            } else if (biome == Biome.TEMPERATE_DESERT) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.SAND);
                            } else if (biome == Biome.TEMPERATE_RAIN_FOREST) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
                            } else if (biome == Biome.TROPICAL_SEASONAL_FOREST) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
                            } else if (biome == Biome.TUNDRA) {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
                            } else {
                                block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
                            }

                        } else {
                            block = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.WATER);
                        }
                        chunk.addBlock(x, y, z, block);
                    }
                }
            }
        }

        return chunk;
    }

    private Vector3f getWorldLocation(Vector3f blockLocation, Chunk chunk) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        return new Vector3f((chunk.getLocation().x * chunkSize.x) + blockLocation.x,
                (chunk.getLocation().y * chunkSize.y) + blockLocation.y,
                (chunk.getLocation().z * chunkSize.z) + blockLocation.z);
    }

    private void createWorldNoise() {
        Random random = new Random(seed);
        layeredNoise = new LayeredNoise();

        layeredNoise.setHardFloor(true);
        layeredNoise.setHardFloorHeight(waterHeight);
        layeredNoise.setHardFloorStrength(0.6f);

        NoiseLayer mountains = new NoiseLayer("mountains");
        mountains.setSeed(random.nextInt());
        //mountains.setNoiseType(FastNoise.NoiseType.PerlinFractal);
        mountains.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        mountains.setStrength(128);
        mountains.setFrequency(mountains.getFrequency() / 4);
        layeredNoise.addLayer(mountains);

        NoiseLayer hills = new NoiseLayer("Hills");
        hills.setSeed(random.nextInt());
        //hills.setNoiseType(FastNoise.NoiseType.PerlinFractal);
        hills.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        hills.setStrength(64);
        hills.setFrequency(hills.getFrequency() / 2);
        layeredNoise.addLayer(hills);

        NoiseLayer details = new NoiseLayer("Details");
        details.setSeed(random.nextInt());
        //details.setNoiseType(FastNoise.NoiseType.PerlinFractal);
        details.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        details.setStrength(15);
        layeredNoise.addLayer(details);

        //try out cubic fractal, it seems interesting. Also found this: https://jobtalle.com/cubic_noise.html
    }

    private void createMoistureNoise() {
        Random random = new Random(seed);
        moistureNoise = new LayeredNoise();

        NoiseLayer details = new NoiseLayer("Moisture");
        details.setSeed(random.nextInt());
        details.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        moistureNoise.addLayer(details);
    }

    private float getHeight(Vector3f blockLocation) {
        float height = 12f;
        return layeredNoise.evaluate(new Vector2f(blockLocation.x, blockLocation.z)) + height;
    }

    private float getMoistureHeight(Vector3f blockLocation) {
        return moistureNoise.evaluate(new Vector2f(blockLocation.x, blockLocation.z));
    }

    public Biome getBiome(float moisture, float height) {
        if (height < 0.1) return Biome.OCEAN;
        if (height < 0.12) return Biome.BEACH;

        if (height > 0.8) {
            if (moisture < 0.1) return Biome.SCORCHED;
            if (moisture < 0.2) return Biome.BARE;
            if (moisture < 0.5) return Biome.TUNDRA;
            return Biome.SNOW;
        }

        if (height > 0.6) {
            if (moisture < 0.33) return Biome.TEMPERATE_DESERT;
            if (moisture < 0.66) return Biome.SHRUBLAND;
            return Biome.TAIGA;
        }

        if (height > 0.3) {
            if (moisture < 0.16) return Biome.TEMPERATE_DESERT;
            if (moisture < 0.50) return Biome.GRASSLAND;
            if (moisture < 0.83) return Biome.TEMPERATE_DECIDUOUS_FOREST;
            return Biome.TEMPERATE_RAIN_FOREST;
        }

        if (moisture < 0.16) return Biome.SUBTROPICAL_DESERT;
        if (moisture < 0.33) return Biome.GRASSLAND;
        if (moisture < 0.66) return Biome.TROPICAL_SEASONAL_FOREST;
        return Biome.TROPICAL_RAIN_FOREST;
    }

}

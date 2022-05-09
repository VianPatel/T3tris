package com.voidcitymc.blocks;

import com.github.luben.zstd.Zstd;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.rvandoosselaer.blocks.*;
import com.simsilica.mathd.Vec3i;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

public class ChunkRepo implements ChunkRepository {
    @Override
    public Chunk load(Vec3i vec3i) {
        if (vec3i.equals(new Vec3i(0,0,0))) {
            return (new FlatTerrainGenerator(Block.builder()
                    .name(BlockIds.getName(TypeIds.GRASS, ShapeIds.CUBE)) // this is a helper method to create a generic name: "grass-pole_up"
                    .shape(ShapeIds.CUBE)
                    .type(TypeIds.GRASS)
                    .solid(true)
                    .transparent(false)
                    .usingMultipleImages(true)
                    .build()).generate(vec3i));
        }
        return null;
        /*Block myGrassyBlock = BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS);
        myGrassyBlock.setShape(ShapeIds.SLAB_DOWN);

        Chunk chunk = Chunk.createAt(new Vec3i());
        for (int x = vec3i.x; x < BlocksConfig.getInstance().getChunkSize().x+vec3i.x; x++) {
            for (int z = vec3i.z; z < BlocksConfig.getInstance().getChunkSize().z+vec3i.z; z++) {
                chunk.addBlock(x, 0, z, myGrassyBlock);
            }
        }
        chunk.update();

        return chunk;*/
    }

    @Override
    public boolean save(Chunk chunk) {
        return false;
        /*
        Block[] blocks = chunk.getBlocks();
        SerializableBlock[] serializableBlock = new SerializableBlock[blocks.length];
        for (int i = 0; i < serializableBlock.length; i++) {
            Block selectedBlock = blocks[i];
            if (selectedBlock != null) {
                serializableBlock[i] = (SerializableBlock) selectedBlock;
            }
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializableBlock);
            oos.flush();
            byte[] compressedData = Zstd.compress(bos.toByteArray(), 3);


            FileOutputStream fos = new FileOutputStream(new File("myTestChunk"));
            fos.write(compressedData);
            fos.close();



        } catch (IOException e) {
            return false;
        }
        return true;
         */
    }

    private Block getRandomBlock() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        List<Block> blocks = new ArrayList<>(blockRegistry.getAll());
        int random = FastMath.nextRandomInt(0, blocks.size() - 1);
        return blocks.get(random);
    }
}

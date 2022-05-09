package com.voidcitymc.blocks;
import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.rvandoosselaer.blocks.*;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.voidcitymc.blocks.api.entity.Player;

import java.util.HashMap;
import java.util.Iterator;


public class Main extends SimpleApplication implements ChunkManagerListener {

    private Node chunkNode;
    private Geometry addPlaceholder;
    private Geometry removePlaceholder;
    private ChunkManager chunkManager;
    private BlockRegistry blockRegistry;

    public static Camera cam;

    private static HashMap<Vector3f, RigidBodyControl> oldPhysicsBodyControlsForRemoval = new HashMap<>();

    //public HashMap<Vector3f, PhysicsRigidBody> physicsChunk = new HashMap<>();

    private static BulletAppState bulletAppState;

    public static Player player;
    public static boolean left = false, right = false, up = false, down = false, jump = false, run = false, leftClick = false, rightClick = false;

    public static boolean waitingForChunksToLoad = true;

    public static void main(String[] args) {

        Main game = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Ram Blocks");
        settings.put("Width", 1280);
        settings.put("Height", 720);
        game.setShowSettings(false);
        game.setSettings(settings);
        game.start();
    }

    public Main() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new WireframeState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState());
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        //GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        //Don't use that because you need to have grovy installed too

        BlocksConfig.initialize(assetManager);

        BlocksTheme myNewTheme = BlocksTheme.builder()
                .name("My awesome theme")
                .path("/Blocks/Themes/faithful/")
                .build();

        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        typeRegistry.setTheme(myNewTheme);

        BlocksConfig.getInstance().setGrid(new Vec3i(3, 3, 3));
        BlocksConfig.getInstance().setPhysicsGrid(new Vec3i(3, 3, 3));
        blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Block block = Block.builder()
                .name(BlockIds.getName(TypeIds.GRASS, ShapeIds.CUBE)) // this is a helper method to create a generic name: "grass-pole_up"
                .shape(ShapeIds.CUBE)
                .type(TypeIds.GRASS)
                .solid(true)
                .transparent(false)
                .usingMultipleImages(true)
                .build();
        blockRegistry.register(block);

        addBlocksToRegistry();

        chunkManager = ChunkManager.builder()
                .generatorPoolSize(2)
                .meshPoolSize(2)
                //.generator(new FlatTerrainGenerator(block))
                .repository(new ChunkRepo())
                .generator(new ChunkNoiseGenerator(System.currentTimeMillis()))
                .triggerAdjacentChunkUpdates(true)
                .build();


        chunkNode = new Node("chunk-node");

        bulletAppState = new BulletAppState();



        hideCursor();
        createCrossHair();
        createBlockPointers();

        rootNode.attachChild(chunkNode);

        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1f)));
        //rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), new ColorRGBA(2f, 2f, 2f, 1f)));

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);

        CamUtils.setFov(cam, 70.0f, 0.001f, 100f);

        stateManager.attach(bulletAppState);

        stateManager.attachAll(new ChunkManagerState(chunkManager), new ChunkPagerState(chunkNode, chunkManager), new PhysicsChunkPagerState(getPhysicsSpace(), chunkManager));

        initPlayer();

        //createBottomItemBar();



    }

    public void addBlocksToRegistry() {
        Block grassPole = Block.builder()
                .name(BlockIds.getName(TypeIds.GRASS, ShapeIds.POLE)) // this is a helper method to create a generic name: "grass-pole_up"
                .shape(ShapeIds.POLE)
                .type(TypeIds.GRASS)
                .solid(true)
                .transparent(false)
                .usingMultipleImages(true)
                .build();
        blockRegistry.register(grassPole);
    }

    public void initPlayer() {

        Node playerNode = new Node("PlayerNode");
        Node camera = new Node("camera");
        camera.setLocalTranslation(0, 1.7f, 0); //head
        playerNode.attachChild(camera);
        player = new Player(0.35f, 1.9f, 50f);
        playerNode.addControl(player);
        playerNode.addControl(new CameraFollowSpatial(cam));

        getPhysicsSpace().addAll(playerNode);
        rootNode.attachChild(playerNode);

        player.warp(new Vector3f(3, 20, 3));
        player.setGravity(new Vector3f(0, -10f, 0));
        //player.setJumpForce(new Vector3f(0, 250, 0));
        player.setJumpForce(new Vector3f(0, 250f, 0));

        chunkManager.addListener(this);

        FirstPersonMovementsControl firstPersonMovementsControl = new FirstPersonMovementsControl(inputManager, this::addBlock, this::removeBlock);
        inputManager.addListener(firstPersonMovementsControl);
        rootNode.addControl(firstPersonMovementsControl);
    }

    @Override
    public void simpleUpdate(float tpf) {
        ChunkPagerState chunkPager = stateManager.getState(ChunkPagerState.class);
        chunkPager.setLocation(new Vector3f(cam.getLocation()));

        if (waitingForChunksToLoad && getBulletAppState().isEnabled()) {
            getBulletAppState().setEnabled(false);
        }

        if (!waitingForChunksToLoad && !getBulletAppState().isEnabled()) {
            getBulletAppState().setEnabled(true);
        }

        PhysicsChunkPagerState physicsChunkPager = stateManager.getState(PhysicsChunkPagerState.class);
        physicsChunkPager.setLocation(new Vector3f(cam.getLocation()));

        if (waitingForChunksToLoad) {
            Worker.runAsyncTask(() -> {
                Iterator<PhysicsRigidBody> rigidBodyIterator = getPhysicsSpace().getRigidBodyList().iterator();
                while (rigidBodyIterator.hasNext()) {
                    PhysicsRigidBody rigidBody = rigidBodyIterator.next();
                    if (rigidBody.getPhysicsLocation().equals(chunkWorldLocationAtLoc(cam.getLocation()))) {
                        waitingForChunksToLoad = false;
                    }
                }
            });
        }



        CollisionResult result = getCollisionResult();

        updatePlaceholders(result);

        if (leftClick) {
            addBlock();
        } else if (rightClick) {
            removeBlock();
        }
    }


    private void createCrossHair() {
        Label label = new Label("+");
        label.setColor(ColorRGBA.White);

        cam = getCamera();
        int width = cam.getWidth();
        int height = cam.getHeight();
        label.setLocalTranslation((((float) width) / 2) - (label.getPreferredSize().getX() / 2), (((float) height) / 2) + (label.getPreferredSize().getY() / 2), label.getLocalTranslation().getZ());

        guiNode.attachChild(label);
    }

    public void createBottomItemBar() {
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Assets/Itembar/ItembarSlotsPentagon.png", true);
        int width = 900;
        int height = 900;
        pic.setWidth(width / 10);
        pic.setHeight(height / 10);
        pic.setPosition(settings.getWidth() / 2, settings.getHeight() / 8);
        guiNode.attachChild(pic);
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

    private void createBlockPointers() {
        Material removePlaceholderMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        removePlaceholderMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        removePlaceholderMaterial.setColor("Color", new ColorRGBA(1, 0, 0, 0.2f));

        removePlaceholder = new Geometry("remove-placeholder", new Box(0.505f, 0.505f, 0.505f));
        removePlaceholder.setMaterial(removePlaceholderMaterial);
        removePlaceholder.setQueueBucket(RenderQueue.Bucket.Transparent);
        removePlaceholder.setLocalScale(BlocksConfig.getInstance().getBlockScale());

        Material addPlaceholderMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        addPlaceholderMaterial.setColor("Color", ColorRGBA.Yellow);

        addPlaceholder = new Geometry("add-placeholder", new WireBox(0.5f, 0.5f, 0.5f));
        addPlaceholder.setMaterial(addPlaceholderMaterial);
        addPlaceholder.setQueueBucket(RenderQueue.Bucket.Transparent);
        addPlaceholder.setLocalScale(BlocksConfig.getInstance().getBlockScale());
    }

    private CollisionResult getCollisionResult() {
        CollisionResults collisionResults = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());

        chunkNode.collideWith(ray, collisionResults);

        return collisionResults.size() > 0 ? collisionResults.getClosestCollision() : null;
    }

    private void updatePlaceholders(CollisionResult result) {
        if (result != null) {
            Vec3i pointingLocation = ChunkManager.getBlockLocation(result);
            Vector3f offset = new Vector3f(0.5f, 0.5f, 0.5f);
            removePlaceholder.setLocalTranslation(pointingLocation.toVector3f().addLocal(offset).multLocal(BlocksConfig.getInstance().getBlockScale()));
            if (removePlaceholder.getParent() == null) {
                rootNode.attachChild(removePlaceholder);
            }

            Vec3i placingLocation = ChunkManager.getNeighbourBlockLocation(result);
            addPlaceholder.setLocalTranslation(placingLocation.toVector3f().addLocal(offset).multLocal(BlocksConfig.getInstance().getBlockScale()));
            if (addPlaceholder.getParent() == null) {
                rootNode.attachChild(addPlaceholder);
            }
        } else {
            addPlaceholder.removeFromParent();
            removePlaceholder.removeFromParent();
        }
    }

    private void addBlock() {
        Vector3f blockLocation = addPlaceholder.getWorldTranslation();

        Block grassBlock = blockRegistry.get(BlockIds.getName(TypeIds.GRASS, ShapeIds.CUBE));

        chunkManager.addBlock(blockLocation.subtract(0.5f, 0.5f, 0.5f), grassBlock);
    }

    private void removeBlock() {
        Vector3f blockLocation = removePlaceholder.getWorldTranslation();
        chunkManager.removeBlock(blockLocation.subtract(0.5f, 0.5f, 0.5f));
    }

    public static PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
        //if that doesn't work use this:
        //return stateManager.getState(BulletAppState.class).getPhysicsSpace();
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    @Override
    public void onChunkUpdated(Chunk chunk) {
    }


    @Override
    public void onChunkAvailable(Chunk chunk) {
    }

    public boolean insideBlock(Vector3f location) {
        return (chunkManager.getBlock(location) != null);
    }

    public static Vec3i chunkAtLoc(Vector3f location) {
        return ChunkManager.getChunkLocation(location);
    }

    public static Vector3f chunkWorldLocationAtLoc(Vector3f location) {
        Vec3i vec3i = chunkAtLoc(location);
        Vector3f worldLocation = vec3i.toVector3f();
        worldLocation.setX(worldLocation.x+0.5f);
        worldLocation.setY(worldLocation.y+0.5f);
        worldLocation.setZ(worldLocation.z+0.5f);
        return worldLocation;
    }
}

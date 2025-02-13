package com.destrostudios.survivors.client.appstates;

import com.destrostudios.survivors.game.Game;
import com.jme3.anim.AnimComposer;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import com.jme3.util.BufferUtils;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GameAppState extends MyBaseAppState {

    private Geometry instancedEnemies;
    private Label labelEnemies;
    private Label labelSpawnsPerSecond;
    private Slider sliderSpawnsPerSecond;
    private Spatial player;
    private ArrayList<Node> enemies = new ArrayList<>();
    private float timeWithoutSpawn;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);

        mainApplication.getCamera().setLocation(new Vector3f(0, 15, 10));
        mainApplication.getCamera().lookAt(new Vector3f(), Vector3f.UNIT_Y);

        mainApplication.getRootNode().addLight(new AmbientLight());
        mainApplication.getRootNode().addLight(new DirectionalLight(new Vector3f(-1, -5, -1).normalizeLocal()));

        player = mainApplication.getAssetManager().loadModel("models/medieval_knight/model.j3o");
        player.getControl(AnimComposer.class).setCurrentAction("idle");
        mainApplication.getRootNode().attachChild(player);

        initGui();
    }

    private void initGui() {
        Container container = new Container();
        container.setLocalTranslation(20, mainApplication.getContext().getFramebufferHeight() - 20, 0);
        TbtQuadBackgroundComponent containerBackground = (TbtQuadBackgroundComponent) container.getBackground();
        containerBackground.setColor(new ColorRGBA(containerBackground.getColor().getRed(), containerBackground.getColor().getGreen(), containerBackground.getColor().getBlue(), 0.95f));

        Label labelContent = new Label(Game.CONTENT);
        container.addChild(labelContent);

        labelEnemies = new Label("");
        container.addChild(labelEnemies);

        labelSpawnsPerSecond = new Label("");
        container.addChild(labelSpawnsPerSecond);

        sliderSpawnsPerSecond = new Slider(new DefaultRangedValueModel(0, 8000, 1000));
        sliderSpawnsPerSecond.setPreferredSize(new Vector3f(200, 20, 0));
        container.addChild(sliderSpawnsPerSecond);

        mainApplication.getGuiNode().attachChild(container);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        labelEnemies.setText("Enemies: " + enemies.size());
        int spawnsPerSecond = (int) sliderSpawnsPerSecond.getModel().getValue();
        labelSpawnsPerSecond.setText("Spawns per second: " + spawnsPerSecond);

        if (spawnsPerSecond == 0) {
            timeWithoutSpawn = 0;
        } else {
            timeWithoutSpawn += tpf;
            int spawns = (int) (timeWithoutSpawn * spawnsPerSecond);
            for (int i = 0; i < spawns; i++) {
                spawnEnemy();
            }
            timeWithoutSpawn -= ((float) spawns) / spawnsPerSecond;
        }

        float enemySpeed = 5;
        for (int i = 0; i < enemies.size(); i++) {
            Node enemy = enemies.get(i);
            Vector3f distanceToPlayer = enemy.getLocalTranslation().subtract(player.getLocalTranslation());
            if (distanceToPlayer.length() > 0.2f) {
                enemy.move(distanceToPlayer.normalizeLocal().multLocal(-1 * enemySpeed * tpf));
            } else {
                mainApplication.getRootNode().detachChild(enemy);
                enemies.remove(i);
                i--;
            }
        }

        updateInstancedEnemies();
    }

    private void spawnEnemy() {
        Node ghost = (Node) mainApplication.getAssetManager().loadModel("models/ghost/model.j3o");
        float angle = FastMath.nextRandomFloat() * FastMath.TWO_PI;
        float spawnRadius = 22;
        float x = FastMath.sin(angle) * spawnRadius;
        float z = FastMath.cos(angle) * spawnRadius;
        ghost.setLocalTranslation(new Vector3f(x, 0, z));
        ghost.lookAt(new Vector3f(), Vector3f.UNIT_Y);
        enemies.add(ghost);
    }

    private void updateInstancedEnemies() {
        if (enemies.isEmpty()) {
            if (instancedEnemies != null) {
                instancedEnemies.setCullHint(Spatial.CullHint.Always);
            }
            return;
        }

        if (instancedEnemies == null) {
            Node referenceEnemy = enemies.get(0);
            Geometry referenceGeometry = (Geometry) referenceEnemy.getChild("ghost_0");
            Mesh referenceMesh = referenceGeometry.getMesh();

            VertexBuffer instanceDataBuffer = new VertexBuffer(VertexBuffer.Type.InstanceData);
            instanceDataBuffer.setInstanced(true);
            referenceMesh.setBuffer(instanceDataBuffer);

            instancedEnemies = new Geometry("enemies", referenceMesh);
            Material material = referenceGeometry.getMaterial();
            material.setBoolean("UseInstancing", true);
            instancedEnemies.setMaterial(material);
            mainApplication.getRootNode().attachChild(instancedEnemies);

        }
        instancedEnemies.setCullHint(Spatial.CullHint.Never);

        Mesh mesh = instancedEnemies.getMesh();
        VertexBuffer instanceDataBuffer = mesh.getBuffer(VertexBuffer.Type.InstanceData);

        FloatBuffer instanceData = BufferUtils.createFloatBuffer(enemies.size() * 16);
        enemies.forEach(enemy -> enemy.getWorldTransform().toTransformMatrix().fillFloatBuffer(instanceData, true));
        instanceData.flip();
        if (instanceDataBuffer.getData() == null) {
            instanceDataBuffer.setupData(VertexBuffer.Usage.Static, 16, VertexBuffer.Format.Float, instanceData);
        } else {
            instanceDataBuffer.updateData(instanceData);
            mesh.updateCounts();
        }
    }
}

package com.destrostudios.survivors.client.appstates;

import com.destrostudios.survivors.client.MaterialUtil;
import com.jme3.anim.AnimComposer;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class GameAppState extends MyBaseAppState {

    private Geometry instancedEnemies;
    private Spatial player;
    private ArrayList<Transform> enemies = new ArrayList<>();
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

        initInstancedEnemies();
    }

    private void initInstancedEnemies() {
        Node node = (Node) mainApplication.getAssetManager().loadModel("models/ghost/model.j3o");
        Geometry geometry = (Geometry) node.getChild("ghost_0");
        Mesh mesh = geometry.getMesh();

        VertexBuffer instanceDataBuffer = new VertexBuffer(VertexBuffer.Type.InstanceData);
        instanceDataBuffer.setInstanced(true);
        mesh.setBuffer(instanceDataBuffer);

        instancedEnemies = new Geometry("enemies", mesh);
        Material material = geometry.getMaterial();
        material.setBoolean("UseInstancing", true);
        instancedEnemies.setMaterial(material);
        mainApplication.getRootNode().attachChild(instancedEnemies);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        MenuAppState menuAppState = getAppState(MenuAppState.class);

        int spawnsPerSecond = menuAppState.getSpawnsPerSecond();
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

        menuAppState.setEnemiesCount(enemies.size());

        float enemySpeed = 5;
        for (int i = 0; i < enemies.size(); i++) {
            Transform enemy = enemies.get(i);
            Vector3f distanceToPlayer = player.getLocalTranslation().subtract(enemy.getTranslation());
            if (distanceToPlayer.length() > 0.2f) {
                enemy.setTranslation(enemy.getTranslation().add(distanceToPlayer.normalizeLocal().multLocal(enemySpeed * tpf)));
            } else {
                enemies.remove(i);
                i--;
            }
        }

        updateInstancedEnemies();
    }

    private void spawnEnemy() {
        float angle = FastMath.nextRandomFloat() * FastMath.TWO_PI;
        float spawnRadius = 22;
        float x = FastMath.sin(angle) * spawnRadius;
        float z = FastMath.cos(angle) * spawnRadius;
        Transform enemy = new Transform();
        enemy.setTranslation(x, 0, z);
        Vector3f distanceToPlayer = player.getLocalTranslation().subtract(enemy.getTranslation());
        enemy.setRotation(new Quaternion().lookAt(distanceToPlayer, Vector3f.UNIT_Y));
        enemies.add(enemy);
    }

    private void updateInstancedEnemies() {
        if (enemies.isEmpty()) {
            instancedEnemies.setCullHint(Spatial.CullHint.Always);
            return;
        }
        instancedEnemies.setCullHint(Spatial.CullHint.Never);

        Mesh mesh = instancedEnemies.getMesh();
        VertexBuffer instanceDataBuffer = mesh.getBuffer(VertexBuffer.Type.InstanceData);

        FloatBuffer instanceData = BufferUtils.createFloatBuffer(enemies.size() * 16);
        enemies.forEach(enemy -> enemy.toTransformMatrix().fillFloatBuffer(instanceData, true));
        instanceData.flip();

        if (instanceDataBuffer.getData() == null) {
            instanceDataBuffer.setupData(VertexBuffer.Usage.Static, 16, VertexBuffer.Format.Float, instanceData);
        } else {
            instanceDataBuffer.updateData(instanceData);
            mesh.updateCounts();
        }
    }

    public void setEnemyLightingMaterialEnabled(boolean enabled) {
        MaterialUtil.setMaterialLightingEnabled(instancedEnemies, mainApplication.getAssetManager(), enabled);
    }
}

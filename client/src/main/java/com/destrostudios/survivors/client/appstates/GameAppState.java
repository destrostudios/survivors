package com.destrostudios.survivors.client.appstates;

import com.destrostudios.survivors.game.Game;
import com.jme3.anim.AnimComposer;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Label;

import java.util.ArrayList;

public class GameAppState extends MyBaseAppState {

    private float timeSinceLastSpawn;
    private Spatial player;
    private ArrayList<Node> enemies = new ArrayList<>();

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);

        Label labelContent = new Label(Game.CONTENT);
        labelContent.setLocalTranslation(20, mainApplication.getContext().getFramebufferHeight() - 20, 0);
        labelContent.setFontSize(30);
        labelContent.setColor(ColorRGBA.White);
        mainApplication.getGuiNode().attachChild(labelContent);

        mainApplication.getCamera().setLocation(new Vector3f(0, 15, 15));
        mainApplication.getCamera().lookAt(new Vector3f(), Vector3f.UNIT_Y);

        mainApplication.getRootNode().addLight(new AmbientLight());
        mainApplication.getRootNode().addLight(new DirectionalLight(new Vector3f(-1, -5, -1).normalizeLocal()));

        player = mainApplication.getAssetManager().loadModel("models/medieval_knight/model.j3o");
        player.getControl(AnimComposer.class).setCurrentAction("idle");
        mainApplication.getRootNode().attachChild(player);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        timeSinceLastSpawn += tpf;
        if (timeSinceLastSpawn > 0.01f) {
            Node ghost = (Node) mainApplication.getAssetManager().loadModel("models/ghost/model.j3o");
            float angle = FastMath.nextRandomFloat() * FastMath.TWO_PI;
            float enemySpawnRadius = 30;
            float x = FastMath.sin(angle) * enemySpawnRadius;
            float z = FastMath.cos(angle) * enemySpawnRadius;
            ghost.setLocalTranslation(new Vector3f(x, 0, z));
            ghost.lookAt(new Vector3f(), Vector3f.UNIT_Y);
            ghost.getChild("Armature").getControl(AnimComposer.class).setCurrentAction("idle");
            mainApplication.getRootNode().attachChild(ghost);
            enemies.add(ghost);

            timeSinceLastSpawn = 0;
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
    }
}

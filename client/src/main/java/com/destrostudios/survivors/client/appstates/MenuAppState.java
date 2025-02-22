package com.destrostudios.survivors.client.appstates;

import com.jme3.app.Application;
import com.jme3.app.DetailedProfilerState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

public class MenuAppState extends MyBaseAppState {

    private Label labelEnemies;
    private Label labelSpawnsPerSecond;
    private Slider sliderSpawnsPerSecond;
    private Checkbox checkboxLightingMaterial;
    private DetailedProfilerState detailedProfilerState;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);

        Container container = new Container();
        container.setLocalTranslation(20, mainApplication.getContext().getFramebufferHeight() - 20, 0);
        TbtQuadBackgroundComponent containerBackground = (TbtQuadBackgroundComponent) container.getBackground();
        containerBackground.setColor(new ColorRGBA(containerBackground.getColor().getRed(), containerBackground.getColor().getGreen(), containerBackground.getColor().getBlue(), 0.95f));

        Label labelContent = new Label("Game.CONTENT");
        labelContent.setColor(ColorRGBA.White);
        container.addChild(labelContent);

        labelEnemies = new Label("");
        labelEnemies.setColor(ColorRGBA.White);
        container.addChild(labelEnemies);

        labelSpawnsPerSecond = new Label("");
        labelSpawnsPerSecond.setColor(ColorRGBA.White);
        container.addChild(labelSpawnsPerSecond);

        sliderSpawnsPerSecond = new Slider(new DefaultRangedValueModel(0, 50000, 2000));
        sliderSpawnsPerSecond.setPreferredSize(new Vector3f(250, 20, 0));
        container.addChild(sliderSpawnsPerSecond);

        checkboxLightingMaterial = new Checkbox("Enemy lighting material");
        checkboxLightingMaterial.setChecked(true);
        checkboxLightingMaterial.setColor(ColorRGBA.White);
        checkboxLightingMaterial.addClickCommands(_ -> setEnemyLightingMaterialEnabled(checkboxLightingMaterial.isChecked()));
        container.addChild(checkboxLightingMaterial);

        detailedProfilerState = new DetailedProfilerState();
        Checkbox checkboxProfiler = new Checkbox("Profiler");
        checkboxProfiler.setColor(ColorRGBA.White);
        checkboxProfiler.addClickCommands(_ -> setProfilerEnabled(checkboxProfiler.isChecked()));
        container.addChild(checkboxProfiler);

        mainApplication.getGuiNode().attachChild(container);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        labelSpawnsPerSecond.setText("Spawns per second: " + getSpawnsPerSecond());
    }

    public int getSpawnsPerSecond() {
        return (int) sliderSpawnsPerSecond.getModel().getValue();
    }

    public void setEnemiesCount(int enemiesCount) {
        labelEnemies.setText("Enemies: " + enemiesCount);
    }

    public void setEnemyLightingMaterialEnabled(boolean enabled) {
        getAppState(GameAppState.class).setEnemyLightingMaterialEnabled(enabled);
    }

    public void setProfilerEnabled(boolean enabled) {
        if (enabled) {
            mainApplication.getStateManager().attach(detailedProfilerState);
        } else {
            mainApplication.getStateManager().detach(detailedProfilerState);
        }
    }
}

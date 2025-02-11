package com.destrostudios.survivors.client.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.GuiGlobals;

public class OverlayAppState extends MyBaseAppState implements ActionListener {

    public OverlayAppState(String text, ColorRGBA color, boolean closeOnClick) {
        this.text = text;
        this.color = color;
        this.closeOnClick = closeOnClick;
        guiNode = new Node();
    }
    private String text;
    private ColorRGBA color;
    private boolean closeOnClick;
    private Node guiNode;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        int width = mainApplication.getContext().getSettings().getWidth();
        int height = mainApplication.getContext().getSettings().getHeight();

        Geometry background = new Geometry("background", new Quad(width, height));
        Material materialBackground = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialBackground.setColor("Color", new ColorRGBA(0, 0, 0, 0.7f));
        materialBackground.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        background.setMaterial(materialBackground);
        guiNode.attachChild(background);

        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText bitmapText = new BitmapText(guiFont);
        bitmapText.setSize(50);
        bitmapText.setColor(color);
        bitmapText.setText(text);
        float x = ((width / 2f) - (bitmapText.getLineWidth() / 2));
        float y = ((height / 2f) + (bitmapText.getHeight() / 2));
        bitmapText.setLocalTranslation(x, y, 0);
        guiNode.attachChild(bitmapText);

        mainApplication.getGuiNode().attachChild(guiNode);

        setLemurInputEnabled(false);

        if (closeOnClick) {
            initInputListeners();
        }
    }

    private void initInputListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("mouseLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "mouseLeft");
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (shouldClose()) {
            close();
        }
    }

    protected boolean shouldClose() {
        return false;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("mouseLeft") && isPressed) {
            close();
        }
    }

    protected void close() {
        mainApplication.getStateManager().detach(this);
        setLemurInputEnabled(true);
    }

    private void setLemurInputEnabled(boolean enabled) {
        GuiGlobals.getInstance().setCursorEventsEnabled(enabled);
        // Lemur hides the cursor when cursor events are disabled
        mainApplication.getInputManager().setCursorVisible(true);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getInputManager().deleteMapping("mouseLeft");
        mainApplication.getInputManager().removeListener(this);
    }
}

package com.destrostudios.survivors.client;

import com.jme3.asset.AssetManager;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class MaterialUtil {

    public static void setMaterialLightingEnabled(Geometry geometry, AssetManager assetManager, boolean enabled) {
        Material oldMaterial = geometry.getMaterial();
        Material newMaterial = new Material(assetManager, enabled ? "Common/MatDefs/Light/Lighting.j3md" : "Common/MatDefs/Misc/Unshaded.j3md");
        for (MatParam param : oldMaterial.getParams()) {
            String name = param.getName();
            if (enabled) {
                if (name.equals("ColorMap")) {
                    name = "DiffuseMap";
                } else if (name.equals("Color")) {
                    name = "Diffuse";
                }
            } else {
                if (name.equals("DiffuseMap")) {
                    name = "ColorMap";
                } else if (name.equals("Diffuse")) {
                    name = "Color";
                }
            }
            if (newMaterial.getMaterialDef().getMaterialParam(name) != null){
                newMaterial.setParam(name, param.getVarType(), param.getValue());
            }
        }
        if (enabled && (newMaterial.getParam("DiffuseMap") == null)) {
            newMaterial.setBoolean("UseMaterialColors", true);
            newMaterial.setColor("Ambient", ColorRGBA.White);
        }
        newMaterial.getAdditionalRenderState().setBlendMode(oldMaterial.getAdditionalRenderState().getBlendMode());
        geometry.setMaterial(newMaterial);
    }
}

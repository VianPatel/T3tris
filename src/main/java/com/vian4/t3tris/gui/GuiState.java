package com.vian4.t3tris.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.vian4.t3tris.T3tris;

public abstract class GuiState extends BaseAppState {

    protected T3tris t3tris;

    protected Node selfNode;

    @Override
    protected void initialize(Application app) {
        t3tris = (T3tris) app;
        selfNode = new Node();
        t3tris.getGuiNode().attachChild(selfNode);
        initialize();
    }

    protected abstract void initialize();

    @Override
    protected void onEnable() {
        t3tris.getGuiNode().attachChild(selfNode);
    }

    @Override
    protected void onDisable() {
        t3tris.getGuiNode().detachChild(selfNode);
    }

    @Override
    protected void cleanup(Application app) {
        onDisable();
        selfNode.detachAllChildren();
    }

    /*@Override
    public void update(float tpf) {
    }*/


}

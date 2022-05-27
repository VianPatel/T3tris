package com.vian4.t3tris.gui;

import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.vian4.t3tris.game.GameState;

public class WinState extends GuiState {

    private static boolean objectiveReached = false;

    @Override
    protected void initialize() {
        if (objectiveReached) {
            t3tris.getStateManager().detach(this);
            return;
        }

        t3tris.getStateManager().getState(GameState.class).setEnabled(false);

        int minDimension = Math.min(t3tris.getWidth(), t3tris.getHeight());
        float scale = 0.9f;
        Picture objectiveReached = new Picture("ObjectiveReached");
        objectiveReached.setImage(t3tris.getAssetManager(), "ObjectiveReached.png", true);
        float height = minDimension * (0.729166f) * scale;
        float width = minDimension * scale;
        objectiveReached.setWidth(width);
        objectiveReached.setHeight(height);
        objectiveReached.setPosition((t3tris.getWidth() - width) / 2, (t3tris.getHeight() - height) / 2);
        selfNode.attachChild(objectiveReached);

        Container startContainer = new Container();
        selfNode.attachChild(startContainer);
        startContainer.setLocalTranslation(t3tris.getWidth() / 2, t3tris.getHeight() / 4, 0);

        Button resumeButton = startContainer.addChild(new Button("Continue Playing"));
        resumeButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                t3tris.getStateManager().detach(t3tris.getStateManager().getState(Title.class));
                t3tris.getStateManager().getState(GameState.class).setEnabled(true);
            }
        });
        resumeButton.setTextHAlignment(HAlignment.Center);

    }

}

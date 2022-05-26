package com.vian4.t3tris.gui;

import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

public class Tutorial extends GuiState {

    private int page = 1;

    Command<Button> nextPageCommand = new Command<Button>() {
        @Override
        public void execute(Button source) {
            page++;
            updatePage();
        }
    };

    @Override
    protected void initialize() {
        updatePage();
    }

    private void setupBasicFunction() {
        Container startContainer = new Container();
        selfNode.attachChild(startContainer);
        startContainer.setLocalTranslation(0.9f * t3tris.getWidth(), 0.1f * t3tris.getHeight(), 0.0f);

        Button startButton = startContainer.addChild(new Button("Next"));
        startButton.setTextHAlignment(HAlignment.Center);
        startButton.addClickCommands(nextPageCommand);
    }
    
    private void updatePage() {
        selfNode.detachAllChildren();
        setupBasicFunction();
        
        int minDimension = Math.min(t3tris.getWidth(), t3tris.getHeight());
        float scale;
        float height;
        float width;
        switch (page) {
            case 1:
                Picture keybinds = new Picture("Keybinds");
                keybinds.setImage(t3tris.getAssetManager(), "TutorialKeybinds.png", true);
                scale = 0.9f;
                height = minDimension * (0.729166f) * scale;
                width = minDimension * scale;
                keybinds.setWidth(width);
                keybinds.setHeight(height);
                keybinds.setPosition((t3tris.getWidth() - width) / 2, (t3tris.getHeight() - height) / 2);
                selfNode.attachChild(keybinds);
                break;
            case 2:
                Picture mouseControls = new Picture("MouseControls");
                mouseControls.setImage(t3tris.getAssetManager(), "TutorialMouseControls.png", true);
                scale = 0.9f;
                height = minDimension * (0.729166f) * scale;
                width = minDimension * scale;
                mouseControls.setWidth(width);
                mouseControls.setHeight(height);
                mouseControls.setPosition((t3tris.getWidth() - width) / 2, (t3tris.getHeight() - height) / 2);
                selfNode.attachChild(mouseControls);
                break;
            case 3:
                Picture tutorialGameObjective = new Picture("TutorialGameObjective");
                tutorialGameObjective.setImage(t3tris.getAssetManager(), "TutorialGameObjective.png", true);
                scale = 0.9f;
                height = minDimension * (0.729166f) * scale;
                width = minDimension * scale;
                tutorialGameObjective.setWidth(width);
                tutorialGameObjective.setHeight(height);
                tutorialGameObjective.setPosition((t3tris.getWidth() - width) / 2, (t3tris.getHeight() - height) / 2);
                selfNode.attachChild(tutorialGameObjective);
                break;
            default:
                t3tris.getStateManager().detach(t3tris.getStateManager().getState(getClass()));
                t3tris.getStateManager().attach(new Title());
                break;
        }
    }
    
}

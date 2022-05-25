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
        switch (page) {
            case 1:
                Picture pic = new Picture("Keybinds");
                pic.setImage(t3tris.getAssetManager(), "TutorialKeybinds.png", true);
                int minDimension = Math.min(t3tris.getWidth(), t3tris.getHeight());
                float scale = 0.9f;
                float height = minDimension * (0.729166f) * scale;
                float width = minDimension * scale;
                pic.setWidth(width);
                pic.setHeight(height);
                pic.setPosition((t3tris.getWidth() - width) / 2, (t3tris.getHeight() - height) / 2);
                selfNode.attachChild(pic);
                break;
            default:
                t3tris.getStateManager().detach(t3tris.getStateManager().getState(Tutorial.class));
                t3tris.getStateManager().attach(new Title());
                break;
        }
    }
    
}

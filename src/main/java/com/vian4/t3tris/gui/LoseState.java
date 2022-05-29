package com.vian4.t3tris.gui;

import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.vian4.t3tris.game.GameState;

public class LoseState extends GuiState {

    @Override
    protected void initialize() {
        t3tris.getStateManager().detach(t3tris.getStateManager().getState(GameState.class));

        int minDimension = Math.min(t3tris.getWidth(), t3tris.getHeight());
        float scale = 0.9f;
        Picture gameEndPicture = new Picture("GameOver");
        gameEndPicture.setImage(t3tris.getAssetManager(), "GameOver.png", true);
        float height = minDimension * (0.729166f) * scale;
        float width = minDimension * scale;
        gameEndPicture.setWidth(width);
        gameEndPicture.setHeight(height);
        gameEndPicture.setPosition((t3tris.getWidth() - width) / 2, (t3tris.getHeight() - height) / 2);
        selfNode.attachChild(gameEndPicture);

        Container optionContainer = new Container();
        selfNode.attachChild(optionContainer);
        optionContainer.setLocalTranslation(t3tris.getWidth() / 2, t3tris.getHeight() / 4, 0);

        Button quitButton = optionContainer.addChild(new Button("Quit"));
        quitButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                t3tris.getStateManager().detach(LoseState.this);
                t3tris.stop();
            }
        });
        quitButton.setTextHAlignment(HAlignment.Center);

    }

}

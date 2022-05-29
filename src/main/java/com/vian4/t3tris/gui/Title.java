package com.vian4.t3tris.gui;

import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.vian4.t3tris.game.GameState;

public class Title extends GuiState {

    @Override
    protected void initialize() {
        Picture pic = new Picture("T3trisLogo");
        pic.setImage(t3tris.getAssetManager(), "T3trisLogo.png", true);
        pic.setWidth(t3tris.getWidth() / 2);
        pic.setHeight(t3tris.getHeight() / 2);
        pic.setPosition(t3tris.getWidth() / 4, t3tris.getHeight() / 4);
        selfNode.attachChild(pic);

        //Container welcomeContainer = new Container();
        //selfNode.attachChild(welcomeContainer);
        //welcomeContainer.setLocalTranslation(t3tris.getWidth() / 2, t3tris.getHeight() * 4 / 5, 0);
        //welcomeContainer.addChild(new Label("Welcome to"));


        Container optionContainer = new Container();
        selfNode.attachChild(optionContainer);
        optionContainer.setLocalTranslation(t3tris.getWidth() / 2, t3tris.getHeight() / 4, 0);

        Button startButton = optionContainer.addChild(new Button("Start"));
        startButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                t3tris.getStateManager().detach(Title.this);
                t3tris.getStateManager().attach(new GameState());
            }
        });
        startButton.setTextHAlignment(HAlignment.Center);

        Button tutorialButton = optionContainer.addChild(new Button("Tutorial"));
        tutorialButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                t3tris.getStateManager().detach(Title.this);
                t3tris.getStateManager().attach(new Tutorial());
            }
        });
        tutorialButton.setTextHAlignment(HAlignment.Center);
        
    }
    
}

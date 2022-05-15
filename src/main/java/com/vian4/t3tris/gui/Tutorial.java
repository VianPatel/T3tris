package com.vian4.t3tris.gui;

import com.jme3.ui.Picture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.vian4.t3tris.T3tris;
import com.vian4.t3tris.game.GameState;

public class Tutorial extends GuiState {

    @Override
    protected void initialize() {
        // Create a simple container for our elements
        Container myWindow = new Container();
        selfNode.attachChild(myWindow);

        // Put it somewhere that we will see it
        // Note: Lemur GUI elements grow down from the upper left corner.
        myWindow.setLocalTranslation(T3tris.WIDTH / 2, T3tris.HEIGHT * 3 / 4, 0);

        Picture pic = new Picture("T3trisLogo");
        pic.setImage(t3tris.getAssetManager(), "T3trisLogo.png", true);
        pic.setWidth(T3tris.WIDTH / 2);
        pic.setHeight(T3tris.HEIGHT / 2);
        pic.setPosition(T3tris.WIDTH / 4, T3tris.HEIGHT / 4);
        selfNode.attachChild(pic);

        // Add some elements
        myWindow.addChild(new Label("Welcome to T3^Dtris"));
        Button clickMe = myWindow.addChild(new Button("Click Me"));
        clickMe.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                System.out.println("Clicked");
                t3tris.getStateManager().detach(t3tris.getStateManager().getState(Tutorial.class));
                t3tris.getStateManager().attach(new GameState());
            }
        });
        
    }
    
}

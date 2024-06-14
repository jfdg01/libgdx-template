package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;

public class OptionsScreen extends BaseScreen {

    private Slider volumeSlider;
    private TextButton backButton;
    private ScreenManager screenManager;

    public OptionsScreen(MyAssetManager assetManager, AudioManager audioManager, SpineAnimationHandler spineAnimationHandler, ScreenManager screenManager) {
        super(assetManager, audioManager, spineAnimationHandler, screenManager);
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        super.show();

        Skin skin = assetManager.get(Constants.Skin.JSON, Skin.class);

        // Create a slider for volume control
        volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(audioManager.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.setVolume(volumeSlider.getValue());
            }
        });

        // Create a back button
        backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Navigate back to the main menu
                screenManager.setScreen(Constants.ScreenType.MENU);
            }
        });

        // Arrange the UI elements in a table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(new Label("Options", skin)).padBottom(20).row();
        table.add(new Label("Volume", skin)).padBottom(10).row();
        table.add(volumeSlider).width(300).padBottom(20).row();
        table.add(backButton).width(150).height(50).padTop(10);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}

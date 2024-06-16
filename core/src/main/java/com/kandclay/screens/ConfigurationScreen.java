package com.kandclay.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;

public class ConfigurationScreen extends BaseScreen {

    private Slider volumeSlider;
    private TextButton backButton;
    private TextButton hairColorButton;
    private TextButton heightButton;

    private Constants.HairColor currentHairColor = Constants.HairColor.BLONDE;
    private Constants.Height currentHeight = Constants.Height.AVERAGE;

    public ConfigurationScreen(SpineAnimationHandler spineAnimationHandler, ScreenManager screenManager) {
        super(spineAnimationHandler, screenManager);
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
                screenManager.setScreen(Constants.ScreenType.MENU);
            }
        });

        // Create a hair color button
        hairColorButton = new TextButton("Hair Color: " + currentHairColor, skin);
        hairColorButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentHairColor = currentHairColor.next();
                hairColorButton.setText("Hair Color: " + currentHairColor);
                configManager.setPreference("hairColor", currentHairColor.toString());
            }
        });

        // Create a height button
        heightButton = new TextButton("Height: " + currentHeight, skin);
        heightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentHeight = currentHeight.next();
                heightButton.setText("Height: " + currentHeight);
                configManager.setPreference("height", currentHeight.toString());
            }
        });

        // Arrange the UI elements in a table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(new Label("Options", skin)).padBottom(Constants.Buttons.PADDING).row();
        table.add(new Label("Volume", skin)).padBottom(Constants.Buttons.PADDING).row();
        table.add(volumeSlider).width(Constants.Buttons.SLIDER_WIDTH).padBottom(Constants.Buttons.PADDING).row();
        table.add(hairColorButton).width(Constants.Buttons.CONTROL_BUTTON_WIDTH).height(Constants.Buttons.CONTROL_BUTTON_HEIGHT).padBottom(Constants.Buttons.PADDING).row();
        table.add(heightButton).width(Constants.Buttons.CONTROL_BUTTON_WIDTH).height(Constants.Buttons.CONTROL_BUTTON_HEIGHT).padBottom(Constants.Buttons.PADDING).row();
        table.add(backButton).width(Constants.Buttons.BACK_BUTTON_WIDTH).height(Constants.Buttons.CONTROL_BUTTON_HEIGHT).padTop(Constants.Buttons.PADDING);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}


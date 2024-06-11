package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;

public class MenuScreen extends BaseScreen {

    ScreenManager screenManager;

    public MenuScreen(MyAssetManager assetManager, AudioManager audioManager, ScreenManager screenManager) {
        super(assetManager, audioManager);
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);

        Skin skin = assetManager.get(Constants.Skin.JSON, Skin.class);

        // Create buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add listeners to buttons
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.setScreen(Constants.ScreenType.KNIFE1); // Or the main game screen
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.setScreen(Constants.ScreenType.OPTIONS);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // Arrange buttons in a table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(startButton).width(200).height(60).pad(10);
        table.row();
        table.add(optionsButton).width(200).height(60).pad(10);
        table.row();
        table.add(exitButton).width(200).height(60).pad(10);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}


